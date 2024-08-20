package fr.ls.main.files;

import java.math.BigInteger;
import java.util.*;

public class FileManagerCache {
    private final int CACHE_SIZE = 10;
    private record CacheElement(BigInteger directory_index, int index){
        public static int getHash(BigInteger directory_index, int index){
            return new CacheElement(directory_index,index).getHashCode();
        }
        public int getHashCode(){
            return directory_index.hashCode() + index;
        }
    }
    private final Deque<StorageFile> cache;
    private final HashMap<Integer,StorageFile> cacheSet;
    public FileManagerCache(){
        cache = new LinkedList<>();
        cacheSet = new HashMap<>();
    }
    /*
    * Add a file to the cache
    * Complexity : O(1)
     */
    public void add(StorageFile file){
        if(cache.size()>=CACHE_SIZE){
            final StorageFile removed = cache.pollLast();
            removed.write();
            cacheSet.remove(CacheElement.getHash(removed.getDirectoryIndex(),removed.getIndex()));
        }
        cache.add(file);
        final CacheElement element = new CacheElement(file.getDirectoryIndex(),file.getIndex());
        cacheSet.put(element.getHashCode(),file);
    }

    /*
    * Get a file from the cache if in cache else read it from the storage and add it to the cache
    * Complexity : O(1) if file is in cache else O(1) + O(StorageFile.read()) = EXPENSIVE
     */
    public StorageFile get(BigInteger directory_index, int index){
        if(!cacheSet.containsKey(CacheElement.getHash(directory_index,index))){
            final StorageFile file = getFile(directory_index,index);
            add(file);
            return file;
        }
        return cacheSet.get(CacheElement.getHash(directory_index,index));
    }
    /*
    * Check if the cache contains a file
    * Complexity : O(1)
     */
    public boolean contains(BigInteger directory_index, int index){
        return cacheSet.containsKey(CacheElement.getHash(directory_index,index));
    }
    public int getCacheSize(){
        return cache.size();
    }
    /*
    * Clear the cache
    * Complexity : O(CacheSize)
     */
    public void clear(){
        cache.clear();
        cacheSet.clear();
    }
    private StorageFile getFile(BigInteger partition, int index){
        final StorageFile file = new StorageFile(partition,index);
        if(file.exist()) {
            file.read();
        }else{
            file.write();
        }
        return file;
    }
    public void saveCache(){
        for(final StorageFile file : cache){
            file.write();
        }
    }
}