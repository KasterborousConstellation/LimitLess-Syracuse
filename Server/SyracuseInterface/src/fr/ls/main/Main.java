package fr.ls.main;
import fr.ls.main.files.FileManager;
import java.io.File;
import java.io.IOException;

public class Main {
    public static String PROJECT_NAME;
    public static String SECURITY_KEY;
    public static UpdateArea area_glb;
    public static ServerHandler server;
    public static void main(String[] args) throws IOException {
        PROJECT_NAME = args[4];
        SECURITY_KEY = args[5];
        assert(PROJECT_NAME.matches("[A-Za-z]"));
        server = new ServerHandler(args[0], Integer.parseInt(args[1]));
        final UIHandler ui = new UIHandler(server);
        // For file management
        FileManager.DATA_PER_FILE = Integer.parseInt(args[3]);
        File file = new File(args[2]);
        FileManager.init(file);
        ui.launch();
    }
    public static void sendMessage(Agent agent, String message){
        area_glb.send(MessageHandler.Identifier(agent)+" "+message);
    }
}