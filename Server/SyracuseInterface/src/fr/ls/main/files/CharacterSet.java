package fr.ls.main.files;

public enum CharacterSet {
    U_EMPTY((short)0,'#'),
    U_1((short)1,'1'),
    U_2((short)2,'2'),
    U_3((short)3,'3'),
    U_4((short)4,'4'),
    U_5((short)5,'5'),
    U_6((short)6,'6'),
    U_7((short)7,'7'),
    U_8((short)8,'8'),
    U_9((short)9,'9'),
    U_0((short)10,'0'),
    U_END_OF_LINE((short) 11,'\n'),
    U_ERROR((short)12,'?')
    ;
    private short value;
    private char character;
    CharacterSet(short value, char character){
        this.value = value;
        this.character = character;
    }
    public short getValue() {
        return value;
    }
    public char getCharacter() {
        return character;
    }
    public static CharacterSet getFromValue(short value){
        for(CharacterSet c : values()){
            if(c.getValue()==value){
                return c;
            }
        }
        return U_ERROR;
    }
    public static CharacterSet getFromCharacter(char character){
        for(CharacterSet c : values()){
            if(c.getCharacter()==character){
                return c;
            }
        }
        return U_ERROR;
    }
}
