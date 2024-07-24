package fr.ls.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientHandler {
    private final String host;
    private final int port;
    private Socket socket = null;
    private BufferedReader inStream = null;
    public ClientHandler(String address,int port){
        host = address;
        this.port = port;
    }
    public void setUp()
    {
        try
        {
            socket = new Socket(host, port);
            inStream = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        } catch(UnknownHostException e) {
            System.err.println("Cannot find host called: " + host);
            e.printStackTrace();
            System.exit(-1);
        } catch(IOException e) {
            System.err.println("Could not establish connection for " + host);
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public boolean canRead() {
        try {
            return inStream.ready();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public String read() {
        if (socket != null && inStream != null) {
            try
            {
                return inStream.readLine();
            }
            catch(IOException e)
            {
                System.err.println("Conversation error with host " + host);
                e.printStackTrace();
            }
        }
        return "";
    }
    public void cleanUp(){
        try{
            if (inStream != null)
                inStream.close();
            if (socket != null)
                socket.close();
        } catch(IOException e) {
            System.err.println("Error in cleanup");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
