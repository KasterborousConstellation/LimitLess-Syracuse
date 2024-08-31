package fr.ls.main;

import fr.ls.main.MSG.MSGFeedback;

import javax.swing.*;
import java.awt.*;

public class UIHandler {
    private final static int height = 720;
    private final static int width =(int) ((float)height *16f/9f);
    public static final int margin = 10;
    public static final int textWidth = (int)((float)width / 6.0F);
    public static final int textHeight = (int)((float)height * 2.0F / 3.0F);
    public static final int threadContainerWidth = (int)((float)width / 3.0F) - 2 * margin;
    public static final int threadContainerHeight = textHeight - 2 * margin;
    private final JFrame main_frame;
    private final ServerHandler server;
    private final DisplayAgentContainer container;
    public UIHandler(ServerHandler server){
        this.server=server;
        final JFrame frame = new JFrame();
        main_frame=frame;
        frame.setSize(width, height);
        frame.setName("KTBS-LS");
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final UpdateArea area = new UpdateArea(10, 10);
        Main.area_glb = area;
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setPreferredSize(new Dimension(textWidth, height));
        final JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBounds(width - textWidth - margin, 0, textWidth, textHeight);
        frame.add(scrollPane, "Center");

        area.send("SERVER ONLINE");
        container = new DisplayAgentContainer();
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
    }
    private boolean finished = false;

    public void launch(){
        main_frame.setVisible(true);
        while(!finished){
            //FLUSH BECAUSE IF I DON'T IT DOESN'T WORK
            //PLEASE HELP ME ON THIS
            System.out.flush();
            final ReadRecord message = server.read();
            if(message == ReadRecord.errorRecord)continue;
            final Agent agent = message.agent();
            final MSGFeedback fb = MessageHandler.parseFeedBack(message.line());
            fb.handle(container,agent,message.line());
            if(agent==null){
                continue;
            }
            System.out.println("RECEIVED from "+agent.getName()+":"+message.line());
            fb.handleLate(container,agent,message.line());
        }
    }
}
