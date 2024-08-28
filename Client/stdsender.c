#include "stdsender.h"
int server_descriptor;
Sbuffer* gbuffer;
void clear_buffer(){
    gbuffer->current_size = 1;
    gbuffer->buffer[0] = DATASENDING;
}
void init_sender(int buffer_size, int server_fd){
    server_descriptor = server_fd;
    gbuffer = malloc(sizeof(Sbuffer));
    char* buff_data = (char*)malloc(buffer_size*sizeof(char));
    gbuffer->buffer = buff_data;
    gbuffer->buff_max_size = buffer_size;
    pthread_mutex_init(&gbuffer->buffer_lock,NULL);
    clear_buffer();
}
void send(){
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
        send();
        strcat(gbuffer->buffer,str);
        gbuffer->current_size += str_length;
    }else{
        strcat(gbuffer->buffer,str);
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
    strcat(message,index);
    strcat(message,';');
    strcat(message,result);
    strcat(message,';');
    appendSTR(message);
}
void destroy_sender(){
    pthread_attr_destroy(&gbuffer->buffer_lock);
    free(gbuffer->buffer);
    free(gbuffer);
}