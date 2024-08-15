package fr.ls.main.files;

import java.util.ArrayList;

public class SDataBuilder {
    private final ArrayList<SData> data;
    private boolean mode = false;
    private String entry = "";
    private String key = "";

    public SDataBuilder(){
        data = new ArrayList<>();
    }
    public void feed(char c){
        final char nc = (char) (c - FileManager.ENCODING_OFFSET);
        final short first = (short) (nc & 0x000F);
        final short second = (short) ((nc & 0x00F0) >> 4);
        final short third = (short) ((nc & 0x0F00) >> 8);
        final short fourth = (short) ((nc & 0xF000) >> 12);
        feed(CharacterSet.getFromValue(first));
        feed(CharacterSet.getFromValue(second));
        feed(CharacterSet.getFromValue(third));
        feed(CharacterSet.getFromValue(fourth));
    }
    private void feed(CharacterSet c){
        if(!mode){
            if(c==CharacterSet.U_END_OF_LINE||c==CharacterSet.U_ERROR){
                System.err.println("ERROR: INVALID DATA");
                return;
            }
            if(c==CharacterSet.U_SEPARATOR){
                mode = true;
                return;
            }
            entry+=c.getCharacter();
        }else{
            if(c==CharacterSet.U_SEPARATOR){
                System.err.println("ERROR: INVALID DATA");
                return;
            }
            if(c==CharacterSet.U_END_OF_LINE){
                data.add(new SData(key,entry));
                entry = "";
                key = "";
                mode = false;
                return;
            }
            key+=c.getCharacter();
        }
    }
    public ArrayList<SData> retrieve(){
        return data;
    }
}
