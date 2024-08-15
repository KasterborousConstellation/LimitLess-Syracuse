package fr.ls.main.files;

public record SData(String entry, String value) {
    public String getEntry() {
        return entry;
    }
    public String getValue() {
        return value;
    }
}
