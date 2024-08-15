package fr.ls.main;

import com.sun.nio.sctp.SctpSocketOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerHandler {
    private ServerSocket serverSocket;
    private final ConcurrentLinkedQueue<Client> clients = new ConcurrentLinkedQueue<>();
    private int port;
    private String local_adress;
    public ServerHandler(String address, int port) throws IOException {
        local_adress = address;
        this.port = port;
        serverSocket = new ServerSocket(port,1);
        serverSocket.setSoTimeout(10000);
        System.out.println("Online: "+serverSocket.getInetAddress().getHostName()+" Port: "+serverSocket.getLocalPort());
    }
    public int getClientsNumber(){
        return clients.size();
    }
    public void acceptNewClient(){
        try{
            serverSocket.setSoTimeout(10_000);
            final Socket socket = serverSocket.accept();
            final Client client = new Client(socket);
            clients.add(client);
            Main.area_glb.send("A new Client has been initialized "+client.getSocket().getInetAddress().getHostAddress());
            client.write("Server Greetings");
        }catch(SocketTimeoutException ti){
            Main.area_glb.send("Connection timeout");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void removeClient(Client client){
        clients.remove(client);
    }
    public ReadRecord read(){
        for(Client client: clients){
            if(client.canRead()){
                return client.read();
            }
        }
        return ReadRecord.errorRecord;
    }
    public void breach(Client client){
        try {
            client.getSocket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
