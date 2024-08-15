package fr.ls.main;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConnexionButton extends JButton {
    public ConnexionButton(String name,ServerHandler handler){
        super(name);
        addActionListener(
                (ActionEvent e)->{
                    handler.acceptNewClient();
                }
        );
    }
}
