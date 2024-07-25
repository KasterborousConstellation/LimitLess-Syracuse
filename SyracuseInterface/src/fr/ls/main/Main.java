package fr.ls.main;
import fr.ls.main.MSG.MSGEndOfProcess;
import fr.ls.main.MSG.MSGEndOfThread;
import fr.ls.main.MSG.MSGFeedback;
import fr.ls.main.MSG.MSGThreadOnline;

import javax.swing.*;
import java.awt.*;
import java.net.ConnectException;

public class Main {
    private static int height = 720;
    private static int width =(int) ((float)height *16f/9f);
    public static int margin = 10;
    public static int textWidth = (int)((float)width / 6.0F);
    public static int textHeight = (int)((float)height * 2.0F / 3.0F);
    public static int threadContainerWidth = (int)((float)width / 3.0F) - 2 * margin;
    public static int threadContainerHeight = textHeight - 2 * margin;
    private static boolean finished = false;
    public static void main(String[] args) {
        ClientHandler client = new ClientHandler(args[0], Integer.parseInt(args[1]));
        int num_threads = Integer.parseInt(args[2]);
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
        AgentContainer container = new AgentContainer("ALPHA",num_threads);
        frame.add(container);
        frame.setVisible(true);
        client.setUp();
        area.send("CLIENT ONLINE");
        while(!finished){
            if(client.canRead()){
                final String message = client.read();
                System.out.println("RECEIVED: "+message);
                final MSGFeedback fb = MessageHandler.parseFeedBack(message);
                if(fb instanceof MSGThreadOnline online){
                    container.getPanel(online.getThread()).setStatus(ThreadState.ONLINE);
                    area.send("THREAD "+online.getThread()+" is ONLINE");
                }else if(fb instanceof MSGEndOfThread ofThread){
                    container.getPanel(ofThread.getThread()).setStatus(ThreadState.OFFLINE);
                    area.send("THREAD "+ofThread.getThread()+" is OFFLINE");
                }else if(fb instanceof MSGEndOfProcess endOfProcess){
                    area.send("COMPUTATION FINISHED");
                }
            }
        }
    }
}