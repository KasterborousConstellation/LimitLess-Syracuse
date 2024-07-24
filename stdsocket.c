#include "stdsocket.h"
#define THREAD_PARAM_SIZE 4
void* thread_function(void* arg) {
    int* THREAD_PARAMS = arg;
    int id = THREAD_PARAMS[0];
    int start = THREAD_PARAMS[1];
    int range = THREAD_PARAMS[2];
    int client = THREAD_PARAMS[3];
    printf("Thread %d ONLINE : start %d range %d\n",id,start,range);
    sendToClient(client,"T");
    return NULL;
}
void set(int* a, int i, int value){
    a[i] = value;
}
int** getThreadsOrder(int num_threads,int range_begin,int range_end){
    int range_width = range_end-range_begin;
    int* THREADS_RANGE = malloc(sizeof(int)*num_threads);
    int* START_THREADS = malloc(sizeof(int)*num_threads);
    for(int i=0;i<num_threads;i++){
        START_THREADS[i] = range_begin + i* range_width/num_threads;
    }
    THREADS_RANGE[num_threads-1] =range_end- START_THREADS[num_threads-1];
    for(int i = 0 ;i<num_threads-1;i++){
        THREADS_RANGE[i] = START_THREADS[i+1]-START_THREADS[i];
    }
    int** orders=malloc(2*sizeof(int*));
    orders[1] = THREADS_RANGE;
    orders[0] = START_THREADS;
    return orders;
}
void createThreads(int num_threads,pthread_t* threads_id,int** orders, int client){
    int* START_THREADS = orders[0];
    int* THREADS_RANGE = orders[1];
    // Create threads
    int** THREADS_PARAMS = malloc(num_threads*sizeof(int*));
    for(int i = 0; i < num_threads; i++) {
        int* THREAD_PARAMS = malloc(THREAD_PARAM_SIZE*sizeof(int));
        THREADS_PARAMS[i] = THREAD_PARAMS;
        set(THREAD_PARAMS,0,i);
        set(THREAD_PARAMS,1,START_THREADS[i]);
        set(THREAD_PARAMS,2,THREADS_RANGE[i]);
        set(THREAD_PARAMS,3,client);
        printf("Creating thread %d\n", i);
        printf("Thread %d start %d range %d\n",THREAD_PARAMS[0],THREAD_PARAMS[1],THREAD_PARAMS[2]);
        pthread_create(threads_id+i, NULL, thread_function, THREADS_PARAMS[i]);
    }
    
}
void waitThreads(int num_threads,pthread_t* threads_id){
    // Wait for threads to finish
    for (int i = 0; i < num_threads; i++) {
        pthread_join(threads_id[i], NULL);
    }
}
struct sockaddr_in* createSocket(int port){
    struct sockaddr_in* server = malloc(sizeof(struct sockaddr_in));
    server->sin_family = AF_INET;
    server->sin_port = htons(port);
    server->sin_addr.s_addr = INADDR_ANY;
    return server;
}
void err(int errn){
    switch(errn){
        case 1:
            printf("Allocation of the socket has failed\n");
            return;
        case 2:
            printf("Bind to the port has failed\n");
            return;
        case 3:
            printf("Unable to listen or an error has occured while listening\n");
            return;
        case 4:
            printf("Client connexion failed\n");
            return;
        case 5:
            printf("Sending information to client failed\n");
            return;
        default:
            printf("An unknown error has occured\n");
            return;
    }
    exit(EXIT_FAILURE);
}
int allocateSocket(){
    int server = socket(AF_INET, SOCK_STREAM, 0);
    if(server < 0){
        err(1);
    }
    return server;
}
void bindSocket(int socket_id, struct sockaddr_in* address) {
    int bind_result = bind(socket_id, (struct sockaddr*)address, sizeof(struct sockaddr_in));
    if (bind_result == -1) {
        err(2);
    }
    return;
}
void startListen(int server){
    int back = listen(server,1);
    if(back==-1){
        err(3);
    }
    return;
}
int accept_connexion(int server, struct sockaddr *addr){
    socklen_t addrlen = sizeof(addr);
    int client = accept(server,addr,&addrlen);
    if(client==-1){
        err(4);
    }
    return client;
}
void sendToClient(int client, char* message){
    int sent = send(client,message,strlen(message),0);
    if(sent==-1){
        err(5);
    }
    return;
}
void cancelThread(int id){
    pthread_cancel(id);
}
void cancelThreads(int num_threads,pthread_t* threads_id){
    for(int i = 0; i < num_threads; i++) {
        cancelThread(threads_id[i]);
    }
}