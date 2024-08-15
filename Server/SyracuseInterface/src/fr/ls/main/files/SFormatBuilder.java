package fr.ls.main.files;

public class SFormatBuilder {
    private final StringBuilder builder;
    private byte parse = 0;
    private char current=0;
    public SFormatBuilder(){
        builder = new StringBuilder();
    }
    public void feed(SData data){
        if(data == null){
            feed(CharacterSet.U_EMPTY);
            feed(CharacterSet.U_SEPARATOR);
            feed(CharacterSet.U_EMPTY);
            feed(CharacterSet.U_END_OF_LINE);
            return;
        }
        final String entry = data.getEntry();
        for(char c : entry.toCharArray()){
            final CharacterSet cs = CharacterSet.getFromCharacter(c);
            feed(cs);
        }
        feed(CharacterSet.U_SEPARATOR);
        final String value = data.getValue();
        for(char c : value.toCharArray()){
            final CharacterSet cs = CharacterSet.getFromCharacter(c);
            feed(cs);
        }
        feed(CharacterSet.U_END_OF_LINE);
    }
    private void feed(CharacterSet cs){
        char value = (char)cs.getValue();
        current =(char)( current | (value << 4 * parse));
        parse++;
        if(parse==4){
            builder.append((char)(current+FileManager.ENCODING_OFFSET));
            current = 0;
            parse = 0;
        }
    }
}
