#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <unistd.h>
#include <sys/time.h>
#include "syracuselib.h"
#include <stdio.h>
#include <pthread.h>
#ifndef STDSOCKET
#define STDOCKET
struct sockaddr_in* createSocket(int port);
int allocateSocket();
void startListen(int server);
void err(int errn);
void bindSocket(int socket_id, struct sockaddr_in* address);
void sendToClient(int client, char* message);
void createThreads(int num_threads,pthread_t* threads_id,int** orders,int client);
int** getThreadsOrder(int num_threads,int range_begin,int range_end);
void cancelThread(int id);
void cancelThreads(int num_threads,pthread_t* threads_id);
void waitThreads(int num_threads,pthread_t* threads_id);
int accept_connexion(int server, struct sockaddr *addr);
#endif