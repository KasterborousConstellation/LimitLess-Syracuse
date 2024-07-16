#include "stdintlib.h"
#include "assert.h"
#define INT_SIZE 32
#define LONG_SIZE 64
#define LONGMAX 9223372036854775807
#define INTMAX 2147483647
stdint* stdint_from(int* array,ordinal size,ordinal capacity){
    stdint* a = create(capacity);
    a->size = size;
    for(ordinal e = 0; e<size ;e++){
        a->data[e] = array[e];
    }
    return a;
}
ordinal getSize(stdint* a){
    return a->size;
}
ordinal getCapacity(stdint* a){
    return a->capacity;
}
stdint* create(ordinal capacity){
    stdint* i = malloc(sizeof(stdint));
    int* data = malloc(capacity*sizeof(int));
    i->capacity = capacity;
    i->size=1;
    i->data = data;
    return i;
}
void setSize(stdint* i,ordinal size){
    if(size>getSize(i)){
        for(ordinal e = getSize(i);e<size;e++){
            i->data[e] =0;
        }
    }
    i->size = size;
}
void resize(stdint* i, ordinal size){
    if(size>=getCapacity(i)){
        int* data = malloc(2*size*sizeof(int));
        for(ordinal e=0;e<getCapacity(i);e++){
            data[e] = i->data[e];
        }
        i->capacity = 2*size;
        i->data = data;
    }else if(size<getCapacity(i)/4){
        int* data = malloc((getCapacity(i)/2)*sizeof(int));
        for(ordinal e=0;e<getCapacity(i)/2;e++){
            data[e] = i->data[e];
        }
        i->capacity = getCapacity(i)/2;
        i->data = data;
    }
    setSize(i,size);
}
int getPartition(stdint* a, ordinal p){
    return a->data[p];
}
void setPartition(stdint* a,ordinal p,int value){
    assert(p<getCapacity(a));
    a->data[p] =value;
}
void setStdBit(stdint* i, ordinal p,bool value){
    if(p>=getSize(i)*INT_SIZE){
        ordinal targetted_size = p/16;
        resize(i,targetted_size);
    }
    //We now need to know if the p bit is 1 or 0 
    //Case 1 if p bit is 1
    int part = getPartition(i,p/INT_SIZE);
    int rem = 1<<(p%INT_SIZE);
    if(getStdBit(i,p)==1){
        if(value==0){
            setPartition(i,p/INT_SIZE,part-rem);
        }
    }else{
        if(value==1){
            setPartition(i,p/INT_SIZE,part+rem);
        }
    }
}
bool getStdBit(stdint* a,ordinal p){
    if(p>getSize(a)*INT_SIZE){
        return 0;
    }
    ordinal place = p/INT_SIZE;
    int part = getPartition(a,place);
    short remainder = p%INT_SIZE;
    int extracted_int = part & (1<<remainder);
    return extracted_int>>remainder;
}
void print_stdint(stdint* a){
    printf("[");
    for(ordinal i = getSize(a)*INT_SIZE-1; i>=0 ;i--){
        if(i%INT_SIZE==(INT_SIZE-1)){
            printf("|");
        }
        char c;
        if(getStdBit(a,i)){
            c = '1';
        }else{
            c = '0';
        }
        printf("%c",c);
    }
    printf("]");
    printf("(%lld|%lld)\n",getSize(a),getCapacity(a));
}
void del(stdint* i){
    free(i->data);
    free(i);
}
bool getMSB(stdint* a){
    ordinal pos = getSize(a) * INT_SIZE - 1;
    return getStdBit(a,pos);
}
void leftShift(stdint* a){
    if(getMSB(a)==1){
        resize(a,getSize(a)+1);
    }
    for(ordinal i = getSize(a)*INT_SIZE-1;i>0;i--){
        setStdBit(a,i,getStdBit(a,i-1));
    }
    setStdBit(a,0,0);
}
void recursiveLeftShift(stdint* a,ordinal i){
    for(ordinal e = 0 ; e < i; e++){
        leftShift(a);
    }
}
void rightShift(stdint* a){
    for(ordinal i = 0;i<getSize(a)*INT_SIZE-1;i++){
        setStdBit(a,i,getStdBit(a,i+1));
    }
    setStdBit(a,getSize(a) * INT_SIZE - 1,0);
}
void recursiveRightShift(stdint* a,ordinal i){
    for(ordinal e = 0 ; e < i; e++){
        rightShift(a);
    }
}
stdint* copy(stdint* a){
    return stdint_from(a->data,getSize(a),getCapacity(a));
}
stdint* addition(stdint* a, stdint* b){
    a = copy(a);
    b = copy(b);
    //Exchange a for b if size of a < size of b
    if(getSize(a)<getSize(b)){
        stdint* tmp = a;
        a = b;
        b= tmp;
    }
    if(getMSB(a)==1){
        resize(a,getSize(a)+1);
    }
    stdint* new = copy(a);
    //Iterate for every bits of new 
    bool ret = 0;
    for(ordinal i = 0;i<getSize(new)*INT_SIZE-1;i++){
        bool bit_a = getStdBit(a,i);
        bool bit_b = getStdBit(b,i); 
        bool res = (bit_a + bit_b + ret) & 1; 
        ret = (bit_a + bit_b + ret)/2;
        setStdBit(new,i,res);
    }
    setStdBit(new,getSize(new)*INT_SIZE-1,ret);
    return new;
}
stdint* intToStd(int i){
    int* data = malloc(sizeof(int));
    data[0] = i;
    stdint* a = stdint_from(data,1,1);
    free(data);
    return a;
}
bool isOdd(stdint* a){
    return getStdBit(a,0);    
}
bool isEven(stdint* a){
    return !getStdBit(a,0);
}
bool isZero(stdint* a){
    for(ordinal i = 0;i<getSize(a)*INT_SIZE;i++){
        if(getStdBit(a,i)){
            return 0;
        }
    }
    return 1;
}
bool isOne(stdint* a){
    if(getStdBit(a,0)){
        for(ordinal i = 1;i<getSize(a)*INT_SIZE;i++){
            if(getStdBit(a,i)){
                return 0;
            }
        }
        return 1;
    }
    return 0;
}
bool isEquals(stdint* a,stdint* b){
    if (getSize(a) < getSize(b)) {
        stdint* tmp = a;
        a = b;
        b = tmp;
    }
    for(ordinal i = 0;i<getSize(a)*INT_SIZE;i++){
        if(getStdBit(a,i)!=getStdBit(b,i)){
            return false;
        }
    }
    return true;
}
stdint* BitReverse(stdint* a){
    stdint* res = copy(a);
    for(ordinal i = 0;i<getSize(res)*INT_SIZE;i++){
        setStdBit(res,i,!getStdBit(res,i));
    }
    return res;
}
void selfBitReverse(stdint* a){
    for(ordinal i = 0;i<getSize(a)*INT_SIZE;i++){
        setStdBit(a,i,!getStdBit(a,i));
    }
}
void selfAddition(stdint* n, stdint* to_add){
    stdint* a = n;
    stdint* b = to_add;
    if(getSize(a)<getSize(to_add)){
        stdint* tmp = a;
        a = b;
        b= tmp;
    }
    if(getMSB(a)==1){
        resize(a,getSize(a)+1);
    }
    //Iterate for every bits of new 
    bool ret = 0;
    for(ordinal i = 0;i<getSize(a)*INT_SIZE-1;i++){
        bool bit_a = getStdBit(a,i);
        bool bit_b = getStdBit(b,i); 
        bool res = (bit_a + bit_b + ret) & 1; 
        ret = (bit_a + bit_b + ret)/2;
        setStdBit(a,i,res);
    }
    setStdBit(a,getSize(a)*INT_SIZE-1,ret);
}
stdint* ordinalToStd(ordinal i){
    int a = i & INTMAX;
    int b = (i>>INT_SIZE) & INTMAX;
    int* data = malloc(2*sizeof(int));
    data[0] = a;
    data[1] = b;
    return stdint_from(data,2,2);
}