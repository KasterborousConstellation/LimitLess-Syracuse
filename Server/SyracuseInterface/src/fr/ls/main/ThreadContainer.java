package fr.ls.main;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class ThreadContainer extends JPanel {
    public int threads;
    private final ArrayList<ThreadPanel> panels=new ArrayList<>();
    public void add(ThreadPanel panel){
        super.add(panel);
        this.panels.add(panel);
    }
    public ThreadContainer(int num_threads){
        super(new GridLayout(num_threads / 3 + 1, 3, 10, 10));
        Dimension dimension = new Dimension(Main.threadContainerWidth,Main.threadContainerHeight);
        setSize(dimension);
        setMaximumSize(dimension);
        this.threads=num_threads;
    }
    public ThreadPanel getPanel(int i){
        return panels.get(i);
    }
}