package fr.ls.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Client {
    private final UUID uuid;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public Client(Socket socket) throws IOException {
        clientSocket=socket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.uuid = UUID.randomUUID();
    }
    public Socket getSocket(){
        return clientSocket;
    }
    public void write(String message){
        out.println(message);
        out.flush();
    }
    public UUID readUUID(){
        return uuid;
    }
    public boolean canRead(){
        try{
            return in.ready();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public ReadRecord read(){
        try{
            String a = in.readLine();
            return new ReadRecord(a,this);
        }catch (IOException e){
            e.printStackTrace();
            return ReadRecord.errorRecord;
        }
    }
}
