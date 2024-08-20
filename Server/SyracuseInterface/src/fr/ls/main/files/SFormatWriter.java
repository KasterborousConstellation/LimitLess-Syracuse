package fr.ls.main.files;

import java.io.File;

public class SFormatWriter {
    private final StringBuilder builder;
    private byte parse = 0;
    private char current=0;
    public SFormatWriter(){
        builder = new StringBuilder();
    }
    public void feed(SData data){
        if(data == null){
            feed(CharacterSet.U_EMPTY);
            feed(CharacterSet.U_END_OF_LINE);
            return;
        }
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
            builder.append((current));
            current = 0;
            parse = 0;
        }
    }
    public void flush(){
        if(parse!=0){
            builder.append(current);
        }
    }
    public void write(File file){
        FileUtils.write(file,builder.toString());
    }
}
