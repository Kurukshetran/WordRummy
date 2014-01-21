package com.test.game.wordgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class Save
{	
	String DECKS;
    int NUM_OF_DECKS;
    int NUM_OF_CARDS;
    int SET_OF_CARDS;
    int NUM_OF_PLAYERS;
    int OPENCARD;
    int CLOSECARD;
    int CURRENT_PLAYER;
    int ACTIVITY_ID;
    
    int[] C_TIME;
    int[] T_TIME;
    int[] ROUNDS;
    int[] ID;
    int[] WRONG_DEC_PLAYER;
	
    String[] U_NAME;
    String[] U_PASSWORD;
    String[] U_IMAGE_PATH;
    
	public String SerializeGameData(int ACTIVITY_ID, String TAG)
	{
		String path = "";
		File direct = new File(Environment.getExternalStorageDirectory() + "/ShuffledWords");

	    if(!direct.exists())
	    {
	        if(direct.mkdir())
	        {
	        	//directory is created;
	        }
	    }
		   
		ResumePackage rp = new ResumePackage();
	
		rp.DECKS = Data.shuffledDecks;
		rp.NUM_OF_DECKS = Data.numOfdecks;
		rp.NUM_OF_CARDS = Data.numOfCards;
		rp.SET_OF_CARDS = Data.setOfCards;
		rp.NUM_OF_PLAYERS = Data.numOfPlayers;
		rp.OPENCARD = Data.OpenCard;
		rp.CLOSECARD = Data.CloseCard;
		rp.CURRENT_PLAYER = Data.currentPlayer;
		rp.ACTIVITY_ID = ACTIVITY_ID;		
		
		rp.PLAYERS_NAME = Data.name;
		rp.PLAYERS_PASSWORD = Data.pass;
		rp.PLAYERS_IMAGE = Data.path;
		
		rp.C_TIME = Timer.currentTime;
		rp.T_TIME = Timer.cummulativeTime;
		rp.ROUNDS = Timer.rounds;
		rp.ID = Data.Player_ID;
		rp.WRONG_DECLARE_PLAYER = WrongDeclare.WrongDeclarePlayer;
		
		Log.v("ID[0]", String.valueOf(rp.ID[0]));
	    Log.v("ID[1]", String.valueOf(rp.ID[1]));
	    
	    Log.v("NAME[0]", String.valueOf(rp.PLAYERS_NAME[0]));
	    Log.v("NAME[1]", String.valueOf(rp.PLAYERS_NAME[1]));
	    
	    Log.v("PASSWORD[0]", String.valueOf(rp.PLAYERS_PASSWORD[0]));
	    Log.v("PASSWORD[1]", String.valueOf(rp.PLAYERS_PASSWORD[1]));
	    
	    Log.v("PLAYERS_IMAGE[0]", String.valueOf(rp.PLAYERS_IMAGE[0]));
	    Log.v("PLAYERS_IMAGE[1]", String.valueOf(rp.PLAYERS_IMAGE[1]));
		
		try
	    {
			path = Environment.getExternalStorageDirectory() +"/"+"ShuffledWords"+"/"+TAG+".txt";
	        FileOutputStream fileOut = new FileOutputStream(path);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(rp);
	        out.close();
	        fileOut.close();
	    }
		catch(IOException i)
	    {
			i.printStackTrace();
	    }
		
		return path;
	} 
	
	public void DeserializeGameData(String FILE_PATH)
	{
		ResumePackage RP = null;
		String path = FILE_PATH;
		
	    try
	    {
		    FileInputStream fileIn = new FileInputStream(path);
		    ObjectInputStream in = new ObjectInputStream(fileIn);
		    RP = (ResumePackage) in.readObject();
		    in.close();
		    fileIn.close();
	    }
	    catch(IOException i)
	    {
		    i.printStackTrace();
		    return;
	    }
	    catch(ClassNotFoundException c)
	    {
	    	Log.v("ERROR", "ResumePackage not found");
		    c.printStackTrace();
		    return;
	    }
	    
	    DECKS = RP.DECKS;
	    NUM_OF_DECKS = RP.NUM_OF_DECKS;
	    NUM_OF_CARDS = RP.NUM_OF_CARDS;
	    SET_OF_CARDS = RP.SET_OF_CARDS;
	    NUM_OF_PLAYERS = RP.NUM_OF_PLAYERS;
	    OPENCARD = RP.OPENCARD;
	    CLOSECARD = RP.CLOSECARD;
	    CURRENT_PLAYER = RP.CURRENT_PLAYER;
	    ACTIVITY_ID = RP.ACTIVITY_ID;	  		    
	    
	    U_NAME = RP.PLAYERS_NAME;
	    U_PASSWORD = RP.PLAYERS_PASSWORD;
	    U_IMAGE_PATH = RP.PLAYERS_IMAGE;
	    
	    C_TIME = RP.C_TIME;
	    T_TIME = RP.T_TIME;
	    ROUNDS = RP.ROUNDS;
	    ID = RP.ID;
	    WRONG_DEC_PLAYER = RP.WRONG_DECLARE_PLAYER;
	    
	    
	    Log.v("FILE_PATH", path);
	    Log.v("NOTICE", "Deserialized Package");
	    Log.v("DECKS", DECKS);
	    Log.v("NUM_OF_DECKS", String.valueOf(NUM_OF_DECKS));
	    Log.v("NUM_OF_CARDS", String.valueOf(NUM_OF_CARDS));
	    Log.v("SET_OF_CARDS", String.valueOf(SET_OF_CARDS));
	    Log.v("NUM_OF_PLAYERS", String.valueOf(NUM_OF_PLAYERS));
	    Log.v("OPENCARD", String.valueOf(OPENCARD));
	    Log.v("CLOSECARD", String.valueOf(CLOSECARD));
	    Log.v("CURRENT_PLAYER", String.valueOf(CURRENT_PLAYER));
	    Log.v("ACTIVITY_ID", String.valueOf(ACTIVITY_ID));
	    
	    Log.v("C_TIME[0]", String.valueOf(C_TIME[0]));
	    Log.v("C_TIME[1]", String.valueOf(C_TIME[1]));
	    Log.v("T_TIME[0]", String.valueOf(T_TIME[0]));
	    Log.v("T_TIME[1]", String.valueOf(T_TIME[1]));
	    Log.v("ROUNDS[0]", String.valueOf(ROUNDS[0]));
	    Log.v("ROUNDS[1]", String.valueOf(ROUNDS[1]));
	    
	    Log.v("ID[0]", String.valueOf(ID[0]));
	    Log.v("ID[1]", String.valueOf(ID[1]));
	    
	    Log.v("NAME[0]", String.valueOf(U_NAME[0]));
	    Log.v("NAME[1]", String.valueOf(U_NAME[1]));
	}	
}