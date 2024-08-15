package fr.ls.main;

import java.awt.*;

public enum ThreadState {
    OFFLINE("OFFLINE", Color.RED),
    ONLINE("ONLINE", Color.GREEN),
    TERMINATED("TERMINATED", Color.CYAN),
    ERROR("ERROR", Color.ORANGE)
    ;
    private final String message;
    private final Color color;
    ThreadState(String message,Color color){
        this.message = message;
        this.color = color;
    }
    public String getMessage(){
        return message;
    }
    public Color getColor(){
        return color;
    }
}
