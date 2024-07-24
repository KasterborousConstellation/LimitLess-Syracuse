package fr.ls.main.MSG;


public class MSGUnknown extends MSGFeedback{
    public MSGUnknown(String source) {
        super(source);
    }
    public void fault(){
        System.out.println(getSource());
    }
}
