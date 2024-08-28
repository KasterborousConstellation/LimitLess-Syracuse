package fr.ls.main.MSG;

import fr.ls.main.Agent;
import fr.ls.main.DisplayAgentContainer;

public abstract class MSGFeedback {
    private final String source;
    private final long time;
    public MSGFeedback(String source){
        this.source=source;
        this.time = System.nanoTime();
    }
    public String getSource(){
        return source;
    }
    public long getTime(){
        return time;
    }
    public abstract void handle(DisplayAgentContainer container, Agent client, String message);
    public void handleLate(DisplayAgentContainer container, Agent client, String message){

    }
}