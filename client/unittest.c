/*
 * unittest - Test the Client program
 */

#include <sys/types.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <sys/time.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <getopt.h>
#include <time.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int debug = 0;
int caching = 0;
int fetching = 0;
int recurse = 0;
int numservers = 10;
int baseport = 20000;

int fd[1000];
int numfds = 0;
char request[10];
char reply[20];

void parse_args(int argc, char **argv)
{
  int opt;

  while ((opt = getopt(argc, argv, "cdfn:p:r")) != -1) {
    switch (opt) {
      case 'c':
        caching = 1;
        break;
      case 'd':
        debug = 1;
        break;
      case 'f':
        fetching = 1;
        break;
      case 'n':
        numservers = atoi(optarg);
	if (numservers > 1000) {
	  fprintf(stderr, "ERROR: numservers must be < 1000.\n");
          exit(-1);
	}
        break;
      case 'p':
        baseport = atoi(optarg);
        break;
      case 'r':
        recurse = 1;
        break;
      default: /* '?' */
        fprintf(stderr, "Usage: %s\n"
	    " [-c] Return CACHE for the results\n"
	    " [-d] Enable extra debugging\n"
            " [-f] Return FETCH for the results\n"
            " [-n numservers] Number of servers in the network\n"
            " [-r] Return FORWARD every other request\n"
            " [-p baseport] First port used by servers\n", argv[0]);
        exit(-1);
    }
  }

  /* Make sure we are either caching or fetching */
  if (!caching && !fetching) {
    fprintf(stderr, "ERROR: Must use -c or -f\n");
    exit(-1);
  }
}

void open_sockets()
{
  int x, port, on = 1, rc;
  struct sockaddr_in sin;

  /* Open and listen on all sockets */
  for (x=0; x<numservers; x++) {
    port = baseport + x;
    if (debug) {
      printf("Binding %x to port %d\n", x, port);
    }

    /* Create the socket */
    if ((fd[x] = socket(PF_INET, SOCK_STREAM, 0)) == -1) {
      perror("ERROR: Creating socket");
      exit(-1);
    }

    /* Track numfds for select() */
    if (fd[x] >= numfds) {
      numfds = fd[x]+1;
    }

    /* Want to reuse addresses quickly */
    if (setsockopt(fd[x], SOL_SOCKET, SO_REUSEADDR, &on, sizeof(int)) == -1) {
      perror("setsockopt error (SO_REUSEADDR)");
      exit(-1);
    }

    /* Local bind */
    sin.sin_family = AF_INET;
    sin.sin_port = htons(port);
    sin.sin_addr.s_addr = INADDR_ANY;
    if (bind(fd[x], (struct sockaddr const *)&sin, sizeof(sin)) == -1) {
      perror("ERROR: bind failed");
      exit(-1);
    }

    /* Listen on the socket */
    if ((rc = listen(fd[x], 5)) == -1) {
      perror("ERROR: listen failed");
      exit(1);
    }
  }
  if (debug) {
    printf("numfds=%d\n", numfds);
  }
}

void handle_request(int x)
{
  int myfd, len, replen;
  char request[10];
  char reply[20];
  static int do_recurse = 0;

  /* Accept the connection */
  if ((myfd = accept(fd[x], NULL, NULL)) == -1) {
    perror("ERROR: accept");
    exit(-1);
  }

  /* Read the request */
  if (debug) {
    printf("Handling FD %d\n", x);
  }
  if ((len = read(myfd, &request, sizeof(reply))) <= 0) {
    perror("ERROR: read");
    exit(-1);
  }

  /* We don't actually parse it, just reply with something */
  if (recurse && do_recurse) {
    sprintf(reply, "FORWARD %ld", random() % numservers + baseport);
  } else if (caching) {
    strcpy(reply, "CACHE");
  } else if (fetching) {
    strcpy(reply, "FETCH");
  } else {
    fprintf(stderr, "Didn't know what to do!!!\n");
    exit(-1);
  }
  do_recurse = !do_recurse;
  if (debug) {
    printf("Got '%s' from %d, replying '%s'\n", request, x, reply);
  }

  /* Now send the reply */
  replen = strlen(reply) + 1;
  if ((len = write(myfd, &reply, replen)) != replen) {
    fprintf(stderr, "ERROR: write(len=%d) wrote %d!\n", replen, len);
    exit(-1);
  }

  /* And close the socket */
  if (close(myfd)) {
    perror("ERROR: close");
    exit(-1);
  }
}

void select_sockets()
{
  int x, rc;
  fd_set readfds;

  while(1)
  {
    /* Build the fdset for select() */
    FD_ZERO(&readfds);
    for (x=0; x<numservers; x++) {
      FD_SET(fd[x], &readfds);
    }

    /* Select() */
    if ((rc = select(numfds, &readfds, NULL, NULL, NULL)) == -1) {
      perror("ERROR: select");
      exit(-1);
    }

    /* Handle each ready fd */
    for (x=0; x<numservers; x++) {
      if (FD_ISSET(fd[x], &readfds)) {
        handle_request(x);
      }
    }
  }
}

int main(int argc, char **argv)
{
  /* Parse the command line args */
  parse_args(argc, argv);

  /* Initialize the random number generator */
  srandom(time(NULL));

  /* Open and listen on all sockets */
  open_sockets();

  /* Loop sending requests forever */
  select_sockets();

  /* This never actually returns */
  return 0;
}
