package fr.ls.main.MSG;


import fr.ls.main.Client;
import fr.ls.main.DisplayAgentContainer;

public class MSGUnknown extends MSGFeedback{
    public MSGUnknown(String source) {
        super(source);
    }

    @Override
    public void handle(DisplayAgentContainer container, Client client, String message) {
        fault();
    }

    public void fault(){
        System.out.println(getSource());
    }
}
