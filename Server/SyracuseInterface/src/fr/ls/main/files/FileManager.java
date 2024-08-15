package fr.ls.main.files;

import fr.ls.main.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.System.exit;

public class FileManager {
    /*
    This variable is used to offset the encoded data to avoid null characters, line terminators and other
    special characters that could be problematic when writing to a file.
    As data is encoded up to 10111011 (0xBB) and the offset is 33, the encoded data will be between 33 and 221.
     */
    public static short ENCODING_OFFSET = (short) 33 ;
    public static int DATA_PER_FILE;
    public static File storing_dir;
    public static ArrayList<StorageFile> storage = new ArrayList<>();
    public static void init(File file){
        storing_dir = file;
        if(!file.exists()){
            FileUtils.createDirectory(file);
        }
        System.out.println("FILE MANAGER INIT");
        //READ
        final File[] files_stored = storing_dir.listFiles();
        if(files_stored==null){
            System.err.println("INCORRECT STORAGE DIRECTORY OR PERMISSION DENIED");
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
    public static String getFileName(int index){
        return "SKTBS_"+Main.PROJECT_NAME+"_"+index;
    }
}