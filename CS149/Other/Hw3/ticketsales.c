#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <signal.h>
#include <sys/time.h>

#define ROWS 10
#define COLS 10
#define CUST_NAME_LENGTH 4

#define SALES_HOUR_DURATION 60

char*** setupSeats(int rowSize, int columnSize);
int freeSeats(char*** seats);

char*** seats;

char*** setupSeats(int rowSize, int columnSize) {
    char* name = (char*)malloc(CUST_NAME_LENGTH * sizeof(char));
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

int main(int argc, char *argv[]) {
    char*** seats = setupSeats(ROWS, COLS);
    seats[0][0] = "H001";

    printf("%s\n", seats[0][0]);
    printf("%s\n", seats[0][1]);
    freeSeats(seats);

    return 0;
}