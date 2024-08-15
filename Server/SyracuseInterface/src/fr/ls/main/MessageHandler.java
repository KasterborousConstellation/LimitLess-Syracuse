package fr.ls.main;


import fr.ls.main.MSG.*;

public class MessageHandler {
    private static int parseThreadNumber(String e){
        return Integer.parseInt(e.substring(1));
    }
    private static MSGThreadDescriptor parseThreadDescriptor(String e){
        final String currentMessage = e.substring(1);
        final String[] parts = currentMessage.split(";");
        return new MSGThreadDescriptor(e,Integer.parseInt(parts[0]),parts[1]);
    }
    public static MSGFeedback parseFeedBack(String message){
        char t = getType(message);
        return switch (t) {
            case 'I' -> new MSGThreadOnline(message,parseThreadNumber(message));
            case 'E' -> new MSGEndOfThread(message,parseThreadNumber(message));
            case 'T' -> new MSGEndOfProcess(message);
            case 'F' -> new MSGErrorOnThread(message, parseThreadNumber(message));
            case 'A' -> new MSGAgentOnline(message);
            case 'L' -> new MSGNThreads(message,parseThreadNumber(message));
            case 'D' -> parseThreadDescriptor(message);
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
