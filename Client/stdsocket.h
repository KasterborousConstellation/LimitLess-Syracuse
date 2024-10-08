#ifndef STDSOCKET
#define STDOCKET
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
#define ENDOFPROCESS 'T'
#define ENDOFTHREAD 'E'
#define THREADONLINE 'I'
#define AGENTONLINE 'A'
#define AGENTTHREAD 'L'
#define THREADDESCRIPTION 'D'
#define DATASENDING 'S'
typedef struct Sdata {stdint* index; stdint* result;} Sdata;
typedef struct Sbuffer {char* buffer; int buff_max_size; int current_size;pthread_mutex_t buffer_lock;} Sbuffer;
void init_sender(int buffer_size, int server_fd);
void append(Sdata data);
void destroy_sender();
void flush();
struct sockaddr_in* getSocket(int port);
int allocateSocket();
void err(int errn);
void sendToServer(int client, char* message);
void createThreads(int num_threads,pthread_t* threads_id,stdint*** orders, int* client);
stdint*** getThreadsOrder(int num_threads,stdint* range_begin,stdint* range_end);
void cancelThread(int id);
void cancelThreads(int num_threads,pthread_t* threads_id);
void waitThreads(int num_threads,pthread_t* threads_id);
int connectServer(int server, struct sockaddr_in *addr);
void finished(int client);
void agentOnline(int client,char agentID);
void agentThreadInfo(int client,int threads);
char* createOrder(char type, int data);
void sendThreadDescription(int client, int thread, char* description);
void sendAgentOnlineAndSecurity(int client,char agentID,char* key);
#endif