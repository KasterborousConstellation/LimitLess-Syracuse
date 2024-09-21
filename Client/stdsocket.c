#include "stdsocket.h"
#include <string.h>
#include <errno.h>
#define THREAD_PARAM_SIZE 4
int server_descriptor;
Sbuffer* gbuffer;
char* createOrder(char type,int thread){
    stdint* e = intToStd(thread);
    char* sequence = convertToChar(e);
    char* sequence2 = malloc((strlen(sequence)+3 )*sizeof(char));
    for(int i =0;i<(int)strlen(sequence);i++){
        sequence2[i+1] = sequence[i];
    }
    sequence2[0] = type;
    sequence2[strlen(sequence)+1] = '\n';
    sequence2[strlen(sequence)+2] = '\0';
    del(e);
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
            printf("Allocation of the socket has failed: %s\n", strerror(errno));
            exit(EXIT_FAILURE);
            return;
        case 2:
            printf("Bind to the port has failed: %s\n", strerror(errno));
            exit(EXIT_FAILURE);
            return;
        case 3:
            printf("Unable to listen or an error has occured while listening: %s\n", strerror(errno));
            return;
        case 4:
            printf("Server connexion failed: %s\n", strerror(errno));
            return;
        case 5:
            printf("Sending information to server failed: %s\n", strerror(errno));
            return;
        default:
            printf("An unknown error has occured: %s\n", strerror(errno));
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
void unsafeSendToServer(int server_fd, char* message,int len){
    char* encoded_input = message;
    //printf("Sending: %s",encoded_input);
    int sent = send(server_fd, encoded_input, len, 0);
    if (sent == -1) {
        err(5);

    }
    return;
}
void sendToServer(int server_fd, char* message){
    unsafeSendToServer(server_fd,message, strlen(message));
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

void clear_buffer(){
    gbuffer->current_size = 1;
    gbuffer->buffer[0] = DATASENDING;
    memset(gbuffer->buffer+1,0,gbuffer->buff_max_size);
}
void init_sender(int buffer_size, int server_fd){
    server_descriptor = server_fd;
    gbuffer = malloc(sizeof(Sbuffer));
    char* buff_data = (char*)malloc((buffer_size+2)*sizeof(char));
    gbuffer->buffer = buff_data;
    gbuffer->buff_max_size = buffer_size;
    pthread_mutex_init(&gbuffer->buffer_lock,NULL);
    clear_buffer();
}
void sendSdata(){
    if(gbuffer->current_size==1){
        return;
    }
    gbuffer->buffer[gbuffer->current_size] = '\n';
    gbuffer->buffer[gbuffer->current_size+1] = '\0';
    sendToServer(server_descriptor,gbuffer->buffer);
    clear_buffer();
}
void lock(){
    pthread_mutex_lock(&gbuffer->buffer_lock);
}
void unlock(){
    pthread_mutex_unlock(&gbuffer->buffer_lock);
}
void appendSTR(char* str){
    int str_length = strlen(str);
    lock();
    int current_size = gbuffer->current_size;
    int buff_max_size = gbuffer->buff_max_size;
    if(current_size+str_length>buff_max_size){
        sendSdata();
        for(int i =gbuffer->current_size;i<gbuffer->current_size+str_length;i++){
            gbuffer->buffer[i] = str[i-gbuffer->current_size];
        }
        gbuffer->current_size += str_length;
    }else{
        for(int i =current_size;i<current_size+str_length;i++){
            gbuffer->buffer[i] = str[i-current_size];
        }
        gbuffer->current_size += str_length;
    }
    unlock();
}
void append(Sdata data){
    char* index = convertToChar(data.index);
    char* result = convertToChar(data.result);
    int index_length = strlen(index);
    int result_length = strlen(result);
    int total_length = index_length+result_length+2;
    char* message = (char*)malloc(total_length * sizeof(char));
    for(int i = 0; i<index_length;i++){
        message[i] = index[i];
    }
    message[index_length] = ';';
    for(int i = 0; i<result_length;i++){
        message[i+index_length+1] = result[i];
    }
    message[total_length-1] = ';';
    appendSTR(message);
}
void destroy_sender(){
    pthread_mutex_destroy(&gbuffer->buffer_lock);
    free(gbuffer->buffer);
    free(gbuffer);
}
void flush(){
    sendSdata();
}
void* thread_function(void* arg) {
    //CONVERSION OF ARGUMENTS
    void** THREAD_PARAMS = arg;
    int id =*(int*) THREAD_PARAMS[0];
    stdint* start =(stdint*) THREAD_PARAMS[1];
    stdint* range =(stdint*) THREAD_PARAMS[2];
    int client =*(int*) THREAD_PARAMS[3];
    printf("Thread %d ONLINE\n",id);
    printf("Client: %d\n",client);
    printf("Start: ");
    print_stdint(start);
    printf("Range: ");
    print_stdint(range);
    sendToServer(client,createOrder(THREADONLINE,id));
    sendThreadDescription(client,id,"Thread is starting");
    stdint* calc = copy(start);
    stdint* upto = addition(start,range);
    stdint* one = intToStd(1);
    while(!isEquals(calc,upto)){
        stdint* result = syracuseFlightTime(calc);
        Sdata data = {calc,result};
        append(data);
        calc = addition(calc,one);
    }
    del(calc);
    del(upto);
    del(one);
    sendToServer(client,createOrder(ENDOFTHREAD,id));
    return NULL;
}
void createThreads(int num_threads,pthread_t* threads_id,stdint*** orders, int* client){
    stdint** START_THREADS = orders[0];
    stdint** THREADS_RANGE = orders[1];
    // Create threads
    int* ids = malloc(num_threads*sizeof(int));
    for(int i =0;i<num_threads;i++){
        ids[i] = i;
    }
    void** THREADS_PARAMS = malloc(num_threads*sizeof(int*));
    for(int i = 0; i < num_threads; i++) {
        void** THREAD_PARAMS = malloc(THREAD_PARAM_SIZE*sizeof(void*));
        THREADS_PARAMS[i] = THREAD_PARAMS;
        set(THREAD_PARAMS,0,ids+i);
        set(THREAD_PARAMS,1,START_THREADS[i]);
        set(THREAD_PARAMS,2,THREADS_RANGE[i]);
        set(THREAD_PARAMS,3,client);
        printf("Creating thread %d\n", i);
        printf("Thread id: %d\n",ids[i]);
        printf("Thread start: ");
        print_stdint(START_THREADS[i]);
        printf("Thread range: ");
        print_stdint(THREADS_RANGE[i]);
        pthread_create(threads_id+i, NULL, thread_function, THREADS_PARAMS[i]);
    }
}