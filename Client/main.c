#include "stdsocket.h"
#include "coreLib.h"
#define PARAMS 6
void freeOrders(stdint*** orders,int num_threads){
    //FIGURE OUT HOW TO FREE THIS MESS
    stdint** first = orders[0];
    stdint** last = orders[1];
    for(int i = 0; i<num_threads;i++){
        del(first[i]);
        del(last[i]);
    }
    free(first);
    free(last);
    free(orders);
}
int main(int argc, char **argv){
    if(argc!=PARAMS){
        printf("Wrong number of parameters, given: %d wanted: %d\n", argc,PARAMS);
        return 0;
    }
    char agentID = '@';
    // Get the range start and end from command line arguments
    int num_threads = getNumCores()*2-3;
    if(num_threads<0){
        num_threads=1;
    }
    printf("Found threads: %d\n",num_threads);
    stdint* range_begin = stdAtoi(argv[1]);
    stdint* range_end = stdAtoi(argv[2]);
    int comm_buff_size = atoi(argv[5]);
    // Check if the range is valid
    if(!isGreater(range_end,range_begin)){
        printf("Invalid range\n");
        return 0;
    }
    int port = atoi(argv[3]);
    // Create the socket
    printf("Socket init\n");
    printf("Port: %d\n",port);
    struct sockaddr_in* server_adress = getSocket(port);
    int server_fd= allocateSocket(server_adress);
    printf("Socket allocated\n");
    printf("Server communication buffer\n");
    init_sender(comm_buff_size,server_fd);
    printf("Connecting...\n");
    int connexion =-1;
    while(connexion==-1){
        printf("Trying to connect to server\n");
        connexion = connectServer(server_fd,(struct sockaddr_in*)server_adress);
        if(connexion==-1){
            printf("Connection failed. Next try in 2s\n");
            sleep(2);
        }
    }
    printf("Client connected to socket\n");
    printf("\e[0;32mWaiting for server clearance\n");
    //We now need to wait for the response of the server
    char* buffer[1024];
    int response = recv(server_fd,buffer,1023 ,0);
    buffer[response]='\0';
    printf("%s\n",buffer);
    //AGENT INFO SYNC
    sleep(2);
    printf("Agent online\n");
    printf("Agent ID: %c\n",agentID);
    sendAgentOnlineAndSecurity(server_fd,agentID,argv[4]);
    agentThreadInfo(server_fd,num_threads);
    printf("Agent info sent\n");
    //THREADS ALLOCATION
    sleep(1);
    printf("Creating threads\n");
    pthread_t* threads_id=malloc(num_threads * sizeof(pthread_t));
    stdint*** orders = getThreadsOrder(num_threads,range_begin,range_end);
    createThreads(num_threads,threads_id,orders,&server_fd);
    printf("Threads created\n");
    sleep(5);
    waitThreads(num_threads,threads_id);
    printf("Threads finished\n");
    printf("Flushing data\n");
    flush();
    //cancelThreads(num_threads,threads_id);
    //FREE MEMORY
    sleep(1);
    sendToServer(server_fd,createOrder(ENDOFPROCESS,0));
    freeOrders(orders,num_threads);
    free(server_adress);
    free(threads_id);
    destroy_sender();
    //CLOSE CONNEXION
    shutdown(server_fd,SHUT_RDWR);
    return 0;
}