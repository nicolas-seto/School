#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ioctl.h>

int pipes[5][2]; //5 pipes, 2 ends ea.
struct timeval start, end;

main()
{
	fd_set set;
	FD_ZERO(&set);
	gettimeofday(&start, NULL);
	//create children and their pipes.
	int id;
	for (id = 1; id <= 5; id++)
	{
		pipe(pipes[id]);
		FD_SET(pipes[id][0], &set);

		pid_t childpid = fork();

		if (childpid == 0)
			child(id);
	}
	//start timer.
	int seconds, useconds = seconds = 0;
	FILE *output = fopen("output.txt", "w");
	//constantly read pipes.
	while (seconds < 30.)
	{
		gettimeofday(&end, NULL);
		seconds = end.tv_sec - start.tv_sec;

		fd_set setcopy = set;
		if (select(FD_SETSIZE, &set, NULL, NULL, NULL) > 0)
			for (id = 0; id <= 5; id++)
				if (FD_ISSET(pipes[id][0], &set))
				{
					// Read in string.
					char pipecontents[256];
					char text[256];

					close(pipes[id][1]); // Close output side
					read(pipes[id][0], pipecontents, sizeof(pipecontents));

					gettimeofday(&end, NULL);
					seconds = end.tv_sec - start.tv_sec;
					useconds = end.tv_usec - start.tv_usec;
					float mtime = seconds + useconds / 1000000.0;
					sprintf(text, "%1d:%2.3f: %s", 0, mtime, pipecontents);

					fprintf(output, "%s\n", text);

				}
				set = setcopy;
	}
	fclose(output);
	exit(0);
}

/**
* child that sleeps 0, 1, or 2 secs.
* receives: id
*/
child(int id)
{
	//start timer.
	int seconds, useconds = seconds = 0;
	srand(end.tv_usec - start.tv_usec); //seed new randomizer

	int messagenum = 1;

	int randomnumber = rand() % 3;
	while (seconds < 30.)
	{
		if(id == 5){
			char input[256];
			char pipecontents[256];
			scanf ("%s", input);

			gettimeofday(&end, NULL);
			seconds = end.tv_sec - start.tv_sec;
			useconds = end.tv_usec - start.tv_usec;
			float mtime = seconds + useconds / 1000000.0;

			sprintf(pipecontents, "%1d:%2.3f: %s", 0, mtime, input);

			close(pipes[id][0]); // Close output side
			write(pipes[id][1], pipecontents, (strlen(pipecontents) + 1));

		}else{
			//sleep some time.
			sleep(randomnumber);

			//update time.
			gettimeofday(&end, NULL);
			seconds = end.tv_sec - start.tv_sec;
			useconds = end.tv_usec - start.tv_usec;
			float mtime = seconds + useconds / 1000000.0;

			//write message to pipes.
			char pipecontents[256];
			sprintf(pipecontents, "%1d:%2.3f: Child %d message %d\n", 0, mtime, id,
				messagenum++);
			while ((randomnumber = rand() % 3) == 0)
				sprintf(pipecontents + strlen(pipecontents),
				"%1d:%2.3f: Child %d message %d\n", 0,
				(end.tv_sec - start.tv_sec)
				+ (end.tv_usec - start.tv_usec) / 1000000., id,
				messagenum++);

			close(pipes[id][0]); // Close output side
			write(pipes[id][1], pipecontents, (strlen(pipecontents) + 1));
		}
	}

	exit(0);
}

/**
* child that asks user for input repeatedly til time up.
* receives: id
*/
interrogatingChild(int id)
{
	struct timeval start, end;
	gettimeofday(&start, NULL);
	int seconds, useconds = seconds = 0;
	srand(end.tv_usec - start.tv_usec); //seed new randomizer

	while (seconds < 30.)
	{
		//sleep some time.
		char input[256];
		char pipecontents[256];
		scanf ("%s", input);


		//update time.
		gettimeofday(&end, NULL);
		seconds = end.tv_sec - start.tv_sec;
		useconds = end.tv_usec - start.tv_usec;
		float mtime = seconds + useconds / 1000000.0;

		//write message to pipes.

		sprintf(pipecontents, "%1d:%2.3f: %s", 0, mtime, input);

		close(pipes[id][0]); // Close output side
		write(pipes[id][1], pipecontents, (strlen(pipecontents) + 1));
	}

	exit(0);
}