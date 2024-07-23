import MSG.MSGFeedback;

public class Main {
    private static boolean finished = false;
    public static void main(String[] args) {
        final ClientHandler client = new ClientHandler(args[0],Integer.parseInt(args[1]));
        while(!finished){
            if(client.canRead()){
                final String message = client.read();
                final MSGFeedback feedback = MessageHandler.parseFeedBack(message);
            }
        }
    }
}