#include "stdsocket.h"
#include "coreLib.h"
#define PARAMS 4
void freeOrders(int** orders){
    free(orders[0]);
    free(orders[1]);
    free(orders);
}
int main(int argc, char **argv) {
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
    int range_begin = atoi(argv[1]);
    int range_end = atoi(argv[2]);
    // Check if the range is valid
    if(range_begin < 0 || range_end < 0 || range_begin > range_end){
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
    agentOnline(server_fd,agentID);
    agentThreadInfo(server_fd,num_threads);
    printf("Agent info sent\n");
    //THREADS ALLOCATION
    sleep(1);
    printf("Creating threads\n");
    pthread_t* threads_id=malloc(num_threads*sizeof(pthread_t));
    int** orders = getThreadsOrder(num_threads,range_begin,range_end);
    createThreads(num_threads,threads_id,orders,server_fd);
    printf("Threads created\n");
    waitThreads(num_threads,threads_id);
    printf("Threads finished\n");
    //cancelThreads(num_threads,threads_id);
    //FREE MEMORY
    sendToServer(server_fd,createOrder(ENDOFPROCESS,0));
    freeOrders(orders);
    free(server_adress);
    free(threads_id);
    //CLOSE CONNEXION
    shutdown(server_fd,SHUT_RDWR);
    return 0;
}