package fr.ls.main.MSG;
public abstract class MSGFeedback {
    private final String source;
    private final long time;
    public MSGFeedback(String source){
        this.source=source;
        this.time = System.nanoTime();
    }
    public String getSource(){
        return source;
    }
    public long getTime(){
        return time;
    }
}