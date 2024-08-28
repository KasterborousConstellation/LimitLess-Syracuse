package fr.ls.main.MSG;

import fr.ls.main.Agent;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;
import fr.ls.main.ThreadState;

public class MSGEndOfThread extends ThreadedMSGFeedback{

    public MSGEndOfThread(String source, int thread) {
        super(source, thread);
    }

    @Override
    public void handle(DisplayAgentContainer container, Agent client, String message) {
        container.getDisplay(client).getPanel(getThread()).setStatus(ThreadState.TERMINATED);
        Main.sendMessage(client,"Thread "+getThread()+" terminated");
    }
}