package fr.ls.main.MSG;

import fr.ls.main.Client;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;
import fr.ls.main.ThreadState;

public class MSGEndOfThread extends ThreadedMSGFeedback{

    public MSGEndOfThread(String source, int thread) {
        super(source, thread);
    }

    @Override
    public void handle(DisplayAgentContainer container, Client client, String message) {
        container.getDisplay(client).getPanel(getThread()).setStatus(ThreadState.TERMINATED);
        Main.sendMessage(container.getAgent(client),"Thread "+getThread()+" terminated");
    }
}