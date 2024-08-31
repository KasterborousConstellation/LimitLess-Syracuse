package fr.ls.main;

import fr.ls.main.Main;
import fr.ls.main.ThreadContainer;
import fr.ls.main.ThreadPanel;

import javax.swing.*;
import java.awt.*;

public class AgentContainer extends JPanel {
    private final Agent agent;
    public Agent getAgent(){
        return agent;
    }
    private ThreadContainer container;
    public AgentContainer(Agent agent){
        this.agent = agent;
    }
    public void launch(){
        JLabel label = new JLabel(agent.getName()+"");
        label.setVerticalAlignment(SwingConstants.TOP);
        add(label);
        final ThreadContainer mainPane = new ThreadContainer(agent.getThreads());
        this.container = mainPane;
        for(int i = 0; i < agent.getThreads(); ++i) {
            final ThreadPanel subPane = new ThreadPanel(i);
            mainPane.add(subPane);
        }
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(mainPane);
        this.setVisible(true);
        setBounds(UIHandler.margin,UIHandler.margin,UIHandler.threadContainerWidth,UIHandler.threadContainerHeight+5*UIHandler.margin);
        setBackground(Color.LIGHT_GRAY);
    }

    public void end(){
        this.container.removeAll();
    }
    public ThreadContainer getThreadContainer(){
        return container;
    }
    public ThreadPanel getPanel(int i){
        return container.getPanel(i);
    }
}
