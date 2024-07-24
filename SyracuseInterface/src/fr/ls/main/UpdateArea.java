package fr.ls.main;

import javax.swing.*;

public class UpdateArea extends JTextArea {
    public void send(String line){
        this.append(line);
    }
    public UpdateArea(int a,int b){
        super(a,b);
    }
}
