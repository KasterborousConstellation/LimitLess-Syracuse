package fr.ls.main.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static java.lang.System.exit;
public class StorageFile {
    private final String path;
    private final int index;
    private final SData[] data;
    private final File file;
    public StorageFile(int index){
        data = new SData[FileManager.DATA_PER_FILE];
        path = FileManager.storing_dir.getAbsolutePath()+"/"+FileManager.getFileName(index);
        this.file = new File(path);
        this.index = index;
    }
    public void read(){
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
        if(!exist()){
            try {
                FileUtils.createFile(file);
            } catch (IOException e) {
                System.err.println("UNABLE TO CREATE FILE: "+path);
                exit(1);
            }
        }
        final SFormatBuilder builder = new SFormatBuilder();
        for(final SData d : data){
            builder.feed(d);
        }
        builder.write(file);
    }
    public int getIndex(){
        return index;
    }
    public boolean exist(){
        return new File(path).exists();
    }
    public void set(int index,SData d){
        data[index] = d;
    }
    public SData get(int index){
        return data[index];
    }
}
