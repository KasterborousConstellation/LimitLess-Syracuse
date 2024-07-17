#include "syracuselib.h"
/**
 *   Compute the next syracuse number
 *   @param a the number to compute the next syracuse number
 *   @param value the value to add to the number
 *   @return void
*/
void computeNextSyracuse(stdint* a,stdint* value){
    stdint* res=copy(a);
    if(isEven(a)){
        rightShift(a);
    }else{
        leftShift(a);
        selfAddition(a,res);
        selfAddition(a,value);
    }
    del(res);
}
/**
 *   Compute the syracuse flight time of a number
 *   @param a the number to compute the syracuse flight time
 *   @return the syracuse flight time of the number
*/
ordinal syracuseFlightTime(stdint* a){
    ordinal i = 0;
    stdint* res = copy(a);
    stdint* one = intToStd(1);
    while(!isEquals(res,one)){
        computeNextSyracuse(res,one);
        i++;
    }
    del(res);
    del(one);
    return i;
}