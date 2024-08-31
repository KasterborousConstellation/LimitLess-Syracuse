package fr.ls.main;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
public class DisplayAgentContainer extends JPanel {
    private final static GridLayout layout = new GridLayout(3,30);
    private final ArrayList<AgentContainer> containers = new ArrayList<>();
    public DisplayAgentContainer(){
        super(layout);
        setBounds(UIHandler.margin,UIHandler.margin,UIHandler.threadContainerWidth,UIHandler.threadContainerHeight+5*UIHandler.margin);
        setVisible(true);
    }
    public int getConnexions(){
        return containers.size();
    }
    public void createAgent(Agent agent) throws IOException {
        final AgentContainer container = new AgentContainer(agent);
        this.containers.add(container);
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setPreferredSize(new Dimension(UIHandler.threadContainerWidth,UIHandler.threadContainerHeight));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //Limit the vertical scrolling up to N pixels
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