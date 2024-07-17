#include "syracuselib.h"
#include <stdio.h>
#include <pthread.h>

void* thread_function(void* arg) {
    // Code to be executed by the thread goes here
    return NULL;
}

int main(int argc, char **argv) {
    // Get the range start and end from command line arguments
    int a = atoi(argv[1]);
    int z = atoi(argv[2]);

    // Calculate the number of threads based on the range
    int num_threads = z - a + 1;

    // Create an array to hold thread IDs
    pthread_t threads[num_threads];

    // Create threads
    for (int i = 0; i < num_threads; i++) {
        pthread_create(&threads[i], NULL, thread_function, NULL);
    }

    // Wait for threads to finish
    for (int i = 0; i < num_threads; i++) {
        pthread_join(threads[i], NULL);
    }

    return 0;
}