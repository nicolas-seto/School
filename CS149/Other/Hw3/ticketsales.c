#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <signal.h>
#include <sys/time.h>
#include <string.h>

#define ROWS 10
#define COLS 10
#define CUST_NAME_LENGTH 5
#define ROWS_SELLERS 10
#define COLS_SELLERS 3
#define H_SELLERS 1
#define M_SELLERS 3
#define L_SELLERS 6
#define MAX_QUEUE 100

#define SALES_HOUR_DURATION 60

char*** setupSeats(int rowSize, int columnSize);
int freeSeats(char*** seats);
char* joinQueue(int sellerIndex);
void sellTicket(int sellerId);
void seatCustomer(char* customer);
void print(char* event);
int randomRange(int min, int max);

char*** seats; // 2d array that holds customer names.

char*** queues; // 2d array of each of seller's queue.
int queueSize[ROWS_SELLERS]; // size of queue.

int nextUp[ROWS_SELLERS]; // index for next customer to be served.
int HML[COLS_SELLERS]; // # of customers who got seats from H (0), M (1), or L (2).

int seatsTaken; // Number of seats taken.
int leaveCount; // Number of customers who left.

int sellerSetupCount; // Number of sellers who have set up; to keep track of the last seller.
char sellers[ROWS_SELLERS][COLS_SELLERS]; // The names of the sellers.

static const int sold[COLS_SELLERS] = { 0, 0, 0 };

pthread_mutex_t seatsMutex;  // mutex protects seats
pthread_mutex_t sellerMutex; // mutex protects seller count
pthread_mutex_t leaveMutex; // mutex protects number of customers who left
pthread_mutex_t queueMutex; // mutex protects customers adding themselves to queue
pthread_mutex_t printMutex;  // mutex proects printing events

int timesUp = 0;  // 1 = sales are over.
struct itimerval saleTimer;
time_t startTime;

int firstPrint = 1;

// Set up the 2d array that holds Customer names in each index.
char*** setupSeats(int rowSize, int columnSize) {
    char* name = (char*)malloc((CUST_NAME_LENGTH)* sizeof(char));
    char*** seats = (char***)malloc(rowSize * sizeof(char**)* sizeof(name));
    int i, j;
    for (i = 0; i < rowSize; i++) {
        seats[i] = (char**)malloc(columnSize * sizeof(name)* sizeof(char*));
    }
    for (i = 0; i < rowSize; i++) {
        for (j = 0; j < columnSize; j++) {
            seats[i][j] = "";
        }
    }
    seatsTaken = 0;
    return seats;
}

int freeSeats(char*** seats) {
    int i;
    for (i = 0; i < ROWS; i++) {
        free(seats[i]);
    }
    free(seats);
    return 0;
}

void setupSellers() {
    int i = 0, counter;

    if (i == 0) {
        strcpy(sellers[i], "H");
        i++;
    }
    for (counter = 0; counter < M_SELLERS; i++, counter++) {
        sprintf(sellers[i], "M%d", counter + 1);
    }
    for (counter = 0; counter < L_SELLERS; i++, counter++) {
        sprintf(sellers[i], "L%d", counter + 1);
    }
    memcpy(HML, sold, sizeof(HML)); // Initialize HML;

    // Initialize queue sizes and next up.
    for (counter = 0; counter < ROWS_SELLERS; counter++) {
        queueSize[counter] = 0;
        nextUp[counter] = 0;
    }

    leaveCount = 0; // Initialize leaveCount;
}

// Print a line for each event:
//   elapsed time
//   what event occurred
void print(char *event)
{
    time_t now;
    time(&now);
    double elapsed = difftime(now, startTime);
    int hour = 0;
    int min = (int)elapsed;

    if (min >= 60) {
        hour++;
        min -= 60;
    }

    // Acquire the mutex lock to protect the printing.
    pthread_mutex_lock(&printMutex);

    if (firstPrint) {
        printf("TIME | EVENT\n");
        firstPrint = 0;
    }

    // Elapsed time.
    printf("%1d:%02d | ", hour, min);

    // What event occurred.
    printf("%s\n", event);

    // Release the mutex lock.
    pthread_mutex_unlock(&printMutex);
}

// The customer thread passed with id of seller.
void *customer(void *param) {
    time_t start, now;
    int id = *((int *)param);
    char event[80];

    sleep(rand() % SALES_HOUR_DURATION);

    char* name = joinQueue(id);

    time(&start);
    time(&now);

    int queueNum = ((name[2] - '0') * 10) + (name[3] - '0') - 1; // Customer's index of queue so customer can check if he/she has been seated or not.
    do {
        time(&now); // waiting in queue
    } while (difftime(now, start) < 10.0 && !timesUp);

    pthread_mutex_lock(&queueMutex); // Lock before seller can access
    int outcome = atoi(queues[id][queueNum]); // Loop is broken either from waiting longer than 10 minutes or time is up.

    if (!outcome) { // Customer has not been seated because the wait has been 10 minutes or time is up.
        pthread_mutex_lock(&leaveMutex);
        if (difftime(now, start) >= 10.0) { // Customer waited 10 minutes or longer.
            sprintf(event, "%s left because 10+ minutes elapsed while waiting.", name);
            print(event);
            queues[id][queueNum] = "1";
        } else if (timesUp) {
            sprintf(event, "%s left because ticket sales are over.", name);
            print(event);
            queues[id][queueNum] = "1";
        }
        leaveCount++;
        pthread_mutex_unlock(&leaveMutex);
    }
    pthread_mutex_unlock(&queueMutex);

    return NULL;
}

// Join the seller's queue. Return the name of the customer.
char* joinQueue(int sellerIndex) {
    char* name = (char*)malloc(CUST_NAME_LENGTH * sizeof(char));
    char event[80];

    pthread_mutex_lock(&queueMutex); // Lock here so multiple customers do not receive the same name.
    int size = queueSize[sellerIndex];
    int number = size + 1;
    if (sellerIndex == 0) {
        if (number < COLS) {
            sprintf(name, "%s00%d", sellers[sellerIndex], number);
        } else {
            sprintf(name, "%s0%d", sellers[sellerIndex], number);
        }
    } else {
        if (number < COLS) {
            sprintf(name, "%s0%d", sellers[sellerIndex], number);
        } else {
            sprintf(name, "%s%d", sellers[sellerIndex], number);
        }
    }

    queues[sellerIndex][size] = name; // Add customer's name to queue.
    sprintf(event, "%s has been added to %s's queue.", queues[sellerIndex][size], sellers[sellerIndex]);
    print(event);
    queueSize[sellerIndex]++; // Increment queue size for seller.

    pthread_mutex_unlock(&queueMutex);

    return name;
}

void *seller(void *param) {
    int sellerId = *((int *)param);

    pthread_mutex_lock(&sellerMutex); // When last seller sets up, start time.
    sellerSetupCount++;
    if (sellerSetupCount == ROWS_SELLERS) {
        time(&startTime);
        saleTimer.it_value.tv_sec = SALES_HOUR_DURATION;
        setitimer(ITIMER_REAL, &saleTimer, NULL);
    }
    pthread_mutex_unlock(&sellerMutex);

    // Sell tickets until hour is over.
    do {
        sellTicket(sellerId); // pass name so we know who is selling the ticket
    } while (!timesUp);

    return NULL;
}

// Seller sells ticket.
void sellTicket(int sellerId) {
    pthread_mutex_lock(&queueMutex); // Lock here because seller is dealing with customer, so customer will not leave.
    
    int index = nextUp[sellerId]; // Get index of customer who is next in line.
    char* customer = queues[sellerId][index];
    char seller = customer[0];
    char event[80];

    if (strcmp(customer, "") != 0) { // Do something if there is a customer in the queue.
        if (strcmp(customer, "1") != 0) { // If the string is not 1, then the customer has not left already.

            pthread_mutex_lock(&seatsMutex);

            if (seatsTaken < (ROWS * COLS)) { // There are seats open.
                if (seller == 'H') { // Simulate ticket sale length.
                    sleep(randomRange(1, 2));
                } else if (seller == 'M') {
                    sleep(randomRange(2, 4));
                } else {
                    sleep(randomRange(4, 7));
                }
                
                seatCustomer(customer);

                sprintf(event, "%s bought a ticket and has been seated.", customer);
                print(event);

                seatsTaken++;
                if (seller == 'H') { // Seated by H.
                    HML[0]++;
                } else if (seller == 'M') { // Seated by M.
                    HML[1]++;
                } else { // Seated by L.
                    HML[2]++;
                }

                queues[sellerId][index] = "1";
            } else {
                pthread_mutex_lock(&leaveMutex);
                leaveCount++;

                sprintf(event, "Seats are filled. %s left.", customer);
                print(event);
                pthread_mutex_unlock(&leaveMutex);

                queues[sellerId][index] = "1";
            }

            pthread_mutex_unlock(&seatsMutex);
        }
        nextUp[sellerId]++; // Increment when customer has already been processed.
    }

    pthread_mutex_unlock(&queueMutex);
}

void seatCustomer(char* customer) {
    char seller = customer[0];
    if (seller == 'H') { // Seated by H.
    } else if (seller == 'M') { // Seated by M.
    } else { // Seated by L.
    }
}

// Return a random number between min (inclusive) and max (inclusive).
int randomRange(int min, int max) {
    return (rand() % (max + 1 - min)) + min;
}

// Timer signal handler.
void timerHandler(int signal)
{
    timesUp = 1;  // Ticket sellers don't take anymore customers
}

int main(int argc, char *argv[]) {
    int i, j;
    int N = atoi(argv[1]); // Number of customer threads to make at a time.
    int indices[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }; // Hold the indices because unsure of passing pointer to variable i within for loop

    seats = setupSeats(ROWS, COLS); // Initialize seats
    setupSellers(); // Initialize array that holds names of sellers, initialize leave count, initialize sell count per seller for each seller, queue sizes, next up
    queues = setupSeats(ROWS, N + 1); // Initialize queues.

    // Initialize mutexes
    pthread_mutex_init(&seatsMutex, NULL);
    pthread_mutex_init(&printMutex, NULL);
    pthread_mutex_init(&sellerMutex, NULL);
    pthread_mutex_init(&leaveMutex, NULL);
    pthread_mutex_init(&queueMutex, NULL);

    srand(time(0));

    pthread_t sellerThreadId[ROWS_SELLERS];
    printf("Creating seller threads.\n");
    // Create the seller threads.
    for (i = 0; i < ROWS_SELLERS; i++) {
        pthread_attr_t sellerAttr;
        pthread_attr_init(&sellerAttr);
        pthread_create(&sellerThreadId[i], &sellerAttr, seller, &indices[i]);
    }

    pthread_t customerThreadIds[ROWS_SELLERS][N];
    printf("Creating customer threads.\n");
    // Create customer threads while time is still running.
    for (i = 0; i < ROWS_SELLERS; i++) {
        for (j = 0; j < N; j++) {
            pthread_attr_t customerAttr;
            pthread_attr_init(&customerAttr);
            pthread_create(&customerThreadIds[i][j], &customerAttr, customer, &indices[i]);
        }
    }

    // Call timeHandler when alarm goes off
    signal(SIGALRM, timerHandler);

    // Wait for the hour to be over.
    for (i = 0; i < ROWS_SELLERS; i++) {
        pthread_join(sellerThreadId[i], NULL);
        for (j = 0; j < N; j++) {
            pthread_join(customerThreadIds[i][j], NULL);
        }
    }

    freeSeats(seats); // Free memory after program is done running
    freeSeats(queues);
    return 0;
}
