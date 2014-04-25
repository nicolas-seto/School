#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/wait.h>
#include <unistd.h>

#define NUMPIPES 5
#define NUMFILEDESC 2
#define STRLEN 256
#define MICRO 1000000.0
#define TIMER 30

static const char PIPE_ERROR[] = "ERROR: Piping failed.\n";
static const char FORK_ERROR[] = "ERROR: Forking failed.\n";
static const char CLOSEW_ERROR[] = "ERROR: Parent failed in closing write-end of pipe";
static const char CLOSER_ERROR[] = "ERROR: Child failed in closing read-end of pipe";
struct timeval start, stop;
int pipes[NUMPIPES][NUMFILEDESC];

int parent(void);
int child(int id);

int main(void) {
    int i;
    
    /* Begin timer for both parent and children */
    gettimeofday(&start, NULL);
    
    for (i = 0; i < NUMPIPES; i++) {
        /* Create the pipes */
        if (pipe(pipes[i]) == -1) {
            printf("%s", PIPE_ERROR);
            exit(1);
        }
        
        /* pid of the child */
        pid_t childpid;
        
        /* Begin forking */
        if ((childpid = fork()) == -1) {
            printf("%s", FORK_ERROR);
            exit(1);
        } else if (childpid == 0) {
            child(i + 1);
        }
    }
    /* Have the parent process watch the pipes */
    parent();
    return 0;
}

int parent(void) {
    char buffer[STRLEN], output[STRLEN];
    fd_set original, copy;
    FILE *op = fopen("output.txt", "w");
    int i, nfds = pipes[NUMPIPES][0], sec, msec;
    
    FD_ZERO(&original);
    /* Watch read-ends and close write-ends */
    for (i = 0; i < NUMPIPES; i++) {
        FD_SET(pipes[i][0], &original);
        if (close(pipes[i][1]) == -1) {
            printf("%s %d.\n", CLOSEW_ERROR, i);
            exit(1);
        }
    }
    
    copy = original;
    
    while (select(nfds, &original, NULL, NULL, NULL) > 0) {
        for (i = 0; i < NUMPIPES; i++) {
            if (FD_ISSET(pipes[i][0], &original)) {
                if (read(pipes[i][0], buffer, STRLEN) > 0) {
                    printf("%s", buffer);
                    gettimeofday(&stop, NULL);
                    sec = stop.tv_sec - start.tv_sec;
                    msec = stop.tv_usec - start.tv_usec;
                    sprintf(output, "%1d:%2.3f: %s", 0, sec + (msec / MICRO), buffer);
                    printf("%s", output);
                    fprintf(op, "%s", output);
                }
            }
        }
        
        if (waitpid(-1, NULL, WNOHANG) == -1) {
            fclose(op);
            return 0;
        }
        
        original = copy;
    }
    fclose(op);
    return 0;
}

int child(int id) {
    char buffer[STRLEN], std_in[STRLEN];
    struct timeval child_start, child_stop;
    int sec, msec, message = 1, random;
    
    if (close(pipes[id - 1][0]) == -1) {
        printf("%s %d.\n", CLOSER_ERROR, id);
        exit(1);
    }
    
    printf("Executing child %d\n", id);
    
    gettimeofday(&stop, NULL);
    srand(stop.tv_usec - start.tv_usec);
    
    gettimeofday(&child_start, NULL);
    gettimeofday(&child_stop, NULL);
    
    do {
        /* Children who sleep */
        if (id < NUMPIPES) {
            random = rand() % 3;
            sleep(random);
            
            gettimeofday(&stop, NULL);
            sec = stop.tv_sec - start.tv_sec;
            msec = stop.tv_usec - start.tv_usec;
            
            sprintf(buffer, "%1d:%2.3f: Child %d message %d\n", 0, 
                sec + (msec / MICRO), id, message++);
            printf("%s", buffer);
        } else { /* Child who reads stdin */
            scanf("%s", std_in);
            
            gettimeofday(&stop, NULL);
            sec = stop.tv_sec - start.tv_sec;
            msec = stop.tv_usec - start.tv_usec;
            
            sprintf(buffer, "%1d:%2.3f: %s\n", 0, 
                sec + (msec / MICRO), std_in);
        }
        
        write(pipes[id - 1][1], buffer, STRLEN);
        gettimeofday(&child_stop, NULL);
    } while (child_stop.tv_sec - child_start.tv_sec < TIMER);
    
    exit(0);
}