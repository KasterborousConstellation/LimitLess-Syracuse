package fr.ls.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;

public class Agent {
    private int threads =0;
    private final char name;
    private final UUID uuid;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public Agent(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        Random random = new Random();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        name = (char)random.nextInt(64,128);
        this.uuid = UUID.randomUUID();
    }
    public void setThreads(int threads){
        this.threads=threads;
    }
    public char getName(){
        return  name;
    }
    public String getUsername(){
        return name+"";
    }
    public int getThreads(){
        return threads;
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
