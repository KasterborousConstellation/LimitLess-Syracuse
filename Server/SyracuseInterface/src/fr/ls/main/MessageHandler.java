package fr.ls.main;


import fr.ls.main.MSG.*;

import java.lang.reflect.InvocationTargetException;

public class MessageHandler {
    private static int parseThreadNumber(String e){
        return Integer.parseInt(e.substring(1));
    }
    private static MSGFeedback parseDoubleMessage(String e,Class<? extends MSGFeedback> c) {
        final String currentMessage = e.substring(1);
        final String[] parts = currentMessage.split(";");
        try {
            return (MSGFeedback) c.getConstructors()[0].newInstance(e, Integer.parseInt(parts[0]), parts[1]);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            return null;
        }
    }
    private static MSGFeedback parseSendingData(String message){
        final String current = message.substring(1,message.length()-1);
        return new MSGUnknown(message);
    }
    public static MSGFeedback parseFeedBack(String message){
        char t = getType(message);
        return switch (t) {
            case 'I' -> new MSGThreadOnline(message,parseThreadNumber(message));
            case 'E' -> new MSGEndOfThread(message,parseThreadNumber(message));
            case 'T' -> new MSGEndOfProcess(message);
            case 'F' -> new MSGErrorOnThread(message, parseThreadNumber(message));
            case 'A' -> parseDoubleMessage(message,MSGAgentOnline.class);
            case 'L' -> new MSGNThreads(message,parseThreadNumber(message));
            case 'D' -> parseDoubleMessage(message,MSGThreadDescriptor.class);
            case 'S' -> parseSendingData(message);
            default -> new MSGUnknown(message);
        };
    }
    private static char getType(String message){
        assert(message.length()>0);
        return message.charAt(0);
    }
    public static String Identifier(Agent agent){
        return "[A|"+agent.getUsername()+"]";
    }
}
