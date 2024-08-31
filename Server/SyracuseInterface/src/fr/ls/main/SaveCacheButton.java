package fr.ls.main;

import fr.ls.main.files.FileManager;

import javax.swing.*;

public class SaveCacheButton extends JButton {
    public SaveCacheButton(String text) {
        super(text);
        addActionListener(e -> FileManager.fileManagerCache.saveCache());
    }
}
