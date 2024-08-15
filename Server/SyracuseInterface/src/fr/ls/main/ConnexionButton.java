package fr.ls.main;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConnexionButton extends JButton {
    private final ServerHandler handler;
    public ConnexionButton(String name,ServerHandler handler){
        super(name);
        this.handler = handler;
        addActionListener(
                (ActionEvent e)->{
                    handler.acceptNewClient();
                }
        );
    }
}
