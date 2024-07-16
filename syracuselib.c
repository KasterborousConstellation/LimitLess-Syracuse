#include "syracuselib.h"
stdint* computeNextSyracuse(stdint* a,stdint* value){
    stdint* res=copy(a);
    if(isEven(res)){
        rightShift(res);
    }else{
        leftShift(res);
        stdint* tmp = addition(res,a);
        stdint* tmp2 = addition(tmp,value);
        res = copy(tmp2);
        del(tmp);
        del(tmp2);
    }
    return res;
}
ordinal syracuseFlightTime(stdint* a){
    ordinal i = 0;
    stdint* res = copy(a);
    stdint* one = intToStd(1);
    stdint* tmp;
    while(!isEquals(res,one)){
        tmp = computeNextSyracuse(res,one);
        del(res);
        res = tmp;
        i++;
    }
    del(res);
    del(one);
    del(tmp);
    return i;
}