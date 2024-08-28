package fr.ls.main;

public record ReadRecord(String line,Agent agent) {
    public static ReadRecord errorRecord = new ReadRecord("",null);
}
