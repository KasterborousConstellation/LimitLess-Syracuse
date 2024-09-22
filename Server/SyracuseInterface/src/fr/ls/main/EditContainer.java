package fr.ls.main;

import fr.ls.main.files.FileManager;
import fr.ls.main.files.SData;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;

public class EditContainer extends JPanel {
    private final JTextField targetArea;
    private final JTextField valueArea;
    public EditContainer(){
        super();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        targetArea = new JTextField();
        valueArea = new JTextField();
        targetArea.setMaximumSize(new Dimension(200,20));
        valueArea.setMaximumSize(new Dimension(200,20));
        JButton sendButton = new JButton("Send");
        JButton clearButton = new JButton("Clear");
        JButton getButton = new JButton("Get");
        add(targetArea);
        add(valueArea);
        add(getButton);
        add(sendButton);
        add(clearButton);
        clearButton.addActionListener((e)->{
            targetArea.setText("");
            valueArea.setText("");
        });
        sendButton.addActionListener((e)-> {
            final String target = targetArea.getText();
            final String value = valueArea.getText();
            if (target.isEmpty() || value.isEmpty()||!target.matches("^[0-9]*$")||!value.matches("^[0-9]*$")) {
                return;
            }
            BigInteger index = new BigInteger(target);
            FileManager.setData(index,new SData(value));
            targetArea.setText("");
            valueArea.setText("");
        });
        getButton.addActionListener((e)->{
            final String target = targetArea.getText();
            if (target.isEmpty()||!target.matches("^[0-9]*$")) {
                System.out.println("Invalid input");
                return;
            }
            BigInteger index = new BigInteger(target);
            SData data = FileManager.getData(index);
            String value = data!=null?data.getValue():"#";
            Main.area_glb.send("READ: "+index+" value: "+value);
        });
        setVisible(true);
        setBackground(Color.DARK_GRAY);
    }

}
