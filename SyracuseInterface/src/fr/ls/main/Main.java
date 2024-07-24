package fr.ls.main;
import javax.swing.*;
import java.awt.*;
import java.net.ConnectException;

public class Main {
    private static int height = 720;
    private static int width =(int) ((float)height *16f/9f);

    private static boolean finished = false;
    public static void main(String[] args) {
        ClientHandler client = new ClientHandler(args[0], Integer.parseInt(args[1]));
        int margin = 10;
        int num_threads = Integer.parseInt(args[2]);
        int textWidth = (int)((float)width / 6.0F);
        int textHeight = (int)((float)height * 2.0F / 3.0F);
        int threadContainerWidth = (int)((float)width / 3.0F) - 2 * margin;
        int threadContainerHeight = textHeight - 2 * margin;
        JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setName("KTBS-LS");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final UpdateArea area = new UpdateArea(10, 10);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setPreferredSize(new Dimension(textWidth, height));
        final JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBounds(width - textWidth - margin, 0, textWidth, textHeight);
        frame.add(scrollPane, "Center");
        final ThreadContainer mainPane = new ThreadContainer(num_threads);
        for(int i = 0; i < num_threads; ++i) {
            final ThreadPanel subPane = new ThreadPanel(i);
            mainPane.add(subPane);
        }
        mainPane.setBounds(margin, margin, threadContainerWidth, threadContainerHeight);
        frame.add(mainPane);
        frame.setVisible(true);

        client.setUp();
        area.send("CLIENT ONLINE");
    }
}