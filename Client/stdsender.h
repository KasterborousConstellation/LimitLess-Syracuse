#ifndef STDSENDER
#define STDSENDE
#include "stdsocket.h"
#include "stdint.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
typedef struct Sdata {stdint* index; stdint* result;} Sdata;
typedef struct Sbuffer {char* buffer; int buff_max_size; int current_size;pthread_mutex_t buffer_lock;} Sbuffer;
void init_sender(int buffer_size, int server_fd);
void append(Sdata data);
void destroy_sender();
void flush();
#endif