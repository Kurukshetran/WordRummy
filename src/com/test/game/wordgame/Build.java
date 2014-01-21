package com.test.game.wordgame;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Build extends Activity 
{  
    DBHelper playerDB = new DBHelper(Build.this);
   
    int open0 =  Data.OpenCard;
    int open1 =  open0+1;
    int newClick =  0;
    int lastClick =  Data.words + 2;
    int CP = Data.currentPlayer;   
   
    final Context context = this;
   
    char[] stringarray;
    char temp;
  
    TextView  UserName ;
    TextView  CurrentTime;
    TextView  CummulativeTime;
   
    private Runnable _myTask;
    public Handler _myHandler;   
   
    private static int[] idArray = new int[Data.words + 4];
    private static Button[] button;// = new Button[idArray.length];
    
	private static final String TAG = "DialogActivity";
    private static final int DLG_EXAMPLE1 = 0;
    private static final int TEXT_ID = 0;
    public static String SAVED_GAME_TAG;
	public static String DEFAULT_TAG = "PhoneLock";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Utils.onActivityCreateSetTheme(this);
		
		if( Data.setOfCards == 3)
		{
	       setContentView(R.layout.build);
	       int[] idArray1 = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6,R.id.button7,R.id.button8, R.id.button9, R.id.button10,R.id.button11,R.id.button12,R.id.button13};
	       idArray = idArray1;
		}
		else if(Data.setOfCards == 4)
		{
			setContentView(R.layout.build4);
			int[] idArray2 = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6,R.id.button7,R.id.button8, R.id.button9, R.id.button10,R.id.button11,R.id.button12,R.id.button13, R.id.button14,R.id.button15,R.id.button16};
			idArray = idArray2;	
		}
		else if(Data.setOfCards == 5)
		{
			setContentView(R.layout.build5);
			int[] idArray3 = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6,R.id.button7,R.id.button8, R.id.button9, R.id.button10,R.id.button11,R.id.button12,R.id.button13,R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19};
			idArray = idArray3;	
		}
		
	    UserName = (TextView) findViewById(R.id.login_user);
	    CurrentTime = (TextView)findViewById(R.id.current_time);
	    CummulativeTime = (TextView)findViewById(R.id.cummulative_time);
	
	    //int r = idArray.length;
	    button = new Button[idArray.length];
	    
	    for (int i = 0; i < idArray.length; i++)
	    {
             final int b = i;
	         button[b] = (Button)findViewById(idArray[b]); 	     
	    }
	    
	    CopyCardsToButtons();
		    
		button[Data.words + 1].setText(Data.shuffledDecks.substring(open0,open1));
		button[Data.words + 2].setText(".");
			
		_myTask = new MyTimerTask() ;
		_myHandler = new Handler();
		_myHandler.postDelayed(_myTask, 1000);
		     
		for (int i = 0; i<idArray.length; i++)
		{
			final int b = i;
	       
			button [b].setOnClickListener
			(
			    new View.OnClickListener() 
			    {
			    	public void onClick(View v)
			    	{		            	
			    	    String hash = button[Data.words + 2].getText().toString();
			    	    String hash1 = button[Data.words + 1].getText().toString();

			    	    for (int i = 0; i < idArray.length; i++)
			    	    {
			    	    	if (idArray[i] == v.getId())
			    	    	{  
			    	    		if(i<Data.words||i == (Data.words+1)||i == (Data.words+2))
			    	    		{
			    	    			interchange(i);
			    	    		}			                    	    	    
			    	    		else if(i == Data.words)
			    	    		{
			    	    			if(hash.equals(".") || hash1.equals("."))
			    	    			{   
			    	    				CollectScore();
			    	    				
			    	    				Timer.T[Data.currentPlayer] = 0;
			    	    				Timer.currentTime[CP] = 0;
  	   
			    	    				CopyButtonsToCards(hash, hash1);
			    	    				Data.currentPlayer++;
			    	    				Intent i1 = new Intent(Build.this ,Login.class);
			    	    				startActivity(i1);			    	   
			    	    			}	
			    	    			else
			    	    			{
			    	    				Toast.makeText(getApplicationContext(), "To Drop a Card make EMPTY BUTTON in empty state", Toast.LENGTH_SHORT).show();   
			    	    			}
			    	    		}           	    
			    	    		else if(i == (Data.words + 3))
			    	    		{
			    	    			if(hash.equals(".")|| hash1.equals("."))
			    	    			{
			    	    				CollectScore();
			    	    				
			    	    				Timer.T[Data.currentPlayer] = 0;
			    	    				Timer.currentTime[CP] = 0;
	  	  
			    	    				CopyButtonsToCards(hash, hash1);
			    	    				declare();
			    	    			}
			    	    			else
			    	    			{
			    	    				Toast.makeText(getApplicationContext(), "To Declare make EMPTY BUTTON in empty state", Toast.LENGTH_SHORT).show();   
			    	    			}
			    	    		}
			    	 	   	}
			    	    } 
			    	}
			    }
			);
		}
	}
	
	public void CollectScore()
	{
		int score;
		
		score = Timer.rounds[Data.currentPlayer] * Timer.currentTime[Data.currentPlayer];
		score = ((score + 5) / 10) * 10;
	    Timer.Total_score[Data.currentPlayer] = Timer.Total_score[Data.currentPlayer] - score;
	    Log.v("","");
	    
	    if(Timer.Total_score[Data.currentPlayer] <= 100)
	    {
	    	Timer.Total_score[Data.currentPlayer] = 100;
	    }
	}

	private void CopyCardsToButtons() 
	{
		 int NC = Data.words;
		 int PI = Data.currentPlayer * NC;
		  
		 if(Data.currentPlayer >= Data.numOfPlayers)
	     {	    	
			 Data.currentPlayer = 0;
	     }
	   
	     for(int k = 0; k < NC; k++)
		 {
	    	 int g = (PI + k);
	    	 int g1 = g + 1;
	    	 button[k].setText(Data.shuffledDecks.substring(g , g1));//ERRROR
		 }	    
	}
	public void declare() 
	{		
		int NC = Data.words;
	    int PI = Data.currentPlayer * NC;
	    String rowString;
	    SQLiteDatabase db1 = playerDB.getReadableDatabase();
	    
		for(int i = 0; i < 3; i++)
		{
			int c = i + 1;
			int k = PI + (i * Data.setOfCards);
			int j = PI + (c * Data.setOfCards);

			rowString = Data.shuffledDecks.substring(k,j);
			
			//Data.Row[Data.currentPlayer] = 3;
			
			try
			{								
				Cursor cursor = db1.rawQuery("SELECT * FROM " + DBHelper.DATABASE_TABLE_NAME_WORDINFO + " WHERE word = "+"'"+rowString+"'",null);
			
				if(cursor != null)
				{
					while (cursor.moveToNext()) 
					{
						Data.Row[Data.currentPlayer]++;
					}				    	
				}
				else
				{
					System.out.println("Cursor NuLL");
				}
				
				cursor.close();				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}  		
		  
		if(Data.Row[Data.currentPlayer] == 3)
		{
			Intent i1 = new Intent(Build.this ,GameWon.class);
			startActivity(i1);
			finish();			
		}
		else
		{
			Intent i1 = new Intent(Build.this ,WrongDeclare.class);
			startActivity(i1);
			finish();
		}    
		db1.close();
	}

	public void interchange(int k)
	{
		newClick = k;
		CharSequence temp = button[k].getText();
		
		button[k].setText(button[lastClick].getText());
		button[lastClick].setText(temp);
		
		lastClick = k;
    }
		
	public void CopyButtonsToCards(String hash, String hash1) 
	{
		int NC = Data.words;
	    int PI = Data.currentPlayer * NC;
	   
	    char[] cs;
	    char[] ds = null;
	    char[] cards = Data.shuffledDecks.toCharArray();
	    	    
	    for(int k = 0; k < NC; k++)
	    {
	    	int g = (PI + k);
	    	cs = button[k].getText().toString().toCharArray();
	     	cards[g] = cs[0];
	    }	
		    
	    //DOUBT
		Data.shuffledDecks =  Data.shuffledDecks.copyValueOf(cards);
		
		//Show Cards Swapping with another Activity
		if(hash.equals("."))
		{
			ds = button[Data.words+1].getText().toString().toCharArray();
		}
		
		if(hash1.equals("."))
		{
			ds = button[Data.words+2].getText().toString().toCharArray();
		}
		
		stringarray = Data.shuffledDecks.toCharArray();
		stringarray[open0] = ds[0];
		
		Data.shuffledDecks = new String(stringarray);			   
	}	

	class MyTimerTask implements Runnable
	{		
		public void run() 
		{
			String text;
			String text1;
		
	     	if (Timer.T[CP] == 1)
			{	     		
			    Timer.currentTime[CP] = Timer.currentTime[CP] + 1;
			    Timer.cummulativeTime[CP]++;
			    
    		    if(Timer.currentTime[CP] % 60 < 10)
	            {
	                text = (Timer.currentTime[CP]/60) + ":" + (Timer.currentTime[CP]%60);
	                text1 = (Timer.cummulativeTime[CP]/60) + ":" + (Timer.cummulativeTime[CP]%60);
	            }
	            else
	            {
	                text = (Timer.currentTime[CP]/60) + ":" + (Timer.currentTime[CP]%60);
	                text1 = (Timer.cummulativeTime[CP]/60) + ":" + (Timer.cummulativeTime[CP]%60);
	            }

			    UserName.setText(Data.name[Data.currentPlayer].toUpperCase());
			    CurrentTime.setText("CT"+" "+" "+text);
			    CummulativeTime.setText("TT"+" "+" "+text1);
				
			    _myHandler.postDelayed(this, 1000);
			}
		}
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		playerDB.close();
	}
	
	public void onBackPressed() 
	{
		Timer.T[Data.currentPlayer] = 0;
		Exit_Popup();
    }
	
	public void getMaximumTimeToLock ()
	{
		Timer.T[Data.currentPlayer] = 0;	
	}
	
	public void Exit_Popup()
	{
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
    	View popupView = layoutInflater.inflate(R.layout.exitpopup, null);
    	final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
    	final Button btnResume = (Button)popupView.findViewById(R.id.extResume);
    	final Button btnExit = (Button)popupView.findViewById(R.id.extExit);
    	final Button btnCancel = (Button)popupView.findViewById(R.id.extCancel);
    	
    	btnResume.setOnClickListener(new Button.OnClickListener()
    	{
			public void onClick(View arg0) 
			{	
				showDialog(DLG_EXAMPLE1);
			}			            		
    	});

    	btnExit.setOnClickListener(new Button.OnClickListener()
    	{
			public void onClick(View v) 
			{
				Exit();
			}
    	});
    	
    	btnCancel.setOnClickListener(new Button.OnClickListener()
    	{
			public void onClick(View v) 
			{
				popupWindow.dismiss();
			}
    	});
    	popupWindow.setFocusable(true);
    	popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
	}
	
	@Override
    protected Dialog onCreateDialog(int id) 
	{ 
        switch (id) 
        {
            case DLG_EXAMPLE1:
                return createExampleDialog();
            default:
                return null;
        }
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) 
    { 
        switch (id) 
        {
        case DLG_EXAMPLE1:
    
            EditText text = (EditText) dialog.findViewById(TEXT_ID);
            text.setText("");
            break;
        }
    }

    private Dialog createExampleDialog() 
    { 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resume Tag");
        builder.setMessage("Enter name for saved game:");
 
        final EditText input = new EditText(this);
        input.setId(TEXT_ID);
        builder.setView(input);
 
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
        { 
            public void onClick(DialogInterface dialog, int whichButton) 
            {
            	SAVED_GAME_TAG = input.getText().toString();
                Log.d(TAG, "User name: " + SAVED_GAME_TAG);
                
                if(SAVED_GAME_TAG.equals(""))
                {
                	Toast.makeText(getApplicationContext(), "Enter Game Tag", Toast.LENGTH_SHORT).show();
                }
                else
                {
                	boolean check = CheckForSameName(SAVED_GAME_TAG);
                	if(check == true)
                	{
	                	Date d = new Date();
	                	Save save = new Save();
	                	ResumeDB rdb = new ResumeDB();
	                	
	                	StringBuilder stringBuilder = new StringBuilder();
	                	int i = 0;
	            		for(CharSequence NAMES : Data.name)
	            		{
	            			if(i == (Data.name.length - 1))
	            			{
	            				stringBuilder.append(""+"&"+""+NAMES);
	            			}
	            			else if(i == 0)
	            			{
	            				stringBuilder.append(NAMES);
	            			}
	            			else
	            			{
	            				stringBuilder.append(","+NAMES);
	            			}
	            			i++;
	            		}
	            		
	                	String Name = stringBuilder.toString();
	                	String Date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(d);
	    				String path = save.SerializeGameData(Data.BUILD_ACTIVITY, SAVED_GAME_TAG);
	    				String Ex_Date = Data.No_of_Days(Date);
	    				rdb.READ_DATABASE(Build.this, SAVED_GAME_TAG, Date, Ex_Date, Name, path);
	    				
	    				Intent i1 = new Intent(Build.this ,MainMenu.class);
	    				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	    	startActivity(i1);
	        	    	finish();
                	}
                	else
                	{
                		Toast.makeText(getApplicationContext(), "Game id already exits", Toast.LENGTH_SHORT).show();
                	}
                }
                return;
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {
                return;
            }
        });
 
        return builder.create();
    }
    
    public boolean CheckForSameName(String text)
    {
    	SQLiteDatabase db = playerDB.getReadableDatabase();	
    	int id = 0;
    	
		try
		{		
			Cursor cursor = db.rawQuery("SELECT _id FROM Resume WHERE game_id = "+"'"+text+"'", null);
				
			if(cursor != null)
			{				
				System.out.println("database showing");
				startManagingCursor(cursor);
				
				while(cursor.moveToNext())
				{
					id = cursor.getInt(cursor.getColumnIndex("_id"));				    
				}
			}
			cursor.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		db.close();
		
		if(id == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
    }	

	private void Exit() 
	{
		Data.GameReset();
		 
		Intent indent = new Intent();
		indent.setClass(Build.this,MainMenu.class);
		indent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(indent);
	}
}