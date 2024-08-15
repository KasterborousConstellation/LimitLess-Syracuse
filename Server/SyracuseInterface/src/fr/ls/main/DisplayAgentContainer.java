package fr.ls.main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DisplayAgentContainer extends JPanel {
    private final static GridLayout layout = new GridLayout(2,30);
    private final ArrayList<AgentContainer> containers = new ArrayList<>();
    public DisplayAgentContainer(){
        super(layout);
        setBounds(Main.margin,Main.margin,Main.threadContainerWidth,Main.threadContainerHeight+5*Main.margin);
        setVisible(true);
    }
    public int getConnexions(){
        return containers.size();
    }
    public void createAgent(Client client){
        final AgentContainer container = new AgentContainer(new Agent(client));
        this.containers.add(container);
        JScrollPane scrollPane = new JScrollPane(container);
        add(scrollPane);
    }
    public void removeAgent(Client client){
        final AgentContainer container = getDisplay(client);
        if(container != null){
            this.containers.remove(container);
            remove(container.getParent().getParent());
            revalidate();
            updateUI();
        }
    }
    public Agent getAgent(Client client){
        return getDisplay(client)!=null?getDisplay(client).getAgent():null;
    }
    public AgentContainer getDisplay(Client client){
        for(AgentContainer container: containers){
            if(client.readUUID().equals(container.getAgent().getClient().readUUID())){
                return container;
            }
        }
        return null;
    }
}
