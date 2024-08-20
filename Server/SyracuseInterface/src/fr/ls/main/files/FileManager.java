package fr.ls.main.files;
import fr.ls.main.Main;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import static java.lang.System.exit;
public class FileManager {
    public static int FILE_PER_PARTITION = 0xffff-1;
    public static int DATA_PER_FILE;
    public static File storing_dir;
    public static BigInteger DIRECTORY_STORING_CAPACITY;
    public static BigInteger U_DATA_PER_FILE;
    public static BigInteger U_FILE_PER_PARTITION;
    public static FileManagerCache fileManagerCache;
    public static void init(File file){
        storing_dir = file;
        fileManagerCache = new FileManagerCache();
        //Check for overflow
        U_FILE_PER_PARTITION = BigInteger.valueOf(FILE_PER_PARTITION);
        U_DATA_PER_FILE = BigInteger.valueOf(DATA_PER_FILE);
        DIRECTORY_STORING_CAPACITY = U_FILE_PER_PARTITION.multiply(U_DATA_PER_FILE);
        if(!file.exists()){
            FileUtils.createDirectory(file);
        }
        System.out.println("FILE MANAGER INIT");
        //READ
        final File[] files_stored = storing_dir.listFiles();
        if(files_stored==null){
            parseError("UNABLE TO READ STORAGE DIRECTORY");
        }
    }
    public static void parseError(String message){
        System.err.println(message);
        exit(1);
    }
    public static void setData(BigInteger index,SData data){
        final BigInteger partition = index.divide(DIRECTORY_STORING_CAPACITY);
        final int file_index = index.mod(DIRECTORY_STORING_CAPACITY).divide(U_DATA_PER_FILE).intValue();
        fileManagerCache.get(partition,file_index).set(index.mod(U_DATA_PER_FILE).intValue(),data);
    }
    public static SData getData(BigInteger index){
        final BigInteger partition = index.divide(DIRECTORY_STORING_CAPACITY);
        final int file_index = index.mod(DIRECTORY_STORING_CAPACITY).divide(U_DATA_PER_FILE).intValue();
        return fileManagerCache.get(partition,file_index).get(index.mod(U_DATA_PER_FILE).intValue());
    }
    public static String getFileName(int index){
        return "SKTBS_"+Main.PROJECT_NAME+"_"+index;
    }
}