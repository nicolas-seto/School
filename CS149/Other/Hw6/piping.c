#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/wait.h>
#include <unistd.h>

#define NUMPIPES 5
#define NUMFILEDESC 2
#define STRLEN 512
#define MICRO 1000000.0
#define TIMER 30

static const char FILE_NAME[] = "output.txt";
static const char PIPE_ERROR[] = "ERROR: Piping failed.\n";
static const char FORK_ERROR[] = "ERROR: Forking failed.\n";
static const char CLOSEW_ERROR[] = "ERROR: Parent failed in closing write-end of pipe";
static const char CLOSER_ERROR[] = "ERROR: Child failed in closing read-end of pipe";
struct timeval start, stop;

int parent(int pipes[][NUMFILEDESC]);
int child(int id, int pipe[]);

int main(void) {
    int pipes[NUMPIPES][NUMFILEDESC];
    int i;
    
    /* Begin timer for both parent and children */
    gettimeofday(&start, NULL);
    
    for (i = 0; i < NUMPIPES; i++) {
        /* Create the pipes */
        if (pipe(pipes[i]) == -1) {
            fprintf(stderr, "%s", PIPE_ERROR);
            exit(1);
        }
        
        /* pid of the child */
        pid_t childpid;
        
        /* Begin forking */
        if ((childpid = fork()) == -1) {
            fprintf(stderr, "%s", FORK_ERROR);
            exit(1);
        } else {
            child(i + 1, pipes[i]);
        }
    }
    /* Have the parent process watch the pipes */
    parent(pipes);
    return 0;
}

int parent(int pipes[][NUMFILEDESC]) {
    char buffer[STRLEN], output[STRLEN];
    fd_set original, copy;
    FILE *op = fopen(FILE_NAME, "w");
    int i, nfds = pipes[NUMPIPES][0], sec, msec;
    
    FD_ZERO(&original);
    /* Watch read-ends and close write-ends */
    for (i = 0; i < NUMPIPES; i++) {
        FD_SET(pipes[i][0], &original);
        if (close(pipes[i][1]) == -1) {
            fprintf(stderr, "%s %d.\n", CLOSEW_ERROR, i);
            exit(1);
        }
    }
    
    copy = original;
    
    while (select(nfds, &original, NULL, NULL, NULL) > 0) {
        for (i = 0; i < NUMPIPES; i++) {
            if (FD_ISSET(pipes[i][0], &original)) {
                if (read(pipes[i][0], buffer, STRLEN) > 0) {
                    gettimeofday(&stop, NULL);
                    sec = stop.tv_sec - start.tv_sec;
                    msec = stop.tv_usec - start.tv_usec;
                    sprintf(output, "%1d:%2.3f: %s", 0, sec + (msec / MICRO), buffer);
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

int child(int id, int pipe[]) {
    char buffer[STRLEN], std_in[STRLEN];
    struct timeval child_start, child_stop;
    int sec, msec, message = 1, random;
    
    if (close(pipe[0]) == -1) {
        fprintf(stderr, "%s %d.\n", CLOSER_ERROR, id);
        exit(1);
    }
    
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
        } else { /* Child who reads stdin */
            scanf("%s", std_in);
            
            gettimeofday(&stop, NULL);
            sec = stop.tv_sec - start.tv_sec;
            msec = stop.tv_usec - start.tv_usec;
            
            sprintf(buffer, "%1d:%2.3f: %s\n", 0, 
                sec + (msec / MICRO), std_in);
        }
        
        write(pipe[1], buffer, STRLEN);
        gettimeofday(&child_stop, NULL);
    } while (child_stop.tv_sec - child_start.tv_sec < TIMER);
    
    exit(0);
}