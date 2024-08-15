package fr.ls.main.MSG;

public abstract class ThreadedMSGFeedback extends MSGFeedback {
    private int thread;
    public ThreadedMSGFeedback(String source,int thread) {
        super(source);
        this.thread=thread;
    }
    public int getThread(){
        return thread;
    }
}
