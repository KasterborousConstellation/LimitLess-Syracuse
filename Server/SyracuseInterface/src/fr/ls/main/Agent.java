package fr.ls.main;

import java.util.Random;

public class Agent {
    private final Client client;
    private int threads =0;
    private final char name;
    public Agent(Client client){
        this.client = client;
        Random random = new Random();
        name = (char)random.nextInt(64,128);
    }
    public Client getClient(){
        return client;
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
    
}
