package com.test.game.wordgame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class PickCard extends Activity 
{	
	Button Pickbutton,Showbutton;
	char[] stringarray, Newdeckarray;
	char temp,tempCard;
	int OpenCard0 = Data.OpenCard;
	int OpenCard1 = OpenCard0 + 1;
	final Context context = this;
	
	private static final String TAG = "DialogActivity";
    private static final int DLG_EXAMPLE1 = 0;
    private static final int TEXT_ID = 0;
	public static String SAVED_GAME_TAG;
	public static String DEFAULT_TAG = "PhoneLock";	
	
	DBHelper playerDB = new DBHelper(PickCard.this);

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Utils.onActivityCreateSetTheme(this);
		setContentView(R.layout.pick);
		
		Timer.rounds[Data.currentPlayer]++;
		stringarray = Data.shuffledDecks.toCharArray();
		addListenerOnButton();		
	}
	
	public void addListenerOnButton()	
	{		
		Pickbutton = (Button) findViewById(R.id.pickbutton);
		Showbutton = (Button) findViewById(R.id.showbutton);
		Showbutton.setText(Data.shuffledDecks.substring(OpenCard0, OpenCard1));

		View.OnClickListener handler = new OnClickListener() 
		{			
			public void onClick(View v)
			{	    
				switch(v.getId())
				{
					  case R.id.pickbutton:
						  
						      Data.CloseCard++;		
						      if(Data.CloseCard >= Data.numOfCards)
						      { 
						    	  GetNewCards();						    	
						      }
						      tempCard = stringarray[OpenCard0];
						      stringarray[OpenCard0] = stringarray[Data.CloseCard];
						      stringarray[Data.CloseCard] = tempCard;
						     
						      ActivityChange();
						      break;
					
					  case R.id.showbutton:
						    
						      ActivityChange();
							  break; 
				}  
				Data.shuffledDecks=new String(stringarray);
			}

		};
		findViewById(R.id.pickbutton).setOnClickListener(handler);
		findViewById(R.id.showbutton).setOnClickListener(handler);
	}	
	
	private void GetNewCards()
	{
		 String newDecks = Data.randomForDrop();
		 Newdeckarray = newDecks.toCharArray();
		  
		 for(int k = 0;k<Data.n;k++)
	     {
			 stringarray[k + (Data.numOfPlayers*Data.words)] = Newdeckarray[k]; 
	     }
			  
    	 Data.CloseCard = (Data.numOfPlayers*Data.words) + 1;	
	}

	public void ActivityChange()
	{
		Intent intent1 = new Intent(context, Build.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent1);  
		finish();
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
	    				String path = save.SerializeGameData(Data.PICK_ACTIVITY, SAVED_GAME_TAG);
	    				String Ex_Date = Data.No_of_Days(Date);
	    				rdb.READ_DATABASE(PickCard.this, SAVED_GAME_TAG, Date, Ex_Date, Name, path);
	    				
	    				Intent i1 = new Intent(PickCard.this ,MainMenu.class);
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
		indent.setClass(PickCard.this, MainMenu.class);
		indent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(indent);
	}
	
	public void onBackPressed() 
	{
	   Exit_Popup();
	}
}