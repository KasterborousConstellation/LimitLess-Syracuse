package fr.ls.main;


import fr.ls.main.MSG.*;

public class MessageHandler {
    private static int parseThreadNumber(String e){
        return Integer.parseInt(e.substring(1));
    }
    public static MSGFeedback parseFeedBack(String message){
        char t = getType(message);
        return switch (t) {
            case 'I' -> new MSGThreadOnline(message,parseThreadNumber(message));
            case 'E' -> new MSGEndOfThread(message,parseThreadNumber(message));
            case 'T' -> new MSGEndOfProcess(message);
            case 'F' -> new MSGErrorOnThread(message, parseThreadNumber(message));
            default -> new MSGUnknown(message);
        };
    }
    private static char getType(String message){
        assert(message.length()>0);
        return message.charAt(0);
    }
}
