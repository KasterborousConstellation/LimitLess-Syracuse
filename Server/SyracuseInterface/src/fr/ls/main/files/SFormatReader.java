package fr.ls.main.files;

import java.util.ArrayList;

public class SFormatReader {
    private final ArrayList<SData> data;
    private String entry = "";

    public SFormatReader(){
        data = new ArrayList<>();
    }
    public void feed(char c){
        final short first = (short) (c & 0x000F);

        final short second = (short) ((c & 0x00F0) >> 4);
        //System.out.println(second);
        final short third = (short) ((c & 0x0F00) >> 8);
        //System.out.println(third);
        final short fourth = (short) ((c & 0xF000) >> 12);
        //System.out.println(fourth);
        feed(CharacterSet.getFromValue(first));
        feed(CharacterSet.getFromValue(second));
        feed(CharacterSet.getFromValue(third));
        feed(CharacterSet.getFromValue(fourth));
    }
    private void feed(CharacterSet c){
        if(c==CharacterSet.U_END_OF_LINE){
            data.add(new SData(entry));
            entry = "";

        }else{
            entry+=c.getCharacter();
        }
    }
    public ArrayList<SData> retrieve(){
        return data;
    }
}
