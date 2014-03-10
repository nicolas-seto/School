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

#define SALES_HOUR_DURATION 60

char*** setupSeats(int rowSize, int columnSize);
int freeSeats(char*** seats);

char*** seats; // 2d array that holds customer names.
int seatsTaken; // Number of seats taken.
int leaveCount; // Number of customers who left.
int sellerSetupCount; // Number of sellers who have set up; to keep track of the last seller.
char sellers[ROWS_SELLERS][COLS_SELLERS]; // The names of the sellers.
int HML[COLS_SELLERS]; // # of customers who got seats from H (0), M (1), or L (2).

static const int sold[COLS_SELLERS] = { 0, 0, 0 };

pthread_mutex_t seatsMutex;  // mutex protects seats
pthread_mutex_t sellerMutex; // mutex protects seller count
pthread_mutex_t leaveMutex; // mutex protects number of customers who left
pthread_mutex_t printMutex;  // mutex proects printing events

int timesUp = 0;  // 1 = sales are over.
struct itimerval saleTimer;
time_t startTime;

// Set up the 2d array that holds Customer names in each index.
char*** setupSeats(int rowSize, int columnSize) {
    char* name = (char*)malloc((CUST_NAME_LENGTH + 1) * sizeof(char)); // End of string character.
    char*** seats = (char***)malloc(rowSize * sizeof(char**) * sizeof(name));
    int i, j;
    for (i = 0; i < rowSize; i++) {
        seats[i] = (char**)malloc(columnSize * sizeof(name) * sizeof(char*));
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
        sprintf(sellers[i], "M%d", counter+1);
    }
    for (counter = 0; counter < L_SELLERS; i++, counter++) {
        sprintf(sellers[i], "L%d", counter + 1);
    }
    memcpy(HML, sold, sizeof(HML)); // Initialize HML;
    leaveCount = 0; // Initialize leaveCount;
}

// The customer thread.
void *customer(void *param) {
    int id = *((int *)param);

    // Students will arrive at random times during the office hour.
    //sleep(rand() % OFFICE_HOUR_DURATION);
    //studentArrives(id);

    return NULL;
}

void *seller(void *param) {
    char *name = (char *) param;
    
    pthread_mutex_lock(&sellerMutex);
    sellerSetupCount++;
    if (sellerSetupCount == ROWS_SELLERS) {
        alarm() // use alarm instead of setitimer()
        time(&startTime); // 
        saleTimer.it_value.tv_sec = SALES_HOUR_DURATION;
        setitimer(ITIMER_REAL, &saleTimer, NULL);
    }
    pthread_mutex_unlock(&sellerMutex);
    

    // Sell tickets until hour is over.
    do {
        sellTickets();
    } while (!timesUp);

    return NULL;
}

// Timer signal handler.
void timerHandler(int signal)
{
    timesUp = 1;  // Ticket sellers don't take anymore customers
}

int main(int argc, char *argv[]) {
    int i;
    char*** seats = setupSeats(ROWS, COLS);
    setupSellers();

    // Initialize mutexes
    pthread_mutex_init(&seatsMutex, NULL);
    pthread_mutex_init(&printMutex, NULL);

    srand(time(0));

    pthread_t sellerThreadId[ROWS_SELLERS];
    // Create the seller threads.
    for (i = 0; i < ROWS_SELLERS; i++) {
        pthread_attr_t sellerAttr;
        pthread_attr_init(&sellerAttr);
        pthread_create(&sellerThreadId[i], &sellerAttr, seller, sellers[i]);
    }

    // Wait for the hour to be over.
    for (i = 0; i < ROWS_SELLERS; i++) {
        pthread_join(sellerThreadId[i], NULL);
    }

    freeSeats(seats); // Free memory after program is done running
    return 0;
}