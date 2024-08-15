package fr.ls.main.files;
import java.io.*;
import java.util.ArrayList;
public class FileUtils {
    public static void createDirectory(File file) {
        if(!file.exists()){
            file.mkdirs();
        }
    }
    public static void createFile(File file) throws IOException {
        if(!file.exists()){
            file.createNewFile();
        }
    }
    public static void write(File file, String content){
        try{
            final FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> read(File file) {
        try {
            final FileReader reader = new FileReader(file);
            final ArrayList<String> lines = new ArrayList<>();
            final BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
            return lines;
        }catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
