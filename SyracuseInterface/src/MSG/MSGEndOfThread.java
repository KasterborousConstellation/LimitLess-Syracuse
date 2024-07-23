package MSG;

public class MSGEndOfThread extends MSGFeedback{
    private final int thread;
    public MSGEndOfThread(String source,int thread) {
        super(source);
        this.thread = thread;
    }
    public int getThread(){
        return thread;
    }
}