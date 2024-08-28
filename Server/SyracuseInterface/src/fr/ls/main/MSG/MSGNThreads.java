package fr.ls.main.MSG;

import fr.ls.main.Agent;
import fr.ls.main.DisplayAgentContainer;

public class MSGNThreads extends ThreadedMSGFeedback{
    public MSGNThreads(String source, int threads){
        super(source,threads);

    }
    @Override
    public void handle(DisplayAgentContainer container, Agent client, String message) {
        client.setThreads(getThread());
        //Create interface for this client
        container.getDisplay(client).launch();
        container.revalidate();
    }
}
