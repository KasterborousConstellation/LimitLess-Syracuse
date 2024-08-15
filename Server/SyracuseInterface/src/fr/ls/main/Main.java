package fr.ls.main;
import fr.ls.main.MSG.*;
import fr.ls.main.files.FileManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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
        frame.setVisible(true);
        area.send("SERVER ONLINE");
        DisplayAgentContainer container = new DisplayAgentContainer();
        frame.add(container);
        final ConnexionButton button = new ConnexionButton("Connect Client",server);
        button.setBounds(width - textWidth - margin, textHeight+margin, 200, 20);
        button.setVisible(true);
        frame.add(button);
        // For file management
        FileManager.DATA_PER_FILE = Integer.parseInt(args[3]);
        final File file = new File(args[2]);
        FileManager.init(file);

        while(!finished){
            //FLUSH BECAUSE IF I DON'T IT DOESN'T WORK
            //PLEASE HELP ME ON THIS
            System.out.flush();
            final ReadRecord message = server.read();
            if(message == ReadRecord.errorRecord)continue;
            final Client client = message.client();
            final MSGFeedback fb = MessageHandler.parseFeedBack(message.line());
            fb.handle(container,client,message.line());
            final Agent agent = container.getAgent(client);
            if(agent==null)continue;
            System.out.println("RECEIVED from "+agent.getName()+":"+message.line());
            fb.handleLate(container,client,message.line());
        }
    }
    public static void sendMessage(Agent agent, String message){
        area_glb.send(MessageHandler.Identifier(agent)+" "+message);
    }
}