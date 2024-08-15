package fr.ls.main.MSG;

import fr.ls.main.Client;
import fr.ls.main.DisplayAgentContainer;

public class MSGThreadDescriptor extends ThreadedMSGFeedback{
    private final String description;
    public MSGThreadDescriptor(String source, int thread,String description){
        super(source, thread);
        this.description=description;
    }

    @Override
    public void handle(DisplayAgentContainer container, Client client, String message) {
        container.getDisplay(client).getPanel(getThread()).setDescription(description);
    }
}
