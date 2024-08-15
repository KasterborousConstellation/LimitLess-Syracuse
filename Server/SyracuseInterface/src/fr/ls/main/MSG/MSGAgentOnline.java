package fr.ls.main.MSG;

import fr.ls.main.Client;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class MSGAgentOnline extends MSGFeedback{
    private final String key;
    public MSGAgentOnline(String source,int agentID,String securityKEY) {
        super(source);
        this.key=securityKEY;
    }

    @Override
    public void handle(DisplayAgentContainer container,Client client, String message) {
        container.createAgent(client);
        if(!key.equals(Main.SECURITY_KEY)){
            System.err.println("SECURITY BREACH: INVALID SECURITY KEY");
            Main.server.breach(client);
            Main.sendMessage(container.getAgent(client),"INVALID SECURITY KEY");
            final Runnable runnable = () -> {
                Main.sendMessage(container.getAgent(client),"AGENT CLEARED");
                try{
                    container.getDisplay(client).end();
                }catch (NullPointerException e){
                    return;
                }
                container.removeAgent(client);
                Main.server.removeClient(client);
                container.revalidate();
            };
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            executor.schedule(runnable,750, TimeUnit.MILLISECONDS);
            return;
        }
        Main.sendMessage(container.getAgent(client),"Welcome "+client.getSocket().getInetAddress().getHostAddress()+":"+client.getSocket().getPort());
    }
}
