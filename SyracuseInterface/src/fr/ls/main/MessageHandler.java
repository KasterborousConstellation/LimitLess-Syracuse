package fr.ls.main;


import fr.ls.main.MSG.*;

public class MessageHandler {
    public static MSGFeedback parseFeedBack(String message){
        char t = getType(message);
        return switch (t) {
            case 'E' -> new MSGEndOfThread(message, Integer.parseInt(message.substring(1)));
            case 'T' -> new MSGEndOfProcess(message);
            case 'F' -> new MSGErrorOnThread(message, Integer.parseInt(message.substring(1)));
            default -> new MSGUnknown(message);
        };
    }
    private static char getType(String message){
        assert(message.length()>0);
        return message.charAt(0);
    }
}
