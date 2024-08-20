package fr.ls.main.files;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class StorageFile {
    private final String path;
    private final int index;
    private final SData[] data;
    private final BigInteger directory_index;
    private final File file;
    public StorageFile(BigInteger directory_index, int index){
        this.directory_index = directory_index;
        data = new SData[FileManager.DATA_PER_FILE];
        if(!FileManager.storing_dir.exists()){
            FileUtils.createDirectory(FileManager.storing_dir);
        }
        path = FileManager.storing_dir.getAbsolutePath()+"/"+"partition_"+directory_index.toString()+"/"+FileManager.getFileName(index);
        this.file = new File(path);
        this.index = index;
    }
    public void read(){
        final String content = FileUtils.read(file).get(0);
        final char[] chars = content.toCharArray();
        final SFormatReader builder = new SFormatReader();
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
                if(!file.getParentFile().exists()){
                    FileUtils.createDirectory(file.getParentFile());
                }
                FileUtils.createFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                FileManager.parseError("UNABLE TO CREATE FILE: "+path);
            }
        }
        final SFormatWriter builder = new SFormatWriter();
        for(final SData d : data){
            builder.feed(d);
        }
        builder.flush();
        builder.write(file);
    }
    public void set(int index,String entry){
        data[index] = new SData(entry);
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
    public BigInteger getDirectoryIndex(){
        return directory_index;
    }
}
