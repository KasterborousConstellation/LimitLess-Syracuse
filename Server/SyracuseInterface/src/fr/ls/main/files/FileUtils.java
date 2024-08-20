package fr.ls.main.files;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
            final FileWriter writer = new FileWriter(file, StandardCharsets.UTF_16);
            writer.write(content);
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String readFull(File file){
        ArrayList<String> lines = read(file);
        StringBuilder content = new StringBuilder();
        for(final String line : lines){
            content.append(line).append("\n");
        }
        return content.toString();
    }
    public static ArrayList<String> read(File file) {
        try {
            final ArrayList<String> lines = new ArrayList<>();
            final BufferedReader bufferedReader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_16);
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