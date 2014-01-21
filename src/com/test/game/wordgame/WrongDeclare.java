package com.test.game.wordgame;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class WrongDeclare extends Activity 
{		
	TextView NoticeBoard, Timing, Rounds;
	public static int[] WrongDeclarePlayer = new int[Data.numOfPlayers];
	
	DBHelper playerDB = new DBHelper(WrongDeclare.this);
	
	int points;
	int play;
	int loss;
	
	public static int MINUS_POINT = 100;
	
	Button Playagain ;
	Button Exit ;
	int j = 0;
	
	MediaPlayer mediaPlayer;
	
	public static String DEFAULT_PATH;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    Utils.onActivityCreateSetTheme(this);
	    setContentView(R.layout.wrongdeclare);
	    
	    PlaySound();		
	    
	    NoticeBoard = (TextView)findViewById (R.id.NoticeBoard);
	    NoticeBoard.setText("Sorry"+"  "+Data.name[Data.currentPlayer]+" "+" Wrong Declare");
	    Timing = (TextView)findViewById (R.id.outputofTiming);
	    Rounds = (TextView)findViewById (R.id.outputofRounds);
	    
	    Playagain = (Button)findViewById(R.id.PlayAgain1);
		Exit = (Button)findViewById(R.id.Exit1);
	    
	    scoreReduction();
	    
	    reducePoints();
	   
	    String text1;
	    String text2 = Integer.toString(Timer.rounds[Data.currentPlayer]);
	    
        if(Timer.currentTime[Data.currentPlayer] % 60 < 10)
        {
             text1 = (Timer.cummulativeTime[Data.currentPlayer] / 60) + ":" + (Timer.cummulativeTime[Data.currentPlayer] % 60);
        }
        else
        {
             text1 = (Timer.cummulativeTime[Data.currentPlayer] / 60) + ":" + (Timer.cummulativeTime[Data.currentPlayer] % 60);
        }
	    Timing.setText(text1);
	    Rounds.setText(text2);
	}	
	
	public void PlaySound()
	{
		if(Data.LOSE_PATH == "")
	    {
	    	mediaPlayer = MediaPlayer.create(WrongDeclare.this, R.raw.lose);	    	
			mediaPlayer.start();	    	
	    }
	    else
	    {
	    	mediaPlayer = new MediaPlayer();
	    	
	    	try 
			{
				mediaPlayer.setDataSource(Data.LOSE_PATH);
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
	    }
	}
	
	public void scoreReduction()
	{
		SQLiteDatabase db = playerDB.getReadableDatabase();
	
		try
		{			
			int id = Data.Player_ID[Data.currentPlayer];
			
			Cursor cursor = db.rawQuery("SELECT player_played, player_points, player_loss" +" FROM " + DBHelper.DATABASE_TABLE_NAME_USERINFO 	+ " WHERE _id = " +id ,null);
		
			if(cursor != null)
			{
				System.out.println("database showing");
				startManagingCursor(cursor);
				
				while (cursor.moveToNext()) 
				{	      		
				    String played = cursor.getString(0);
				    String pt = cursor.getString(1);
				    String lose = cursor.getString(2);
			        
				    play = Integer.parseInt(played);
				    points = Integer.parseInt(pt);
				    loss  = Integer.parseInt(lose);
				    
				    play++; 
				    loss++;
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

	public void reducePoints()
	{
		if(points >= 1000)
		{
					  		    
		    Playagain.setClickable(true);
		    Exit.setClickable(true);
		    buttonControll();
		}
		else
		{
			
			Playagain.setClickable(false);
		    Exit.setClickable(true);
		    buttonControll();			
		}
	}
	
	public void UpdateScore(int points)
	{
		SQLiteDatabase db = playerDB.getWritableDatabase();
		
		try
		{					
			int id = Data.Player_ID[Data.currentPlayer];
			
			String sql="update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_points =" +"'"+points+"'"+ "where _id=";
			db.execSQL(sql + id);
			
			
			System.out.println("Cursor NuLL");			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		db.close();
	}
	
	public void UpdateLoss()
	{
		SQLiteDatabase db = playerDB.getWritableDatabase();
		
		try
		{		
			int id = Data.Player_ID[Data.currentPlayer];
			
			String sql = "update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_played =" +"'"+play+"'"+ "where _id =";
			db.execSQL(sql+id);
			
			String sql1 = "update "+DBHelper.DATABASE_TABLE_NAME_USERINFO+" set player_loss =" +"'"+loss+"'"+ "where _id =";
			db.execSQL(sql1+id);
			
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
					  case R.id.PlayAgain1:
						  
						  Log.d("POINTS", String.valueOf(points));
						  
						  if(points <= 900)
						  {							     
							    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
				            	View popupView = layoutInflater.inflate(R.layout.wrongdecpopup, null);
				            	final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				            	
				            	final Button btnResume = (Button)popupView.findViewById(R.id.wrongExit);
				            	
				            	btnResume.setOnClickListener(new Button.OnClickListener()
				            	{
									public void onClick(View arg0) 
									{
										if(mediaPlayer.isPlaying())
										{
											mediaPlayer.stop();
											mediaPlayer.release();
										}								
										
										Data.NotOut_Players = Data.NotOut_Players - 1;
										
										Data.currentPlayer++;
										
										label:
											
											while ((Data.currentPlayer>=Data.numOfPlayers) || (WrongDeclare.WrongDeclarePlayer[Data.currentPlayer] == 2))
											{
												if(Data.currentPlayer>=Data.numOfPlayers)
										    	{
													Data.currentPlayer = 0;
											    }
										
											   if(WrongDeclare.WrongDeclarePlayer[Data.currentPlayer] == 2)
										       {
												   Data.currentPlayer++;
												   continue label;		   
											   }
											}	  	
										
										if(Data.NotOut_Players == 1)
										{
											Intent i1 = new Intent(WrongDeclare.this ,GameWon.class);
											i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							    	    	startActivity(i1);
										}
										else
										{
											
											
											Intent i1 = new Intent(WrongDeclare.this ,Login.class);
											i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							    	    	startActivity(i1);
										}
									}			            		
				            	});				            	
				            	
				            	popupWindow.showAsDropDown(v.findViewById(R.id.PlayAgain1),50, 30);
						  }
						  else
						  {
							   int Point = points - MINUS_POINT;
							   UpdateScore(Point);
							  
							  Data.currentPlayer++;
							    
						   
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
							  Intent mainMenu = new Intent(WrongDeclare.this, Login.class);
							  WrongDeclare.this.startActivity(mainMenu);
							  WrongDeclare.this.finish();
						  }
					      break;
					
					  case R.id.Exit1:
						  
						  UpdateLoss();
						  
						  WrongDeclarePlayer[Data.currentPlayer] = 2;
						  
						  
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
							
							Data.currentPlayer++;
							
							label:
								
								while ((Data.currentPlayer>=Data.numOfPlayers) || (WrongDeclare.WrongDeclarePlayer[Data.currentPlayer] == 2))
								{
									if(Data.currentPlayer>=Data.numOfPlayers)
							    	{
										Data.currentPlayer = 0;
								    }
							
								   if(WrongDeclare.WrongDeclarePlayer[Data.currentPlayer] == 2)
							       {
									   Data.currentPlayer++;
									   continue label;		   
								   }
								}	  	
							 
							Data.NotOut_Players = Data.NotOut_Players - 1;
							
							if(Data.NotOut_Players == 1)
							{
								Intent i1 = new Intent(WrongDeclare.this ,GameWon.class);
								i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    	    	startActivity(i1);
							}
							else
							{
								Intent i1 = new Intent(WrongDeclare.this ,Login.class);
								i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    	    	startActivity(i1);
							}  
					      break;
				}				
			}	
		};
		findViewById(R.id.PlayAgain1).setOnClickListener(handler);
		findViewById(R.id.Exit1).setOnClickListener(handler);
	}
	
	@Override
	public void onBackPressed() 
	{
	    return;
	}	
}