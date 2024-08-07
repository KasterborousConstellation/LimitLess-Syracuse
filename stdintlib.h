#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>
#ifndef STDINTLIB
#define STDINTLIB
/*
2^9223372036854775807 -1 theorical limit representation
Can be improved by using unsigned long long if i make it work
*/
//Define bit placement size
#define ordinal long long
#define format unsigned int
//Define long int structure
typedef struct decimal {char* data;ordinal size;ordinal capacity;} decimal;
typedef struct stdint {format* data;ordinal size;ordinal capacity;} stdint;
stdint* create(ordinal capacity); ////
ordinal getSize(stdint* i); ////
ordinal getCapacity(stdint* i); ////
bool getStdBit(stdint* a, ordinal n);/////
int getPartition(stdint* a,ordinal p); ////
void setPartition(stdint* a,ordinal p,int value);////
void resize(stdint* i,ordinal capacity); ////
void setStdBit(stdint* i, ordinal n,bool value);
void del(stdint* i);////
void print_stdint(stdint* a);////
bool getMSB(stdint* a); ////
stdint* copy(stdint* a);
stdint* stdint_from(unsigned int* array,ordinal size,ordinal capacity);////
bool isOdd(stdint* a);////
bool isEven(stdint* a);////
bool isZero(stdint* a);////
bool isOne(stdint* a);////
bool isEquals(stdint* a,stdint* b);////
//END OF STD SIMPLE FUNCTIONS
//START OF OPERATION
void leftShift(stdint* i); ////
void recursiveLeftShift(stdint* a,ordinal i); ////
void rightShift(stdint* i);////
void recursiveRightShift(stdint* a,ordinal i);////
stdint* addition(stdint* a, stdint* b); /////
void selfAddition(stdint* a, stdint* b); ////
stdint* multiplication(stdint* a, stdint* b); ////
void selfMultiplication(stdint* a, stdint* b); ////
//CAST FUNCTIONS
stdint* intToStd(unsigned int i); ///
stdint* ordinalToStd(ordinal i); ////
//Conversion
char* convertToChar(stdint* a); ////
void printDecimalStdInt(stdint* a); ////
#endif