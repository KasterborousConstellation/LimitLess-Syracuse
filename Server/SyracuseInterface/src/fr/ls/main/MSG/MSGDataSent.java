package fr.ls.main.MSG;

import fr.ls.main.Agent;
import fr.ls.main.DisplayAgentContainer;
import fr.ls.main.Main;
import fr.ls.main.files.FileManager;
import fr.ls.main.files.SData;

import java.math.BigInteger;
import java.util.ArrayList;

public class MSGDataSent extends MSGFeedback {
    private ArrayList<SData> data;
    private ArrayList<BigInteger> indexes;
    public MSGDataSent(String source,ArrayList<SData>data,ArrayList<BigInteger>indexes) {
        super(source);
        this.data = data;
        this.indexes = indexes;
    }

    @Override
    public void handle(DisplayAgentContainer container, Agent client, String message) {
        for(int i = 0; i<data.size();i++){
            FileManager.setData(indexes.get(i),data.get(i));
        }
        Main.sendMessage(client,"Data received");
    }
}
