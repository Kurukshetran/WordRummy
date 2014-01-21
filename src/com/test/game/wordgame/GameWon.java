package com.test.game.wordgame;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.media.MediaPlayer;

public class GameWon extends Activity 
{		
	int row = 0;
	Button Playagain;
	Button Exit;
	
	int[] p = new int[Data.numOfPlayers];
	int[] l = new int[Data.numOfPlayers];
	int AddPoints;
	int TotalPoints;
	
	TextView NoticeBoard ,Score ,Timing, Total_Points;
	String NBResult ,ScoreOut ,TimingOut, T_Points;
	String pass;
	
	MediaPlayer mediaPlayer;
	
	DBHelper playerDB = new DBHelper(GameWon.this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    Utils.onActivityCreateSetTheme(this);
	    setContentView(R.layout.gamewon);
	    
	    if(Data.WON_PATH == "")
	    {	    
	    	mediaPlayer = MediaPlayer.create(this, R.raw.applause);		
	    	mediaPlayer.start();
	    }
	    else
	    {
	    	mediaPlayer = new MediaPlayer();
	    	
	    	try 
			{
				mediaPlayer.setDataSource(Data.WON_PATH);
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}	    	
	    }		
	   
	    NoticeBoard = (TextView)findViewById (R.id.noticeboard);
		Score = (TextView)findViewById (R.id.OutputofScore);
		Timing = (TextView)findViewById (R.id.outputofTiming);
		Total_Points = (TextView)findViewById(R.id.OutputTotalScore);
		
		Playagain = (Button)findViewById(R.id.PlayAgain);
		Exit = (Button)findViewById(R.id.Exit);

	    checkResult();
	}

	private void checkResult()
	{
        NoticeBoard.setText("Congratulation "+Data.name[Data.currentPlayer].toUpperCase()+",  "+" you won the game");
            
        String text1;
        String text2;
		 
        if(Timer.currentTime[Data.currentPlayer] % 60 < 10)
        {
             text1 = (Timer.cummulativeTime[Data.currentPlayer]/60) + ":" + (Timer.cummulativeTime[Data.currentPlayer]%60);
        }
        else
        {
             text1 = (Timer.cummulativeTime[Data.currentPlayer]/60) + ":" + (Timer.cummulativeTime[Data.currentPlayer]%60);
        }
        
	    Timing.setText(text1);
      
	    text2 = Integer.toString(Timer.Total_score[Data.currentPlayer]);
	       
	    Score.setText(text2);
	    
	    AddScore();
	    
	    AddPoints = TotalPoints + Timer.Total_score[Data.currentPlayer];
	    T_Points = String.valueOf(AddPoints);
	    Total_Points.setText(T_Points);
       
	    buttonControll();		
	}
	
	public void AddScore()
	{
		SQLiteDatabase db = playerDB.getReadableDatabase();
	
		try
		{			
			int id = Data.Player_ID[Data.currentPlayer];		
			
			Cursor cursor = db.rawQuery("SELECT player_played, player_won, player_points" +" FROM " + DBHelper.DATABASE_TABLE_NAME_USERINFO + " WHERE _id = " +id,null);
		
			if(cursor != null)
			{
				System.out.println("database showing");
				startManagingCursor(cursor);
				while (cursor.moveToNext()) 
				{
				    String played = cursor.getString(0);
				    String won = cursor.getString(1);
				    String pt = cursor.getString(2);				  
			        
				    int play = Integer.parseInt(played);
				    int win = Integer.parseInt(won);
				    int points = Integer.parseInt(pt);
				    
				    TotalPoints = points;
			        T_Points = String.valueOf(points);
			        points = points + Timer.Total_score[Data.currentPlayer];
			        
			        win++;
			        play++;
			      
			        AddPoints(play, win, points);			      
				}
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

	public void AddPoints(int play, int win, int points)
	{
		playerWon( play, win, points);
		playerLoss();
	}	

	private void playerLoss()
	{
		for(int i = 0; i < Data.numOfPlayers; i++ )
		{
			if( i != Data.currentPlayer && WrongDeclare.WrongDeclarePlayer[i] != 2)
			{
			   	readDB(i);
				
	          	updateDB(i);
			}			
		}		
	}

	private void updateDB(int i)
	{
		 SQLiteDatabase db = playerDB.getWritableDatabase();
		try
		{		
			int id = Data.Player_ID[i];
			
			String sql = "update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_played =" +"'"+p[i]+"'"+"where _id=";
			db.execSQL(sql + id);
			
			String sql1 = "update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_loss =" +"'"+l[i]+"'"+"where _id=";
			db.execSQL(sql1 + id);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	     
		db.close();
	}

	private void readDB(int i)
	{
		SQLiteDatabase db = playerDB.getReadableDatabase();
		
		try
		{					
			int id = Data.Player_ID[i];
			
			Cursor cursor = db.rawQuery("SELECT player_played, player_loss" +" FROM " + DBHelper.DATABASE_TABLE_NAME_USERINFO + " WHERE _id = " +id ,null);
		
			if(cursor != null)
			{
				System.out.println("database showing");
				startManagingCursor(cursor);
				while (cursor.moveToNext()) 
				{
				    String played = cursor.getString(0);
				    String loss = cursor.getString(1);
				    
				     p[i] = Integer.parseInt(played);
				     l[i] = Integer.parseInt(loss);
				    
			         p[i]++;
			         l[i]++;			      			       			      
				}
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

	private void playerWon(int play, int win, int points)
	{
       SQLiteDatabase db = playerDB.getWritableDatabase();
		
		try
		{		
			int id = Data.Player_ID[Data.currentPlayer];
			
			String sql = "update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_played =" +"'"+play+"'"+"where _id=";
			db.execSQL(sql + id);
			
			String sql1 = "update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_won =" +"'"+win+"'"+"where _id=";
			db.execSQL(sql1 + id);
			
			String sql2 = "update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_points =" +"'"+points+"'"+"where _id=";
			db.execSQL(sql2 + id);
			
			System.out.println("Cursor NuLL");			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		db.close();
	}

	private void buttonControll()
	{
		View.OnClickListener handler = new OnClickListener() 
		{			
			public void onClick(View v)
			{			    
				switch(v.getId())
				{
					  case R.id.PlayAgain:
						  
						   PlayAgain_Handler();
					       break;
					
					  case R.id.Exit:
						    
						   Exit_Handler();
		    	    	   break;
				}				
			}	
		};
		findViewById(R.id.PlayAgain).setOnClickListener(handler);
		findViewById(R.id.Exit).setOnClickListener(handler);
	}
	
	private void PlayAgain_Handler()
	{
		Data.GameReset();
		   
	    if(mediaPlayer.isPlaying())
	    {
		    mediaPlayer.stop();
		    mediaPlayer.release();
	    }
	    else
	    {
	    	mediaPlayer.stop();
		    mediaPlayer.release();
	    }
	   
	    Intent i1 = new Intent(GameWon.this ,Play.class);
	    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 	    startActivity(i1);
 	    finish();
	}
	
	private void Exit_Handler()
	{
		Data.GameReset();
		   
	    if(mediaPlayer.isPlaying())
	    {
		    mediaPlayer.stop();
		    mediaPlayer.release();
	    }
	    else
	    {
	    	mediaPlayer.stop();
		    mediaPlayer.release();
	    }
	   
	    Intent i2 = new Intent(GameWon.this ,MainMenu.class);
	    i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(i2);
 	    finish();
	}
	
	@Override
	public void onBackPressed() 
	{
	    return;
	}	
}