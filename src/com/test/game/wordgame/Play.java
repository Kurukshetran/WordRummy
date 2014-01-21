package com.test.game.wordgame;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Play extends Activity 
{
	DBHelper playerDB = new DBHelper(Play.this);
	
	Spinner sp;
    ArrayAdapter<String> adapter;
    
    String numbers[] = {"EASY", "MEDIUM", "HARD"};
    public String[] Friends;
    
    public int SP ;
    
	protected Button selectFriendsButton;
	protected Button Save_Button, System_Order, Manual_Order;
	
	protected ArrayList<String> selectedFriends = new ArrayList<String>();	
		
	private RadioButton radioThreePlayer;
	private RadioButton radioFourPlayer;
	private RadioButton radioFivePlayer;
	
	public static String[] NAME = new String[Data.numOfPlayers];

	public static String[] PASS = new String[Data.numOfPlayers];
	
	public static String[] path = new String[Data.numOfPlayers + 1];
	
	public static String[] Player_ID = new String[Data.numOfPlayers];
	
	public int lastNP = 0;
	
	public boolean Check = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    Utils.onActivityCreateSetTheme(this);
	    setContentView(R.layout.game_settings);	
	    
	    System_Order = (Button) findViewById(R.id.sysorder);
	    Manual_Order = (Button) findViewById(R.id.manorder);
	  
	    Data.GameReset();
	    
	    DataBaseRead();
	    
	    spinnerSelection();
	    
	    radioButtonSeletion();
	  	 
	    onChangeSelectedPlayers();   
	    
	    selectFriendsButton.setOnClickListener(new View.OnClickListener() 
        {          
           public void onClick(View v) 
           {
        	   showSelectFriendsDialog();        	   
           }
        });   
	    
	    
	    if(Data.numOfPlayers == 2)
	    {
	    	Manual_Order.setEnabled(false);
	    }
	   
	    System_Order.setOnClickListener(new View.OnClickListener() 
        {         
           public void onClick(View v) 
           {       
        	   Check = false;
        	   Manual_Order.setEnabled(false);
           }			
        });
	    
	    Manual_Order.setOnClickListener(new View.OnClickListener() 
        {         
           public void onClick(View v) 
           {        
        	   if(SP == Data.numOfPlayers)
	  	        {
	    	    	Data.shuffledDecks = Data.random();
	    	    	path[Data.numOfPlayers] = "/mnt/sdcard/ShuffledWords/MyCameraApp/dummy_bg.png";
		        	GetLoginContent();
		        	initialForScore();
		        	System_Order.setEnabled(false);
		        	Check = true;
		    		Intent i = new Intent(Play.this, Manual_Shuffled.class);
		    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		   			startActivity(i);
	  	        }
	    	    else
	            {        		 
	    	    	Toast.makeText(getApplicationContext(), "Please check Players are correctly choosed", Toast.LENGTH_LONG).show();
	            }
           }			
        });
	  
	    Save_Button.setOnClickListener(new View.OnClickListener() 
        {         
           public void onClick(View v) 
           { 	   
        	 if(SP == Data.numOfPlayers)
        	 {   
        		 if(Check == true)
        		 {
        			 Data.shuffledDecks = Data.random();
    	    		 
        			 Data.name = Manual_Shuffled.NAME;
    				 Data.pass = Manual_Shuffled.PASS;
    				 Data.path = Manual_Shuffled.PATH;
    				 Data.Player_ID = Manual_Shuffled.ID;
    				 
    	    		 //Data.CPSuffled();
    	    		 //GetLoginContent();
    				 initialForScore();
    	    		 Intent i1 = new Intent(Play.this, Login.class);
    	    		 i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	   			 startActivity(i1);        			 
        		 }
        		 
        		 if(Check == false)
        		 {
        			 Data.shuffledDecks = Data.random();
    	    		 initialForScore();    				 
    	    		 Data.CPSuffled();
    	    		 GetLoginContent();
    	    		 Intent i1 = new Intent(Play.this, Login.class);
    	    		 i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	   			 startActivity(i1);   
        		 }      		
        	 }
        	 else
        	 {        		 
  			   Toast.makeText(getApplicationContext(), "Please check Players are correctly choosed", Toast.LENGTH_LONG).show();
        	 }       
           }			
       });
	}

	private void setRadioButtonEnable()
	{
	   switch(lastNP)
	   {
		   case 2:
			   radioThreePlayer.setChecked(true);
			   Data.numOfPlayers = 2;
			   break;
		   case 3:
			   radioFourPlayer.setChecked(true);
			   Data.numOfPlayers = 3;
			   break;
		   case 4:
			   radioFivePlayer.setChecked(true);
			   Data.numOfPlayers = 4;
			   break;
	   }		
	}

	private void DataBaseRead() 
	{
		SQLiteDatabase db = playerDB.getReadableDatabase();
        
		int k = 0;		
		
		try
		{		
			Cursor cursor1 = db.rawQuery("SELECT player_username FROM PlayerData WHERE player_status = '1'", null);
			Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.DATABASE_TABLE_NAME_USERINFO, null);
			
			Friends = new String[cursor.getCount()];
			
			if(cursor1 != null)
			{				
				System.out.println("database showing");
				startManagingCursor(cursor1);
				while(cursor1.moveToNext())
				{
					selectedFriends.add(cursor1.getString(0));
					lastNP++;
				}
				while(cursor.moveToNext())
				{
				    String name = cursor.getString(cursor.getColumnIndex("player_username"));
				    Friends[k] = name;
				    k++;
				}
			}
			cursor1.close();
			cursor.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		db.close();
	}

	public int CheckDatabase ()
	{
		int chk = 0;
				
		SQLiteDatabase db = playerDB.getReadableDatabase();
		
		Cursor mCursor = db.rawQuery("SELECT * FROM " + DBHelper.DATABASE_TABLE_NAME_USERINFO, null);
		
		while(mCursor.moveToNext())
		{
		   // Not Empty
			chk++;
		} 
		mCursor.close();
		db.close();
		
		return chk;		
	}	
	
	public void GetLoginContent() 
	{			
		SQLiteDatabase db = playerDB.getReadableDatabase();
	
		try
		{							  	
			Cursor cursor = db.rawQuery("SELECT _id, player_photo, player_username, player_pass, player_status" +" FROM " + DBHelper.DATABASE_TABLE_NAME_USERINFO 	+ " WHERE player_status = '1' ",null);
		
			if(cursor != null)
			{
				System.out.println("database showing");
				startManagingCursor(cursor);
				LoginProcessor(cursor);
			}
			cursor.close();
			System.out.println("Cursor NuLL");			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		db.close();
	}
		
	public void LoginProcessor(Cursor cursor) 
	{
		int i = 0;
		
		while (cursor.moveToNext()) 
		{	      		
			Data.Player_ID[i] = cursor.getInt(0);
			Data.path[i] = cursor.getString(1);
		    Data.name[i] = cursor.getString(2);	
		    Data.pass[i]= cursor.getString(3);	
			
		    Player_ID[i] = cursor.getString(0);
		    path[i] = cursor.getString(1);
		    NAME[i] = cursor.getString(2);
		    PASS[i] = cursor.getString(3);
	        i++;  
		}
	}
	
	public void showSelectFriendsDialog() 
	{
		final SQLiteDatabase db = playerDB.getReadableDatabase();
		
		final boolean[] checkedFriends = new boolean[Friends.length];
		int count = Friends.length;		
		
		for(int i = 0; i < count; i++)
			checkedFriends[i] = selectedFriends.contains(Friends[i]);

		DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() 
		{
			int idPlayer;
			String sql ;		
		
			public void onClick(DialogInterface dialog, int which, boolean isChecked) 
			{				
				if(isChecked)
				{
					selectedFriends.add(Friends[which]);
					
					idPlayer = which + 1;
					sql="update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_status = '1' where _id=";
					db.execSQL(sql + idPlayer);
					onChangeSelectedPlayers();
				}				
				else
				{
					selectedFriends.remove(Friends[which]);
					
					idPlayer = which + 1;
					sql="update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_status = '0' where _id=";
					db.execSQL(sql + idPlayer);
					onChangeSelectedPlayers();
				}
				
				if(selectedFriends.size() != Data.numOfPlayers)
				{
					 Toast.makeText(getApplicationContext(), "pls select"+" "+Data.numOfPlayers+" "+"players", Toast.LENGTH_SHORT).show();
					 ((Dialog) dialog).setCancelable(false);
				}
				else if(selectedFriends.size() == Data.numOfPlayers)
				{
					((Dialog) dialog).setCancelable(true);
				}
			}			
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	
		builder.setTitle("Select Friends");
		builder.setMultiChoiceItems(Friends, checkedFriends, coloursDialogListener);
		
		AlertDialog dialog = builder.create();
	
		dialog.show();		
		
		if(selectedFriends.size() != Data.numOfPlayers)
		{
			 ((Dialog) dialog).setCancelable(false);
		}
		else if(selectedFriends.size() == Data.numOfPlayers)
		{
			((Dialog) dialog).setCancelable(true);
		}
	}
	
	public static void initialForScore()
	{
		for(int i = 0; i < Data.numOfPlayers; i++)
		{
			Timer.Total_score[i] = 1000;
		}
	}	
	
	protected void onChangeSelectedPlayers() 
	{
		StringBuilder stringBuilder = new StringBuilder();
		int i = 0;
		
		for(CharSequence user : selectedFriends)
		{
			if(i == 0)
			{
				stringBuilder.append(user);
			}			
			else if(i == (selectedFriends.size() - 1))
			{
				stringBuilder.append(" "+"&"+" "+user);
			}
			else
			{
				stringBuilder.append(","+user);
			}
			i++;
		}
		selectFriendsButton.setText(stringBuilder.toString());
		SP = selectedFriends.size();	
	}

	public void spinnerSelection() 
	{
        sp = (Spinner) findViewById(R.id.spinner1);
	    
	    List<String> Players = new ArrayList<String>();
        Players.add("EASY");
        Players.add("MEDIUM");
        Players.add("HARD");
     
        ArrayAdapter<String> aspnPlayers = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Players);
        aspnPlayers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(aspnPlayers);
        
        sp.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
             public void onNothingSelected(AdapterView<?> arg0) 
             { 
            	 Data.setOfCards =  3;
               	 Data.numOfdecks =  3;
             }
             
             public void onItemSelected(AdapterView<?> parent, View v, int position, long id) 
             {
            	 Data.setOfCards = position + 3;
            	 Data.numOfdecks =  position + 3; 
             }
        });			
	}
	
	private void radioButtonSeletion() 
	{
	    radioThreePlayer = (RadioButton) findViewById(R.id.radioplayThree);
	    radioFourPlayer = (RadioButton) findViewById(R.id.radioplayFour);
	    radioFivePlayer = (RadioButton) findViewById(R.id.radioplayFive);
	    
	    selectFriendsButton = (Button) findViewById(R.id.showFriends);
	    Save_Button = (Button) findViewById(R.id.play);
	
	    setRadioButtonEnable();
       
	   
	    radioThreePlayer.setOnClickListener(new View.OnClickListener()
	    {			
			public void onClick(View arg0) 
			{
				if(CheckDatabase() < 2)
    			{
    				selectFriendsButton.setEnabled(false);
    				Toast.makeText(getApplicationContext(), "Currently no registered players.Please Register atleast 2 Players", Toast.LENGTH_LONG).show();
    			}        			
    			else
    			{
    				Data.numOfPlayers = 2;
    				Manual_Order.setEnabled(false);
    				Manual_Order.setClickable(false);    				
    				selectFriendsButton.setEnabled(true);
    				showSelectFriendsDialog();
    			}        			
			}
		});
    
	    radioFourPlayer.setOnClickListener(new View.OnClickListener()
	    {				
			public void onClick(View arg0) 
			{
				if(CheckDatabase() < 3)
    			{
    				selectFriendsButton.setEnabled(false);
    				Toast.makeText(getApplicationContext(), "Currently no registered players.Please Register atleast 3 Players", Toast.LENGTH_SHORT).show();
    			}
    			else
    			{
    				Manual_Order.setEnabled(true);
    				Manual_Order.setClickable(true);
    				Data.numOfPlayers = 3;
    				selectFriendsButton.setEnabled(true);
    				showSelectFriendsDialog();
    			}        			
			}
		});
	    
	    radioFivePlayer.setOnClickListener(new View.OnClickListener()
	    {				
			public void onClick(View arg0) 
			{
				if(CheckDatabase() < 4)
    			{
    				selectFriendsButton.setEnabled(false);
    				Toast.makeText(getApplicationContext(), "Currently no registered players.Please Register atleast 3 Players", Toast.LENGTH_LONG).show();
    			}
    			else
    			{
    				Manual_Order.setEnabled(true);
    				Manual_Order.setClickable(true);
    				Data.numOfPlayers = 4;
    				selectFriendsButton.setEnabled(true);
    				showSelectFriendsDialog();
    			}        			
			}
		});	    
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		playerDB.close();
	}	
}