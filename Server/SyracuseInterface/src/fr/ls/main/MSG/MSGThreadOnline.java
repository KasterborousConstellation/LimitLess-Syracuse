package fr.ls.main.MSG;

import fr.ls.main.*;

public class MSGThreadOnline extends MSGFeedback{
    private final int thread;
    public MSGThreadOnline(String source, int thread) {
        super(source);
        this.thread = thread;
    }
    public int getThread(){
        return thread;
    }

    @Override
    public void handle(DisplayAgentContainer container, Agent client, String message) {
        ThreadPanel threadPanel = container.getDisplay(client).getPanel(getThread());
        threadPanel.setStatus(ThreadState.ONLINE);
        Main.sendMessage(client,"Thread "+getThread()+" online");
    }
}
