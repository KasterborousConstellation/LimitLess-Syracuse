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
#define ENDOFPROCESS 'T'
#define ENDOFTHREAD 'E'
#define THREADONLINE 'I'
#define AGENTONLINE 'A'
#define AGENTTHREAD 'L'
struct sockaddr_in* getSocket(int port);
int allocateSocket();
void err(int errn);
void sendToServer(int client, char* message);
void createThreads(int num_threads,pthread_t* threads_id,int** orders,int client);
int** getThreadsOrder(int num_threads,int range_begin,int range_end);
void cancelThread(int id);
void cancelThreads(int num_threads,pthread_t* threads_id);
void waitThreads(int num_threads,pthread_t* threads_id);
int connectServer(int server, struct sockaddr_in *addr);
void finished(int client);
void agentOnline(int client,char agentID);
void agentThreadInfo(int client,int threads);
char* createOrder(char type, int data);
#endif