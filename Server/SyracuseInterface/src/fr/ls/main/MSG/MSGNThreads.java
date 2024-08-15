package fr.ls.main.MSG;

import fr.ls.main.Agent;
import fr.ls.main.Client;
import fr.ls.main.DisplayAgentContainer;

public class MSGNThreads extends ThreadedMSGFeedback{
    public MSGNThreads(String source, int threads){
        super(source,threads);

    }
    @Override
    public void handle(DisplayAgentContainer container, Client client, String message) {
        final Agent agent = container.getAgent(client);
        agent.setThreads(getThread());
        //Create interface for this client
        container.getDisplay(client).launch();
        container.revalidate();
    }
}
