#include "stdsocket.h"
#include <string.h>
#define THREAD_PARAM_SIZE 4
void* thread_function(void* arg) {
    //CONVERSION OF ARGUMENTS
    void** THREAD_PARAMS = arg;
    int id =*(int*) THREAD_PARAMS[0];
    stdint* start =(stdint*) THREAD_PARAMS[1];
    stdint* range =(stdint*) THREAD_PARAMS[2];
    int client =*(int*) THREAD_PARAMS[3];
    printf("Thread %d ONLINE\n",id);
    printf("Start: ");
    print_stdint(start);
    printf("Range: ");
    print_stdint(range);
    sendToServer(client,createOrder(THREADONLINE,id));
    sendThreadDescription(client,id,"Thread is starting");
    sendToServer(client,createOrder(ENDOFTHREAD,id));
    return NULL;
}
char* createOrder(char type,int thread){
    stdint* i = intToStd(thread);
    char* sequence = convertToChar(i);
    char* sequence2 = malloc((strlen(sequence)+2 )*sizeof(char));
    for(int i =0;i<(int)strlen(sequence);i++){
        sequence2[i+1] = sequence[i];
    }
    sequence2[0] = type;
    sequence2[strlen(sequence)+1] = '\n';
    sequence2[strlen(sequence)+2] = '\0';
    del(i);
    free(sequence);
    return sequence2;
}

void set(void** a, int i, void* value){
    a[i] = value;
}
stdint*** getThreadsOrder(int num_threads,stdint* range_begin,stdint* range_end){
    stdint* range_width = substraction(range_end,range_begin);
    stdint** THREADS_RANGE = malloc(sizeof(stdint*)*num_threads);
    stdint** START_THREADS = malloc(sizeof(stdint*)*num_threads);
    stdint* num_thr = intToStd(num_threads);
    stdint* scalar = division(range_width,num_thr);
    for(int i=0;i<num_threads;i++){
        stdint* i_std = intToStd(i);
        stdint* calc = multiplication(scalar,i_std);
        START_THREADS[i] = addition(range_begin,calc);
        del(calc);
        del(i_std);
    }
    del(num_thr);
    THREADS_RANGE[num_threads-1] = substraction(range_end, START_THREADS[num_threads-1]);
    print_stdint(THREADS_RANGE[num_threads-1]);
    for(int i = 0 ;i<num_threads-1;i++){
        THREADS_RANGE[i] = substraction(START_THREADS[i+1],START_THREADS[i]);
    }
    stdint*** orders=malloc(2*sizeof(stdint**));
    orders[1] = THREADS_RANGE;
    orders[0] = START_THREADS;
    del(range_width);
    del(scalar);
    
    return orders;
}
void createThreads(int num_threads,pthread_t* threads_id,stdint*** orders, int client){
    stdint** START_THREADS = orders[0];
    stdint** THREADS_RANGE = orders[1];
    // Create threads
    void** THREADS_PARAMS = malloc(num_threads*sizeof(int*));
    for(int i = 0; i < num_threads; i++) {
        void** THREAD_PARAMS = malloc(THREAD_PARAM_SIZE*sizeof(void*));
        THREADS_PARAMS[i] = THREAD_PARAMS;
        int id = i;
        set(THREAD_PARAMS,0,&id);
        set(THREAD_PARAMS,1,START_THREADS[i]);
        set(THREAD_PARAMS,2,THREADS_RANGE[i]);
        set(THREAD_PARAMS,3,&client);
        printf("Creating thread %d\n", i);
        printf("Thread id: %d\n",id);
        printf("Thread start: ");
        print_stdint(START_THREADS[i]);
        printf("Thread range: ");
        print_stdint(THREADS_RANGE[i]);
        pthread_create(threads_id+i, NULL, thread_function, THREADS_PARAMS[i]);
    }
    
}
void waitThreads(int num_threads,pthread_t* threads_id){
    // Wait for threads to finish
    for (int i = 0; i < num_threads; i++) {
        pthread_join(threads_id[i], NULL);
    }
}
struct sockaddr_in* getSocket(int port){
    struct sockaddr_in* server = malloc(sizeof(struct sockaddr_in));
    memset(server, 0, sizeof(struct sockaddr_in));
    server->sin_family = AF_INET;
    server->sin_port = htons(port);
    server->sin_addr.s_addr = INADDR_ANY;
    return server;
}
void err(int errn){
    switch(errn){
        case 1:
            printf("Allocation of the socket has failed\n");
            exit(EXIT_FAILURE);
            return;
        case 2:
            printf("Bind to the port has failed\n");
            exit(EXIT_FAILURE);
            return;
        case 3:
            printf("Unable to listen or an error has occured while listening\n");
            return;
        case 4:
            printf("Server connexion failed\n");
            return;
        case 5:
            printf("Sending information to server failed\n");
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
        exit(EXIT_FAILURE);
    }
    return server;
}

int connectServer(int server, struct sockaddr_in *addr){
    socklen_t addrlen = sizeof(*addr);
    int client = connect(server,(struct sockaddr*)addr,addrlen);
    if(client==-1){
        err(4);
    }
    return client;
}

void sendToServer(int server_fd, char* message){
    char* encoded_input = message;
    printf("Sending: %s",encoded_input);
    int sent = send(server_fd, encoded_input, strlen(encoded_input), 0);
    if (sent == -1) {
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
void finished(int client){
    sendToServer(client,createOrder(ENDOFPROCESS,-1));
}
void converseProtocol(int server_fd,char type, int info){
    sendToServer(server_fd,createOrder(type,info));
}
void agentThreadInfo(int client,int threads){
    sendToServer(client,createOrder(AGENTTHREAD,threads));
}
void sendDoubleInfoMessage(int client,char protocol, int thread, char* des){
    char* t_seq = convertToChar(intToStd(thread));
    const int size = (strlen(t_seq)+strlen(des)+4);
    char* message = malloc(size*sizeof(char));
    message[0] = protocol;
    for(size_t i = 0; i<strlen(t_seq);i++){
        message[i+1] = t_seq[i];
    }
    message[strlen(t_seq)+1] = ';';
    for(size_t i = 0; i<strlen(des);i++){
        message[i+2+strlen(t_seq)] = des[i];
    }
    message[size-2] = '\n';
    message[size-1] = '\0';
    sendToServer(client,message);
    free(message);
}
void sendThreadDescription(int client, int thread, char* description){
    sendDoubleInfoMessage(client,THREADDESCRIPTION,thread,description);
}
void sendAgentOnlineAndSecurity(int client,char agentID,char* key){
    sendDoubleInfoMessage(client,AGENTONLINE,(int)agentID,key);
}