package fr.ls.main.MSG;


import fr.ls.main.Agent;
import fr.ls.main.DisplayAgentContainer;

public class MSGUnknown extends MSGFeedback{
    public MSGUnknown(String source) {
        super(source);
    }

    @Override
    public void handle(DisplayAgentContainer container, Agent client, String message) {
        fault();
    }

    public void fault(){
        System.out.println(getSource());
    }
}
