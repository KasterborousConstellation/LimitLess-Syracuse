package fr.ls.main.MSG;

import fr.ls.main.Client;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;

public class MSGAgentOnline extends MSGFeedback{
    public MSGAgentOnline(String source) {
        super(source);
    }

    @Override
    public void handle(DisplayAgentContainer container,Client client, String message) {
        container.createAgent(client);
        Main.sendMessage(container.getAgent(client),"Welcome "+client.getSocket().getInetAddress().getHostAddress()+":"+client.getSocket().getPort());
    }
}
