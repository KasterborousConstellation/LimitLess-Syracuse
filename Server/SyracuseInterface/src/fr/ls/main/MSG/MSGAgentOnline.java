package fr.ls.main.MSG;

import fr.ls.main.Agent;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MSGAgentOnline extends MSGFeedback{
    private final String key;
    private int agentID;
    public MSGAgentOnline(String source,int agentID,String securityKEY) {
        super(source);
        this.key=securityKEY;
        this.agentID = agentID;
    }

    @Override
    public void handle(DisplayAgentContainer container, Agent agent, String message) {
        //Agent isn't created yet, so we can't use it client is null
        //So we get the agent from the server
        final Runnable runnable = () -> {
            Main.sendMessage(agent,"AGENT CLEARED");
            try{
                container.getDisplay(agent).end();
            }catch (NullPointerException e){
                return;
            }
            container.removeAgent(agent);
            Main.server.removeAgent(agent);
            container.revalidate();
        };
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        try {
            container.createAgent(agent);
        } catch (IOException e) {
            e.printStackTrace();
            executor.schedule(runnable,750, TimeUnit.MILLISECONDS);
            return;
        }

        if(!key.equals(Main.SECURITY_KEY)){
            System.err.println("SECURITY BREACH: INVALID SECURITY KEY");
            Main.server.breach(agent);
            Main.sendMessage(agent,"INVALID SECURITY KEY");


            executor.schedule(runnable,750, TimeUnit.MILLISECONDS);
            return;
        }
        Main.sendMessage(agent,"Welcome "+agent.getSocket().getInetAddress().getHostAddress()+":"+agent.getSocket().getPort());
    }
}
