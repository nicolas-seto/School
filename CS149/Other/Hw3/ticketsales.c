#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <signal.h>
#include <sys/time.h>

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

char*** seats;
char sellers[ROWS_SELLERS][COLS_SELLERS];
pthread_mutex_t seatsMutex;  // mutex protects seats
pthread_mutex_t printMutex;  // mutex proects printing events

// Set up the 2d array that holds Strings in each index.
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
        sellers[i] = "H";
        i++;
    }
    for (i, counter = 0; counter < M_SELLERS; i++, counter++) {
        sprintf(sellers[i], "M%d", counter+1);
    }
    for (i, counter = 0; counter < L_SELLERS; i++, counter++) {
        sprintf(sellers[i], "L%d", counter + 1);
    }
}

// The customer thread.
void *customer(void *param) {
    int id = *((int *)param);

    // Students will arrive at random times during the office hour.
    sleep(rand() % OFFICE_HOUR_DURATION);
    studentArrives(id);

    return NULL;
}

void *seller(void *param) {
    char name[3] = *((char *)param);
    
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
    for (i = 0; i < ROWS; i++) {
        printf("%s\n", sellers[i]);
    }
    
    //pthread_mutex_init(&seatsMutex, NULL);
    //pthread_mutex_init(&printMutex, NULL);

    //srand(time(0));
    
    
    freeSeats(seats); // Free memory after program is done running
    return 0;
}