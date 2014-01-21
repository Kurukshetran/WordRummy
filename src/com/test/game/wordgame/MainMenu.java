package com.test.game.wordgame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainMenu extends Activity implements OnClickListener
{
	DBHelper playerDB = new DBHelper(MainMenu.this);
	
	public String Resume_GameId;
	public String Resume_Players;
	public String Resume_Path;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.mainmenu);       
        
        CopyDB();        
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) 
        {
            boolean value = extras.getBoolean("AppStarted");
            
            if(value == true)
            {
            	ResumeNotification();
            }
        }
    
        View button1Click = findViewById(R.id.button1);
		button1Click.setOnClickListener(this);
		
		View button2Click = findViewById(R.id.button2);
		button2Click.setOnClickListener(this);
		
		View button3Click = findViewById(R.id.button3);
		button3Click.setOnClickListener(this);
		
		View button4Click = findViewById(R.id.button4);
		button4Click.setOnClickListener(this);
		
		View button5Click = findViewById(R.id.button5);
		button5Click.setOnClickListener(this);
    }
	
	public void CopyDB()
	{ 
        try 
        { 
        	playerDB.createDataBase();
        	CopyDefaultImageToSdcard();
 
        }
        catch (IOException ioe) 
        { 
        	throw new Error("Unable to create database"); 
        }
 
	 	try 
	 	{ 
	 		playerDB.openDataBase();	 
	 	}
	 	catch(SQLException sqle)
	 	{ 
	 		throw sqle; 
	 	}	    
	}
	
	private void CopyDefaultImageToSdcard()
	{
		File GameDir = new File(Environment.getExternalStoragePublicDirectory("ShuffledWords"), "MyCameraApp");

	    if (! GameDir.exists())
	    {
		    if (! GameDir.mkdirs())
		    {
		    	Log.v("MyCameraApp", "failed to create directory");
		    }
	    }
	     
		AssetManager assetManager = getAssets();
	    String[] files = null;
	    try 
	    {
	        files = assetManager.list("");
	    } 
	    catch (IOException e) 
	    {
	        Log.e("tag", e.getMessage());
	    }
	    for(String filename : files) 
	    {
	        InputStream in = null;
	        OutputStream out = null;
	        try 
	        {
	          in = assetManager.open(filename);
	          out = new FileOutputStream("/sdcard/ShuffledWords/MyCameraApp/" + filename);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } 
	        catch(Exception e) 
	        {
	            Log.e("tag", e.getMessage());
	        }       
	    }
	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException 
	{
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1)
	    {
	      out.write(buffer, 0, read);
	    }
	}
	
	private void ResumeNotification()
	{
		if(CheckDataBaseExit() == true)
		{
			if(CheckResumeTable() == true)
			{
				CheckForLastResumedGame();
				ShowResumeDialog();
			}
			else
			{
				//Do Nothing
			}
		}
		else
		{		
			//Do Nothing
		}
	}	
	
	private boolean CheckDataBaseExit()
	{
		String DB_PATH = null;
		String DATABASE_NAME = "PlayerInfo.db";
		
		DB_PATH="/data/data/"+getApplicationContext().getPackageName()+"/"+"databases/";
    	
    	SQLiteDatabase checkDB = null;
 
    	try
    	{
    		String myPath = DB_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}
    	catch(SQLiteException e)
    	{ 
    		//database does't exist yet. 
    	}
 
    	if(checkDB != null)
    	{ 
    		checkDB.close(); 
    	}
 
    	return checkDB != null ? true : false;
	}
	
	private boolean CheckResumeTable()
	{
		boolean chk;
				
		SQLiteDatabase db = playerDB.getReadableDatabase();
		
		Cursor mCursor = db.rawQuery("SELECT * FROM " + DBHelper.DATABASE_TABLE_NAME_RESUME, null);
		
		if(mCursor.moveToFirst())
		{
			chk = true; //Has Data
		}
		else
		{
			chk = false; //Has No Data
		}
		mCursor.close();
		db.close();
		
		return chk;		
	}
	
	public void CheckForLastResumedGame()
	{
		SQLiteDatabase db = playerDB.getReadableDatabase();
		
		try
		{							  	
			Cursor cursor = db.rawQuery("SELECT game_id, playername, file" +" FROM " + DBHelper.DATABASE_TABLE_NAME_RESUME, null);
		
			if(cursor != null)
			{
				System.out.println("database showing");
				startManagingCursor(cursor);
				
				//while (cursor.moveToLast()) 
				//{
				cursor.moveToLast();
					Resume_GameId = cursor.getString(0);
					Resume_Players = cursor.getString(1);
					Resume_Path = cursor.getString(2);
				//}
				
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
	
	private void ShowResumeDialog()
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Want to resume previous Game?");
        adb.setMessage("GameId is"+" "+Resume_GameId+"\n"+"Played by"+" "+Resume_Players);
        
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				Resume_Data(Resume_Path);
			}
		});
		adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		//adb.setIcon(R.drawable.icon);
		adb.show();
	}
	
	private void Resume_Data(String path)
	{
		Toast.makeText(getApplicationContext(), "Deserializing..", Toast.LENGTH_LONG).show();
	   	
	   	Save deseriliaze = new Save(); 	  			   	
	   	
	    deseriliaze.DeserializeGameData(path);
	    
	    Data.shuffledDecks = deseriliaze.DECKS;
		Data.numOfdecks = deseriliaze.NUM_OF_DECKS;
		Data.numOfCards = deseriliaze.NUM_OF_CARDS;
		Data.setOfCards = deseriliaze.SET_OF_CARDS;
		Data.words = 3 * deseriliaze.SET_OF_CARDS;
		Data.numOfPlayers = deseriliaze.NUM_OF_PLAYERS;
		Data.OpenCard = deseriliaze.OPENCARD;
		Data.CloseCard = deseriliaze.CLOSECARD;
		Data.currentPlayer = deseriliaze.CURRENT_PLAYER;
		Data.LOGIN_ACTIVITY = deseriliaze.ACTIVITY_ID;
		
		Timer.currentTime = deseriliaze.C_TIME;
		Timer.cummulativeTime = deseriliaze.T_TIME;
		Timer.rounds = deseriliaze.ROUNDS;
		Timer.T[Data.currentPlayer] = 1; 
		Data.Player_ID = deseriliaze.ID;
		WrongDeclare.WrongDeclarePlayer = deseriliaze.WRONG_DEC_PLAYER;
		
		Data.name = deseriliaze.U_NAME;
		Data.pass = deseriliaze.U_PASSWORD;
		Data.path = deseriliaze.U_IMAGE_PATH;
		
		UpdateStatus(deseriliaze.ID);
		    
	    switch(deseriliaze.ACTIVITY_ID)
	    {
	        case 1:
		    	Timer.ACTIVITY = 1;
				Intent i1 = new Intent(MainMenu.this, Login.class);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
		    	break;
		    
	        case 2:
		    	Timer.ACTIVITY = 2;
				Intent i2 = new Intent(MainMenu.this, Login.class);
				i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i2);
		    	break;
		    
	        case 3:
		    	Timer.ACTIVITY = 3;
				Intent i3 = new Intent(MainMenu.this, Login.class);
				i3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i3);
		    	break;
	    }      
	}
	
	public void UpdateStatus(int[] id)
	{
		SQLiteDatabase db = playerDB.getWritableDatabase();
		
		try
		{							
			String sql="update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_status = '0'";
			db.execSQL(sql);
			
			for(int i = 0; i < Data.numOfPlayers; i++)
			{
				int temp = id[i];
				String sql1="update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_status = '1' where _id ="+temp;
				db.execSQL(sql1);
			} 
			
			System.out.println("Cursor NuLL");			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		db.close();
	}
	
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.button1:
			Intent i = new Intent(this, Play.class);  
			startActivity(i);
			break;
			
		case R.id.button2:
			
			Intent i1 = new Intent(this, Register.class);  
			startActivity(i1);
			break;
			
		case R.id.button3:
			Intent i2 = new Intent(this, Leaderboard.class);  
			startActivity(i2);
			break;
			
		case R.id.button4:
			Intent i3 = new Intent(this, ResumeListView.class);  
			startActivity(i3);
			break;
			
		case R.id.button5:	
			Intent i4 = new Intent(this, Settings.class);  
			startActivity(i4);
			break;			
		}
	}
	
	public void ExitPopUp()
	{
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
    	View popupView = layoutInflater.inflate(R.layout.appexitpopup, null);
    	final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
    	final Button btnYes = (Button)popupView.findViewById(R.id.yespopup);
    	final Button btnNo = (Button)popupView.findViewById(R.id.nopopup);
    	
    	btnYes.setOnClickListener(new Button.OnClickListener()
    	{
			public void onClick(View arg0) 
			{						
				int pid= android.os.Process.myPid();
				android.os.Process.killProcess(pid);
				finish();
			}			            		
    	});

    	btnNo.setOnClickListener(new Button.OnClickListener()
    	{
			public void onClick(View v) 
			{
				popupWindow.dismiss();
			}
    	});
    	
    	popupWindow.showAtLocation(popupView, 0, 100, 100);
	}
	
	@Override
	public void onBackPressed() 
	{
		ExitPopUp();
	}
	
	@Override
    protected void onStop() 
	{
		super.onStop();		
    }
}