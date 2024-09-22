package fr.ls.main;

import javax.swing.*;

public class UpdateArea extends JTextArea {
    public void send(String line){
        if(this.getText().length()>1000){
            this.setText("");
        }
        this.append(line+"\n");
    }
    public UpdateArea(int a,int b){
        super(a,b);
    }
}
