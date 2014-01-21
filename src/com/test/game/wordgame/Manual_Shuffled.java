package com.test.game.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

public class Manual_Shuffled extends Activity 
{
	private static int[] btnArray = new int[Data.numOfPlayers + 1];
	QuickContactBadge[] btn = new QuickContactBadge[btnArray.length];
	
	private static int[] txArray = new int[Data.numOfPlayers + 1];
	TextView[] t = new TextView[txArray.length];
	Button save;

	//public static String[] NAME = new String[Data.numOfPlayers];

	//public static String[] PASS = new String[Data.numOfPlayers];
	
	//public static String[] path = new String[Data.numOfPlayers + 1];
	
	//public static String[] Player_ID = new String[Data.numOfPlayers];
	
	public static String[] NAME = new String[Data.numOfPlayers];

	public static String[] PASS = new String[Data.numOfPlayers];
	
	public static String[] PATH = new String[Data.numOfPlayers + 1];
	
	public static int[] ID = new int[Data.numOfPlayers];
    
	int x = 0;
	
	int newClick =  0;
	int lastClick =  Data.numOfPlayers;
	
	public void onCreate(Bundle savedInstanceState) 
	{
	   super.onCreate(savedInstanceState);
	   Utils.onActivityCreateSetTheme(this);
	   
	   if(Data.numOfPlayers == 3)
 	   {
		   setContentView(R.layout.manual_shuffled);
		   
	       int[] btnArray1 = {R.id.quickContactBadge1, R.id.quickContactBadge2, R.id.quickContactBadge3,  R.id.quickContactBadge6};
	       btnArray = btnArray1;
	       
	       int[] txArray1 = {R.id.Tx_quickContactBadge1, R.id.Tx_quickContactBadge2, R.id.Tx_quickContactBadge3,  R.id.Tx_quickContactBadge6};
	       txArray = txArray1;	
 	  }
       
       else if(Data.numOfPlayers == 4)
 	   {
    	   setContentView(R.layout.manual_shuffled4);
    	   
	       int[] btnArray1 = {R.id.quickContactBadge1, R.id.quickContactBadge2, R.id.quickContactBadge3, R.id.quickContactBadge4,  R.id.quickContactBadge6};
	       btnArray = btnArray1;
	       
	       int[] txArray1 = {R.id.Tx_quickContactBadge1, R.id.Tx_quickContactBadge2, R.id.Tx_quickContactBadge3, R.id.Tx_quickContactBadge4,  R.id.Tx_quickContactBadge6};
	       txArray = txArray1;	
 	  }

       save = (Button)findViewById(R.id.Save);
   
       for (int i = 0; i < btnArray.length; i++)
	    {
            final int b = i;
	       
            btn[b] = (QuickContactBadge)findViewById(btnArray[b]);
	        t[b] = (TextView)findViewById(txArray[b]);
	    }
       
	    for (int i = 0; i < btnArray.length - 1; i++)
	    {
             final int b = i;
      
             Bitmap UserBitmap = BitmapFactory.decodeFile(Play.path[(i)]);
             btn[b].setImageBitmap(UserBitmap);
                  
             t[b].setText(Data.name[(i)]);
	    }
	    //path[Data.numOfPlayers] = "/mnt/sdcard/ShuffledWords/MyCameraApp/dummy_bg.png";
	  
	    Bitmap UserBitmap = BitmapFactory.decodeFile(Play.path[Data.numOfPlayers]);
        btn[Data.numOfPlayers].setImageBitmap(UserBitmap);
       
	    t[Data.numOfPlayers ].setText("_");
	    
	    for(int k = 0; k < Data.numOfPlayers; k++)
		{
			Log.v("UnShuffled Users", t[k].getText().toString());
			Log.v("UnShuffled U_Pass", Play.PASS[k]);
			Log.v("UnShuffled U_Path", Play.path[k]);
			Log.v("UnShuffled U_PID", Play.Player_ID[k]);
		}
	 
	    save.setOnClickListener(new View.OnClickListener() 
	    {			
			public void onClick(View v) 
			{				
				if(t[Data.numOfPlayers].getText().equals("_"))
				{				
					for(int i = 0; i < Data.numOfPlayers; i++)
					{
						for(int j = 0; j < Data.numOfPlayers; j++)
						{
							if(t[i].getText().toString().equals(Play.NAME[j]))
							{								
								Data.pass[i] = Play.PASS[j];
								Data.Player_ID[i] = Integer.parseInt(Play.Player_ID[j]);
								Log.v("Shuffled U_Pass", Play.PASS[j]);
								Log.v("Shuffled U_PID", Play.Player_ID[j]);
							}					
						}
					}
					
					for(int i = 0; i < Data.numOfPlayers; i++)
					{
					   Data.name[i] = t[i].getText().toString();
					   Data.path[i] = Play.path[i];
					   Log.v("Shuffled U_USERS", Play.NAME[i]);
					   Log.v("Shuffled U_U_Path", Play.path[i]);
					}
					
					NAME = Data.name;
					PASS = Data.pass;
					PATH = Data.path;
					ID = Data.Player_ID;
					
					Toast.makeText(getApplicationContext(), "Press Back to Main Menu", Toast.LENGTH_LONG).show();
					
				}
				else
				{
					 Toast.makeText(getApplicationContext(), "Please fill selection order correctly", Toast.LENGTH_LONG).show();
				}
			}
	    });	   
	   
	    for (int i = 0; i<btnArray.length; i++)
		{
			final int b = i;
	       
			btn [b].setOnClickListener
			(
			    new View.OnClickListener() 
			    {
			    	public void onClick(View v)
			    	{	
			    		for (int i = 0; i < btnArray.length; i++)
			    	    {			    		
			    		   if (btnArray[i] == v.getId())
			    	       {				    	   
					    		newClick = i;
					    		
					    		String temp = t[i].getText().toString();
					    		t[i] .setText(t[lastClick].getText());
					    		
					    		t[lastClick].setText(temp);
					    				
					    		Bitmap UserBitmap = BitmapFactory.decodeFile(Play.path[lastClick]);
					            btn [i].setImageBitmap(UserBitmap);
					    		
					            Bitmap UserBitmap1 = BitmapFactory.decodeFile(Play.path[i]);
					            btn[lastClick] .setImageBitmap(UserBitmap1);
					             
					            String tmp = Play.path[i];
					            Play.path[i] = Play.path[lastClick];
					            Play.path[lastClick] = tmp;
					             
					    		lastClick = i;
			    	       }
			    	    }
			    	}			
			    }	
	        );
	   }
	}
	
	protected void onPause()
	{
	    super.onPause();
	}
	
	protected void onResume()
	{
		super.onResume();
	}
}