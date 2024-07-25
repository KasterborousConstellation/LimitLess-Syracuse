#include "stdsocket.h"
#define PARAMS 5
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
    // Get the range start and end from command line arguments
    int num_threads = atoi(argv[1]);
    int range_begin = atoi(argv[2]);
    int range_end = atoi(argv[3]);
    // Check if the range is valid
    if(range_begin < 0 || range_end < 0 || range_begin > range_end){
        printf("Invalid range\n");
        return 0;
    }
    int port = atoi(argv[4]);
    // Create the socket
    printf("Creating socket\n");
    printf("Port: %d\n",port);
    struct sockaddr_in* server_adress = createSocket(port);
    int server;
    int client;
    // Allocate the socket
    printf("Allocating socket\n");
    server = allocateSocket();
    // Bind the socket
    printf("Binding socket\n");
    bindSocket(server,server_adress);
    // Start listening
    printf("Starting to listen\n");
    startListen(server);
    printf("Waiting for client\n");
    client = accept_connexion(server, (struct sockaddr *)server_adress);
    printf("Client connected\n");
    //THREADS ALLOCATION
    sleep(1);
    printf("Creating threads\n");
    pthread_t* threads_id=malloc(num_threads*sizeof(pthread_t));
    int** orders = getThreadsOrder(num_threads,range_begin,range_end);
    createThreads(num_threads,threads_id,orders,client);
    printf("Threads created\n");
    waitThreads(num_threads,threads_id);
    printf("Threads finished\n");
    finished(client);
    //cancelThreads(num_threads,threads_id);
    //FREE MEMORY
    freeOrders(orders);
    free(server_adress);
    free(threads_id);
    //CLOSE CONNEXION
    close(client);
    shutdown(server,2);
    return 0;
}
