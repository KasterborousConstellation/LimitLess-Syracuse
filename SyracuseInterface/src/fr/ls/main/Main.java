package fr.ls.main;
import fr.ls.main.MSG.*;

import javax.swing.*;
import java.awt.*;
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
    public static void main(String[] args) throws IOException {
        final ServerHandler server = new ServerHandler(args[0], Integer.parseInt(args[1]));
        JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setName("KTBS-LS");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final UpdateArea area = new UpdateArea(10, 10);
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
        while(!finished){
            if(server.canRead()){
                final ReadRecord message = server.read();
                System.out.println("RECEIVED "+message.line());
                final Client client = message.client();
                final MSGFeedback fb = MessageHandler.parseFeedBack(message.line());
                if(fb instanceof MSGAgentOnline){
                    container.createAgent(client);
                }else if(fb instanceof MSGNThreads msgnThreads){
                    final Agent agent = container.getAgent(client);
                    agent.setThreads(msgnThreads.getThreads());
                    //Create interface for this client
                    container.getDisplay(client).launch();
                    container.revalidate();
                }
            }
        }
    }
}