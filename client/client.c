/*
 * dsclient - Client program to test the Distributed System in TechJam 2014.
 */

#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <getopt.h>
#include <time.h>
#include <netinet/in.h>
#include <errno.h>
#include <netdb.h>

int debug=0;
int cachepercent = 90;
int cacherange = 1000;
int numservers = 10;
int baseport = 20000;
int milliseconds = 100;
int host = 0;

int port;
char request[11];
char reply[20];
int is_random;

long int stats_total = 0;
long int stats_random_forward = 0;
long int stats_random_fetch = 0;
long int stats_random_cache = 0;
long int stats_random_error = 0;
long int stats_number_forward = 0;
long int stats_number_fetch = 0;
long int stats_number_cache = 0;
long int stats_number_error = 0;

int parse_host(char *name)
{
  struct addrinfo *info;
  int ipaddr = 0;
  struct sockaddr_in *sin;

  /* Get the hostent */
  if ((getaddrinfo(name, NULL, NULL, &info)) == -1) {
    perror("ERROR: getaddrinfo\n");
    exit(-1);
  }
  sin = (struct sockaddr_in *) info->ai_addr;
  ipaddr = sin->sin_addr.s_addr;

  /* And return the saved IP address */
  if (debug) {
    printf("parse_host(%s) returns %08x\n", name, ipaddr);
  }
  return(ipaddr);
}

void parse_args(int argc, char **argv)
{
  int opt;

  host = htonl(INADDR_LOOPBACK);
  while ((opt = getopt(argc, argv, "c:dh:m:n:p:r:")) != -1) {
    switch (opt) {
      case 'c':
        cachepercent = atoi(optarg);
        break;
      case 'd':
        debug = 1;
        break;
      case 'h':
        host = parse_host(optarg);
        break;
      case 'm':
        milliseconds = atoi(optarg);
        break;
      case 'n':
        numservers = atoi(optarg);
        break;
      case 'p':
        baseport = atoi(optarg);
        break;
      case 'r':
        cacherange = atoi(optarg);
        if (cacherange > 2000000000) {
          fprintf(stderr, "ERROR: cacherange must be < 2M.\n");
          exit(-1);
        }
        break;
      default: /* '?' */
        fprintf(stderr, "Usage: %s\n"
          " [-c cachepercent] Percent of likely cached traffic to send (%d)\n"
          " [-d]              Enable extra debugging\n"
          " [-h hostname]     Hostname running servers\n"
          " [-m miliseconds]  How long to wait between requests.\n"
          " [-n numservers]   Total servers in your networks\n"
          " [-p baseport]     Base TCP port (default 20000)\n"
          " [-r cacherange]   Expected cached items (%d)\n", argv[0],
          cachepercent, cacherange);
        exit(-1);
    }
  }
}

/* Create a random request of 9 upper case characters */
void create_random()
{
  int x;

  for (x=0; x<9; x++)
    request[x] = random() % 26 + 'A';
  request[9] = '\n';
  request[10] = '\0';
  is_random = 1;
}

/* Create a numbered request */
void create_number()
{
  sprintf(request, "%09d\n", (int) (random() % cacherange));
  is_random = 0;
}

/* Send a request to the server */
int send_server()
{
  int fd, on = 1, len1, len2;
  struct sockaddr_in sin;

  if (debug) {
    printf("Sending '%s' to port %d\n", request, port);
  }

  /* Create the socket */
  if ((fd = socket(PF_INET, SOCK_STREAM, 0)) == -1) {
    perror("ERROR: Creating socket");
    exit(-1);
  }

  /* Want to reuse addresses quickly */
  if (setsockopt(fd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(int)) == -1) {
    perror("setsockopt error (SO_REUSEADDR)");
    exit(-1);
  }

  /* Local bind */
  sin.sin_family = AF_INET;
  sin.sin_port = htons(port);
  sin.sin_addr.s_addr = host;
  if (connect(fd, (struct sockaddr const *)&sin, sizeof(sin)) == -1) {
    if ((errno == ECONNREFUSED) || (errno == ETIMEDOUT)) {
      /* Record stats */
      if (is_random) {
        stats_random_error++;
      } else {
        stats_number_error++;
      }
      /* Close the socket and return failure */
      if (close(fd) == -1) {
        perror("ERROR: connect");
        exit(-1);
      }
      return(0);
    } else {
      perror("ERROR: connect");
      exit(-1);
    }
  }

  /* Write the request */
  len1 = strlen(request);
  if ((len2 = write(fd, &request, len1)) != len1) {
    fprintf(stderr, "ERROR: write '%s' len %d only wrote %d\n", request,
      len1, len2);
    exit(-1);
  }
  if (debug) {
    printf("Write %d bytes to remote\n", len2);
  }

  /* Read the reply */
  if ((len1=read(fd, &reply, sizeof(reply))) == -1) {
    perror("ERROR: read");
    exit(-1);
  }
  if (debug) {
    printf("Read %d bytes in reply\n", len1);
  }

  /* Close the socket */
  if (close(fd) == -1) {
    perror("ERROR: connect");
    exit(-1);
  }

  /* Succeeded */
  return(1);
}

/* This routine handles a reply by recording stats and maybe forwarding */
void handle_reply()
{
  if (debug) {
    printf("Request '%s' to port %d got reply '%s'\n", request, port, reply);
  }

  if (strncmp(reply, "FETCH", 5) == 0) {
    /* Request was fetched from the origin, done */
    if (is_random) {
      stats_random_fetch++;
    } else {
      stats_number_fetch++;
    }
  } else if (strncmp(reply, "CACHE", 5) == 0) {
    /* Request came from cache, done */
    if (is_random) {
      stats_random_cache++;
    } else {
      stats_number_cache++;
    }
  } else if (strncmp(reply, "FORWARD ", 8) == 0) {
    /* Request needs to be forwarded */
    if (is_random) {
      stats_random_forward++;
    } else {
      stats_number_forward++;
    }

    /* Now forward */
    port = atoi(&reply[8]);
    if (send_server()) {
      handle_reply();
    }
  } else {
    fprintf(stderr, "ERROR: Got unknown reply '%s'!\n", reply);
    exit(-1);
  }
}

int main(int argc, char **argv)
{
  long int oldtime, newtime;

  /* Parse the command line args */
  parse_args(argc, argv);

  /* Initialize the random number generator */
  oldtime = time(NULL);
  srandom(oldtime);

  /* Loop sending requests forever */
  printf("  Totals  Random   Random   Random  Random   "
    "Number   Number   Number  Number\n");
  printf("  Totals Forward    Fetch    Cache   Error   "
    "Forward   Fetch    Cache   Error\n");
  while(1)
  {
    /* Pick a random server (port) */
    port = random() % numservers + baseport;

    /* Pick a string to send */
    if (random() % 100 <= cachepercent)
    {
      create_number();
    } else {
      create_random();
    }

    /* Send the request and handle the reply */
    if (send_server()) {
      handle_reply();
    }
    stats_total++;

    /* Print stats every second */
    newtime = time(NULL);
    if (newtime > oldtime) {
        oldtime = newtime;
        printf("%08ld %08ld %08ld %08ld %07ld %08ld %08ld %08ld %07ld\n",
          stats_total, stats_random_forward, stats_random_fetch,
          stats_random_cache, stats_random_error, stats_number_forward,
          stats_number_fetch, stats_number_cache, stats_number_error);
    }

    /* Now maybe sleep for a bit */
    usleep(milliseconds*1000);
  }
}
