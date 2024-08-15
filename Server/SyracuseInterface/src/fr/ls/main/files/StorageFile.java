package fr.ls.main.files;

import java.io.File;
import java.util.ArrayList;

public class StorageFile {
    private final String path;
    private final int index;
    private final SData[] data;
    public StorageFile(int index){
        data = new SData[FileManager.DATA_PER_FILE];
        path = FileManager.storing_dir.getAbsolutePath()+"/"+FileManager.getFileName(index);
        this.index = index;
    }
    public void read(){
        final File file = new File(path);
        final String content = FileUtils.read(file).get(0);
        final char[] chars = content.toCharArray();
        final SDataBuilder builder = new SDataBuilder();
        for(char chr : chars){
            builder.feed(chr);
        }
        final ArrayList<SData> data = builder.retrieve();
        assert(data.size()==FileManager.DATA_PER_FILE);
        for(int i = 0;i<FileManager.DATA_PER_FILE;i++){
            this.data[i] = data.get(i);
        }
    }
    public void write(){

    }
    public int getIndex(){
        return index;
    }
    private boolean exist(){
        return new File(path).exists();
    }
    public void set(int index,SData d){
        data[index] = d;
    }
    public SData get(int index){
        return data[index];
    }
}
