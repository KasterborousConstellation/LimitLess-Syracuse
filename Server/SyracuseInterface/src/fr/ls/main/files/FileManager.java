package fr.ls.main.files;

import fr.ls.main.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.System.exit;

public class FileManager {
    public static int DATA_PER_FILE;
    public static File storing_dir;
    public static ArrayList<StorageFile> storage = new ArrayList<>();
    public static void init(File file){
        storing_dir = file;
        System.out.println("FILE MANAGER INIT");
        //READ
        final File[] files_stored = storing_dir.listFiles();
        if(files_stored==null){
            System.out.println("INCORRECT STORAGE DIRECTORY");
            exit(1);
            return;
        }
        for(final File fs : files_stored){
            final String name = fs.getName();
            final String[] split = name.split("_");
            if(split.length==3&&!"SKTBS".equals(split[0])&&!Main.PROJECT_NAME.equals(split[1])){
                parseError("INCORRECT STORAGE FILE NAME: "+name);
                return;
            }
            int file_index;
            try{
                file_index = Integer.parseInt(split[2]);
            }catch (final NumberFormatException e){
                parseError("INCORRECT STORAGE FILE NAME: "+name);
                return;
            }

        }
    }
    private static void parseError(String message){
        System.err.println(message);
        exit(1);
    }
}