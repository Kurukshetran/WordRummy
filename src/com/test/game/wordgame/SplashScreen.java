package com.test.game.wordgame;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Typeface;
import android.widget.TextView;

public class SplashScreen extends Activity
{
	public static final int APP_THREAD_DELAY = 2000;
	
	DBHelper playerDB = new DBHelper(SplashScreen.this);
	
	static ArrayList<String> Words = new ArrayList<String>();
	
	MediaPlayer mediaPlayer;
	
	TextView MentorTitle, MentorName, ProjectTitle, Member1, Member2;
	String Font_Path = "fonts/BoyzRGrossNF.ttf";
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        
        mediaPlayer = MediaPlayer.create(SplashScreen.this, R.raw.startscreen);
        
        if(!mediaPlayer.isPlaying())
      	{
        	mediaPlayer.start();
      	}
        
        MentorTitle = (TextView) findViewById(R.id.creditsText);
        MentorName = (TextView) findViewById(R.id.mentorText);
        ProjectTitle = (TextView) findViewById(R.id.members);
        Member1 = (TextView) findViewById(R.id.membersname1);
        Member2 = (TextView) findViewById(R.id.membersname2);
        
        Typeface tf = Typeface.createFromAsset(getAssets(), Font_Path);
        
        MentorTitle.setTypeface(tf);
        MentorName.setTypeface(tf);
        ProjectTitle.setTypeface(tf);
        Member1.setTypeface(tf);
        Member2.setTypeface(tf);
                
        delay();
    }    
  
    private void delay()
	{
	  new Handler().postDelayed(new Thread() 
	  {
		  @Override
		  public void run() 
		  {
			  if(mediaPlayer.isPlaying())
			  {
				  mediaPlayer.stop();
				  mediaPlayer.release();
			  }
			  
			  Intent mainMenu = new Intent(SplashScreen.this, MainMenu.class);
			  mainMenu.putExtra("AppStarted",true);
			  SplashScreen.this.startActivity(mainMenu);
			  SplashScreen.this.finish();
		  }
	  }, APP_THREAD_DELAY);		
	}   
}