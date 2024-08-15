package fr.ls.main;

public record ReadRecord(String line,Client client) {
    public static ReadRecord errorRecord = new ReadRecord("",null);
}
