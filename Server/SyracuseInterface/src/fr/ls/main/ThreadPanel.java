package fr.ls.main;

import javax.swing.*;
import java.awt.*;

public class ThreadPanel extends JPanel {
    private static final GridLayout threadLayout =new GridLayout(3, 1, 10, 10);
    private static final Color threadColor = new Color(150, 150, 150);
    private ThreadState state;
    private final JLabel status;
    private final JLabel descriptor;
    private final int thread;
    public ThreadPanel(int i){
        super(threadLayout);
        this.thread=i;
        final JLabel title = new JLabel("Thread " + thread);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.status = new JLabel("?");
        this.descriptor = new JLabel("No information");
        setBackground(threadColor);
        setStatus(ThreadState.OFFLINE);
        setSize(UIHandler.threadContainerWidth/3,30);
        add(title);
        add(status);
        add(descriptor);
    }
    public int getThread(){
        return thread;
    }
    public ThreadState getStatus() {
        return state;
    }

    public void setStatus(ThreadState state) {
        String sts = state.getMessage();
        Color color = state.getColor();
        this.status.setText(sts);
        this.status.setForeground(color);
        updateUI();
    }
    public void setDescription(String str){
        this.descriptor.setText(str);
    }
}
