#include "stdintlib.h"
#include "assert.h"
#include "math.h"
#define INT_SIZE 32
#define LONG_SIZE 64
#define LONGMAX 9223372036854775807
#define INTMAX 2147483647
format min(format a,format b){
    return (a<b)?a:b;
}
format max(format a,format b){
    return (a>b)?a:b;
}
stdint* stdint_from(format* array,ordinal size,ordinal capacity){
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
    format* data = malloc(capacity*sizeof(format));
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
        format* data = malloc(2*size*sizeof(int));
        for(ordinal e=0;e<getCapacity(i);e++){
            data[e] = i->data[e];
        }
        i->capacity = 2*size;
        i->data = data;
    }else if(size<getCapacity(i)/4){
        format* data = malloc((getCapacity(i)/2)*sizeof(int));
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
        ordinal targetted_size = 2*p/INT_SIZE;
        resize(i,targetted_size);
    }
    //We now need to know if the p bit is 1 or 0 
    //Case 1 if p bit is 1
    int part = getPartition(i,p/INT_SIZE);
    int mask = ~(1<<(p%INT_SIZE));
    setPartition(i,p/INT_SIZE,part&mask | (value<<(p%INT_SIZE)));
}
bool getStdBit(stdint* a,ordinal p){
    if(p>=getSize(a)*INT_SIZE){
        return 0;
    }
    ordinal place = p/INT_SIZE;
    int part = getPartition(a,place);
    return (part>>p%INT_SIZE) & 1;
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
    stdint* new = copy(a);
    //Iterate for every bits of new 
    bool ret = 0;
    for(ordinal i = 0;i<getSize(new)*INT_SIZE;i++){
        bool bit_a = getStdBit(a,i);
        bool bit_b = getStdBit(b,i); 
        bool res = (bit_a + bit_b + ret) & 1; 
        ret = (bit_a + bit_b + ret)/2;
        setStdBit(new,i,res);
    }
    setStdBit(new,getSize(new)*INT_SIZE,ret);
    return new;
}
stdint* intToStd(unsigned int i){
    format* data = malloc(sizeof(format));
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
/**
 * Add a number to another number
 *
 * @param n the first number and also the one to store the result
 * @param to_add the number to add to the first number
 * @return void
 */
void selfAddition(stdint* n, stdint* to_add){
    stdint* a = n;
    stdint* b = to_add;
    ordinal size;
    if(getSize(a)<getSize(to_add)){
        size = getSize(to_add);
        resize(a,size);
    }else{
        size = getSize(a);
        resize(b,size);
    }
    //Iterate for every bits of new 
    bool ret = 0;
    for(ordinal i = 0;i<(size*INT_SIZE);i++){
        bool bit_a = getStdBit(a,i);
        bool bit_b = getStdBit(b,i); 
        bool res = (bit_a + bit_b + ret) & 1; 
        ret = (bit_a + bit_b + ret)/2;
        setStdBit(a,i,res);
    }
    if(ret){
        setStdBit(a,size*INT_SIZE,ret);
    }
}
/**
 * Converts an ordinal number to its standard form.
 *
 * @param i The ordinal number to convert.
 * @return The standard form of the ordinal number.
 */
stdint* ordinalToStd(ordinal i){
    int a = i & INTMAX;
    int b = (i>>INT_SIZE) & INTMAX;
    format* data = malloc(2*sizeof(format));
    data[0] = a;
    data[1] = b;
    return stdint_from(data,2,2);
}
int digits_in_base10(ordinal size){
    return 1 + log(2)*(size)/log(10);
}
char getChar(char i){
    return i+'0';
}
decimal* create_decimal(ordinal capacity){
    decimal* i = malloc(sizeof(decimal));
    char* data = malloc(capacity*sizeof(char));
    i->capacity = capacity;
    i->size=1;
    i->data = data;
    return i;
}
decimal* decimal_from(char* array,ordinal size,ordinal capacity){
    decimal* a = create_decimal(capacity);
    a->size = size;
    for(ordinal e = 0; e<size ;e++){
        a->data[e] = array[e];
    }
    return a;
}
decimal* char_to_decimal(char a){
    char* data = malloc(sizeof(char));
    data[0] = a;
    return decimal_from(data,1,1);
}
decimal* copy_decimal(decimal* a){
    return decimal_from(a->data,a->size,a->capacity);
}
void resize_decimal(decimal* i, ordinal size){
    if(size>=i->capacity){
        char* data = malloc(2*size*sizeof(char));
        for(ordinal e=0;e<i->capacity;e++){
            data[e] = i->data[e];
        }
        i->capacity = 2*size;
        i->data = data;
    }else if(size<i->capacity/4){
        char* data = malloc((i->capacity/2)*sizeof(char));
        for(ordinal e=0;e<i->capacity/2;e++){
            data[e] = i->data[e];
        }
        i->capacity = i->capacity/2;
        i->data = data;
    }
    if(size>i->size){
        for(ordinal e = i->size;e<size;e++){
            i->data[e] =0;
        }
    }
    i->size = size;
}
void del_decimal(decimal* i){
    free(i->data);
    free(i);
}
ordinal getSizeDecimal(decimal* a){
    return a->size;
}
void selfAdditionDecimal(decimal* a, decimal* b){
    ordinal size;
    if(getSizeDecimal(a)<getSizeDecimal(b)){
        size = getSizeDecimal(b);
        resize_decimal(a,size);
    }else{
        size = getSizeDecimal(a);
        resize_decimal(b,size);
    }
    //Iterate for every bits of new 
    bool ret = 0;
    for(ordinal i = 0;i<size;i++){
        char bit_a = a->data[i];
        char bit_b = b->data[i]; 
        char res = (bit_a + bit_b + ret) % 10; 
        ret = (bit_a + bit_b + ret)/10;
        a->data[i] = res;
    }
    if(ret){
        resize_decimal(a,getSizeDecimal(a)+1);
        a->data[getSizeDecimal(a)-1] = ret;
    }
}
char* convertToChar(stdint* a){
    decimal* res = char_to_decimal(0);
    decimal* tmp = char_to_decimal(1);
    for(ordinal i =0;i<getSize(a)*INT_SIZE;i++){
        if(getStdBit(a,i)){
            selfAdditionDecimal(res,tmp);
        }
        selfAdditionDecimal(tmp,tmp);
    }
    char* data = malloc((getSizeDecimal(res)+1)*sizeof(char));
    for(ordinal i = 0;i<getSizeDecimal(res);i++){
        data[getSizeDecimal(res)-i-1] = getChar(res->data[i]);
    }
    data[getSizeDecimal(res)] = '\0';
    del_decimal(res);
    del_decimal(tmp);
    return data;
}
void printDecimalStdInt(stdint* a){
    char* data = convertToChar(a);
    char* tmp = data;
    while(*data){
        printf("%c",*data);
        data++;
    }
    printf("\n");
    free(tmp);
}
stdint* multiplication(stdint* a, stdint* b){
    if(isZero(a) || isZero(b)){
        return intToStd(0);
    }
    ordinal size = (getSize(a)<getSize(b))?getSize(b):getSize(a);
    stdint* top = copy(a);
    stdint* bottom = copy(b);
    resize(top,size);
    resize(bottom,size);
    stdint* res = intToStd(0);
    for(ordinal i = 0;i<size*INT_SIZE;i++){
        if(getStdBit(bottom,i)){
            selfAddition(res,top);
        }
        leftShift(top);
    }
    del(top);
    del(bottom);
    return res;
}
void selfMultiplication(stdint* a, stdint* b){
    if(isZero(a) || isZero(b)){
        stdint* z = intToStd(0);
        a->data = z->data;
        a->size = z->size;
        a->capacity = z->capacity;
        del(z);
        return;
    }
    ordinal size = (getSize(a)<getSize(b))?getSize(b):getSize(a);
    stdint* top = copy(a);
    stdint* bottom = copy(b);
    resize(top,size);
    resize(bottom,size);
    stdint* res = intToStd(0);
    for(ordinal i = 0;i<size*INT_SIZE;i++){
        if(getStdBit(bottom,i)){
            selfAddition(res,top);
        }
        leftShift(top);
    }
    del(top);
    del(bottom);
    a->data = res->data;
    a->size = res->size;
    a->capacity = res->capacity;
    //DO NOT TOUCH
    free(res);
}
ordinal getPositionMSB(stdint* a){
    ordinal pos = 0;
    for(ordinal i=0;i<getSize(a)*INT_SIZE;i++){
        if(getStdBit(a,i)){
            pos = i;
        }
    }
    return pos;
}

stdint* reverse(stdint* a,format upTo){
    stdint* res = copy(a);
    for(ordinal i = 0;i<min(upTo,getSize(a)*INT_SIZE);i++){
        setStdBit(res,i,!getStdBit(a,i));
    }
    return res;
}
bool isGreater(stdint* a, stdint* b){
    if(getPositionMSB(a)>getPositionMSB(b)){
        return true;
    }else if(getPositionMSB(a)<getPositionMSB(b)){
        return false;
    }else{
        for(ordinal i = 0;i<=getPositionMSB(a);i++){
            ordinal e = getPositionMSB(a)-i;
            if(getStdBit(a,e)>getStdBit(b,e)){
                return true;
            }else if(getStdBit(a,e)<getStdBit(b,e)){
                return false;
            }
        }
    }
    return false;
}
bool isGreaterOrEquals(stdint* a, stdint* b){
    if(getPositionMSB(a)>getPositionMSB(b)){
        return true;
    }else if(getPositionMSB(a)<getPositionMSB(b)){
        return false;
    }else{
        for(ordinal i = 0;i<=getPositionMSB(a);i++){
            ordinal e = getPositionMSB(a)-i;
            if(getStdBit(a,e)>getStdBit(b,e)){
                return true;
            }else if(getStdBit(a,e)<getStdBit(b,e)){
                return false;
            }
        }
    }
    return true;
}
void selfSubstraction(stdint* a, stdint* b){
    assert(isGreater(a,b)||isEquals(a,b));
    ordinal a_msb = getPositionMSB(a);
    stdint* rev = reverse(b,a_msb+1);
    stdint* tmp = copy(a);
    stdint* one = intToStd(1);
    selfAddition(tmp,one);
    selfAddition(tmp,rev);
    for(ordinal e =0;e<=a_msb;e++){
        setStdBit(a,e,getStdBit(tmp,e));
    }
    del(one);
    del(tmp);
    del(rev);
}
int getN(char c){
    return (int) c - '0';
}
stdint* stdAtoi(char* str){
    stdint* res = intToStd(0);
    stdint* tmp= intToStd(1);
    stdint* ten = intToStd(10);
    for(size_t i = 0;i<strlen(str);i++){
        char c = str[strlen(str)-i-1];
        if(c>='0'&&c<='9'){
            int int_data = getN(c);
            stdint* tmp2 = intToStd(int_data);
            stdint* tadd = multiplication(tmp,tmp2);
            selfAddition(res,tadd);
            del(tadd);
            del(tmp2);
        }
        selfMultiplication(tmp,ten);
    }
    del(ten);
    del(tmp);
    return res;
}
stdint* substraction(stdint* a, stdint* b){
    stdint* res = copy(a);
    selfSubstraction(res,b);
    return res;
}
stdint* division(stdint* a, stdint* b){
    stdint* res = intToStd(0);
    stdint* tmp = copy(a);
    stdint* one = intToStd(1);
    while(isGreaterOrEquals(tmp,b)){
        selfSubstraction(tmp,b);
        selfAddition(res,one);
    }
    del(tmp);
    del(one);
    return res;
}