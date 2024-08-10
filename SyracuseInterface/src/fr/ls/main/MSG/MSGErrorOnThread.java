package fr.ls.main.MSG;


import fr.ls.main.Client;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;
import fr.ls.main.ThreadState;

import java.awt.*;

public class MSGErrorOnThread extends MSGFeedback{
    private final int thread;
    public MSGErrorOnThread(String source,int thread) {
        super(source);
        this.thread = thread;
    }
    public int getThread(){
        return thread;
    }

    @Override
    public void handle(DisplayAgentContainer container, Client client, String message) {
        container.getDisplay(client).getPanel(getThread()).setStatus(ThreadState.ERROR);
        Main.area_glb.setCaretColor(Color.RED);
        Main.sendMessage(container.getAgent(client),"Error on thread "+getThread());
        Main.area_glb.setCaretColor(null);
    }
}
