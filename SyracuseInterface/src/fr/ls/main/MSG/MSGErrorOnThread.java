package MSG;

public class MSGErrorOnThread extends MSGFeedback{
    private final int thread;
    public MSGErrorOnThread(String source,int thread) {
        super(source);
        this.thread = thread;
    }
    public int getThread(){
        return thread;
    }
}
