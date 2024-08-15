package fr.ls.main.MSG;

import fr.ls.main.Client;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;

public class MSGEndOfProcess extends MSGFeedback{

    public MSGEndOfProcess(String source) {
        super(source);
    }

    @Override
    public void handle(DisplayAgentContainer container, Client client, String message) {
        Main.sendMessage(container.getAgent(client),"Client sign out from "+client.getSocket().getInetAddress().getHostAddress()+":"+client.getSocket().getPort());
    }

    @Override
    public void handleLate(DisplayAgentContainer container, Client client, String message) {
        container.getDisplay(client).end();
        container.removeAgent(client);
        container.revalidate();
    }
}
