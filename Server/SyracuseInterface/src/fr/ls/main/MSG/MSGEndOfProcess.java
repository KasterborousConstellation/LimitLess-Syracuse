package fr.ls.main.MSG;

import fr.ls.main.Agent;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;

public class MSGEndOfProcess extends MSGFeedback{

    public MSGEndOfProcess(String source) {
        super(source);
    }

    @Override
    public void handle(DisplayAgentContainer container, Agent client, String message) {
        Main.sendMessage(client,"Client sign out from "+client.getSocket().getInetAddress().getHostAddress()+":"+client.getSocket().getPort());
    }

    @Override
    public void handleLate(DisplayAgentContainer container, Agent client, String message) {
        container.getDisplay(client).end();
        container.removeAgent(client);
        Main.server.removeAgent(client);
        container.revalidate();
    }
}
