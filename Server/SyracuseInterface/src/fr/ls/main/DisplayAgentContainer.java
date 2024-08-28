package fr.ls.main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
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
    public void createAgent(Agent agent) throws IOException {
        final AgentContainer container = new AgentContainer(agent);
        this.containers.add(container);
        JScrollPane scrollPane = new JScrollPane(container);
        add(scrollPane);
    }
    public void removeAgent(Agent client){
        final AgentContainer container = getDisplay(client);
        if(container != null){
            this.containers.remove(container);
            remove(container.getParent().getParent());
            revalidate();
            updateUI();
        }
    }
    public AgentContainer getDisplay(Agent agent){
        for(AgentContainer container: containers){
            if(agent.readUUID().equals(container.getAgent().readUUID())){
                return container;
            }
        }
        return null;
    }
}
