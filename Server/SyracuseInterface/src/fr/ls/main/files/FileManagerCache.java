package fr.ls.main.files;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

public class FileManagerCache {
    private final int CACHE_CONSTANT = 3;
    // The constant controlling the speed at which the cache is removed from the cache
    //Here it means that it will take CACHE_CONSTANT time more calls
    // to other cache indexes to remove the cached file from the cache
    private final StorageFile[] array = new StorageFile[FileManager.CACHE_SIZE];
    private final int[] uses = new int[FileManager.CACHE_SIZE];
    private void update(){
        for(int i = 0;i<uses.length;i++){
            uses[i]-=1;
        }
    }
    public boolean isCached(BigInteger partition,int index){
        for(StorageFile file : array){
            if(file!=null&&file.getIndex()==index && partition.equals(file.getDirectoryIndex())){
                return true;
            }
        }
        return false;
    }
    public void add(StorageFile file){
        int index = getFirstEmpty();
        if(isFull()){
            int min = Integer.MAX_VALUE;
            int min_index = -1;
            for(int i = 0;i<uses.length;i++){
                if(uses[i]<min){
                    min = uses[i];
                    min_index = i;
                }
            }
            array[min_index] = file;
            uses[min_index] = 0;
        }else{
            array[index] = file;
            uses[index] = 0;
        }
    }
    public boolean isFull(){
        for(StorageFile file : array){
            if(file==null){
                return false;
            }
        }
        return true;
    }
    public int getFirstEmpty(){
        for(int i = 0;i<array.length;i++){
            if(array[i]==null){
                return i;
            }
        }
        return -1;
    }
}