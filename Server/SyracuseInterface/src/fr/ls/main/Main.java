package fr.ls.main;
import fr.ls.main.MSG.*;
import fr.ls.main.files.FileManager;
import fr.ls.main.files.FileUtils;
import fr.ls.main.files.SData;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class Main {
    private static int height = 720;
    private static int width =(int) ((float)height *16f/9f);
    public static int margin = 10;
    public static int textWidth = (int)((float)width / 6.0F);
    public static int textHeight = (int)((float)height * 2.0F / 3.0F);
    public static int threadContainerWidth = (int)((float)width / 3.0F) - 2 * margin;
    public static int threadContainerHeight = textHeight - 2 * margin;
    private static boolean finished = false;
    public static String PROJECT_NAME;
    public static String SECURITY_KEY;
    public static UpdateArea area_glb;
    public static ServerHandler server;
    public static void main(String[] args) throws IOException {
        PROJECT_NAME = args[4];
        SECURITY_KEY = args[5];
        assert(PROJECT_NAME.matches("[A-Za-z]"));
        server = new ServerHandler(args[0], Integer.parseInt(args[1]));
        final JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setName("KTBS-LS");
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final UpdateArea area = new UpdateArea(10, 10);
        area_glb = area;
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setPreferredSize(new Dimension(textWidth, height));
        final JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBounds(width - textWidth - margin, 0, textWidth, textHeight);
        frame.add(scrollPane, "Center");

        area.send("SERVER ONLINE");
        DisplayAgentContainer container = new DisplayAgentContainer();
        frame.add(container);
        final ConnexionButton button = new ConnexionButton("Connect Client",server);
        final SaveCacheButton saveCacheButton = new SaveCacheButton("Save Cache");
        button.setBounds(width - textWidth - margin, textHeight+margin, 200, 20);
        button.setVisible(true);
        Rectangle bounds = button.getBounds();
        bounds.translate(0,30);
        saveCacheButton.setBounds(bounds);
        saveCacheButton.setVisible(true);
        frame.add(button);
        frame.add(saveCacheButton);
        final EditContainer editsContainer = new EditContainer();
        editsContainer.setBounds(width - textWidth - margin,textHeight+margin+30*2,200,130);
        editsContainer.setVisible(true);
        frame.add(editsContainer);
        frame.setVisible(true);
        // For file management
        FileManager.DATA_PER_FILE = Integer.parseInt(args[3]);
        File file = new File(args[2]);
        FileManager.init(file);
        while(!finished){
            //FLUSH BECAUSE IF I DON'T IT DOESN'T WORK
            //PLEASE HELP ME ON THIS
            System.out.flush();
            final ReadRecord message = server.read();
            if(message == ReadRecord.errorRecord)continue;
            final Agent agent = message.agent();
            final MSGFeedback fb = MessageHandler.parseFeedBack(message.line());
            fb.handle(container,agent,message.line());
            if(agent==null)continue;
            System.out.println("RECEIVED from "+agent.getName()+":"+message.line());
            fb.handleLate(container,agent,message.line());
        }
    }
    public static void sendMessage(Agent agent, String message){
        area_glb.send(MessageHandler.Identifier(agent)+" "+message);
    }
}