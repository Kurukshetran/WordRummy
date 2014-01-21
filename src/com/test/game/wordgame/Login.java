package com.test.game.wordgame;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener
{
	DBHelper playerDB = new DBHelper(Login.this);
	
	private static final String TAG = "DialogActivity";
    private static final int DLG_EXAMPLE1 = 0;
    private static final int TEXT_ID = 0;
	
	Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnEnter, btnBackspace;
	EditText inputPassword;
	TextView UserName;
	ImageView UserImage, ErrorImage;
	
	public static String SAVED_GAME_TAG = " ";
	public static String DEFAULT_TAG = "PhoneLock";
	
	public static Bitmap UserBitmap, ErrorBitmap;
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    Utils.onActivityCreateSetTheme(this);
	    setContentView(R.layout.login);
	    
	    btn0 = (Button) findViewById(R.id.x0);
	    btn0.setOnClickListener(this);
	    btn1 = (Button) findViewById(R.id.x1);
	    btn1.setOnClickListener(this);
	    btn2 = (Button) findViewById(R.id.x2);
	    btn2.setOnClickListener(this);
	    btn3 = (Button) findViewById(R.id.x3);
	    btn3.setOnClickListener(this);
	    btn4 = (Button) findViewById(R.id.x4);
	    btn4.setOnClickListener(this);
	    btn5 = (Button) findViewById(R.id.x5);
	    btn5.setOnClickListener(this);
	    btn6 = (Button) findViewById(R.id.x6);
	    btn6.setOnClickListener(this);
	    btn7 = (Button) findViewById(R.id.x7);
	    btn7.setOnClickListener(this);
	    btn8 = (Button) findViewById(R.id.x8);
	    btn8.setOnClickListener(this);
	    btn9 = (Button) findViewById(R.id.x9);
	    btn9.setOnClickListener(this);
	    btnEnter = (Button) findViewById(R.id.xE);
	    btnEnter.setOnClickListener(this);
	    btnBackspace = (Button) findViewById(R.id.xBackspace);
	    btnBackspace.setOnClickListener(this);
	    
	    inputPassword = (EditText) findViewById(R.id.loginPassword);
	    inputPassword.setFocusable(false);
	    
	    RetrieveData();
	}
	
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.x0:
			addText(v);
			break;
		case R.id.x1:
			addText(v);
			break;
		case R.id.x2:
			addText(v);
			break;
		case R.id.x3:
			addText(v);
			break;
		case R.id.x4:
			addText(v);
			break;
		case R.id.x5:
			addText(v);
			break;
		case R.id.x6:
			addText(v);
			break;
		case R.id.x7:
			addText(v);
			break;
		case R.id.x8:
			addText(v);
			break;
		case R.id.x9:
			addText(v);
			break;
		case R.id.xE:
			isEnter(v);
		    break;
		case R.id.xBackspace:
			isBack(v);
			break;
		}
	}
	
	private void isEnter(View v)
	{
		if(Timer.ACTIVITY == 3)
		{
			Timer.ACTIVITY = 0;
    		Timer.T[Data.currentPlayer] = 1;
    		Intent i = new Intent(getApplicationContext(), Build.class);
			startActivity(i);					
			finish();				
		}
		else
		{
			Timer.T[Data.currentPlayer] = 1;
    		Intent i = new Intent(getApplicationContext(), PickCard.class);
			startActivity(i);					
			finish();
		}
	}
	
	private void addText(View v) 
	{	
		if(Data.Player_ID[Data.currentPlayer] <= 2)
		{
			inputPassword.setEnabled(false);
		}
		else
		{
			String b = "";
			b = (String) v.getTag();
			if (b != null) 
			{
				inputPassword.append(b);
			}			
		}
	}
	
	private void isBack(View v) 
	{
		CharSequence cc = inputPassword.getText();
		if (cc != null && cc.length() > 0) 
		{
			{
				inputPassword.setText("");
				inputPassword.append(cc.subSequence(0, cc.length() - 1));
			}
		}
	}
	
	public void RetrieveData()
	{	
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
		
		LoginProcessor();
	}
	
	public void LoginProcessor() 
	{
		UserImage = (ImageView) findViewById(R.id.userimage);
		ErrorImage = (ImageView) findViewById(R.id.errorImage);
	    UserName = (TextView) findViewById(R.id.login_user);
	    
	    if(Data.Player_ID[Data.currentPlayer] <= 2)
	    {
	    	UserName.setText(Data.name[Data.currentPlayer].toUpperCase());
	    	inputPassword.setHint("Press E to login");
	    	inputPassword.setEnabled(false);
	    	inputPassword.setFreezesText(true);
	    	
		    btn0.setEnabled(false);		    
		    btn1.setEnabled(false);		    
		    btn2.setEnabled(false);		    
		    btn3.setEnabled(false);		    
		    btn4.setEnabled(false);		    
		    btn5.setEnabled(false);		    
		    btn6.setEnabled(false);		    
		    btn7.setEnabled(false);		    
		    btn8.setEnabled(false);		    
		    btn9.setEnabled(false);		    
		    btnBackspace.setEnabled(false);
	    }
	    else
	    {			
	    	UserBitmap = BitmapFactory.decodeFile(Data.path[Data.currentPlayer]);
	    	UserImage.setImageBitmap(UserBitmap);
	    	UserName.setText(Data.name[Data.currentPlayer].toUpperCase());
	    	btnEnter.setEnabled(false);
	    }
			
		inputPassword.addTextChangedListener
		(
			new TextWatcher() 
			{
			    public void beforeTextChanged(CharSequence s, int start, int count, int after) 
		        {				          
		        }

		        public void onTextChanged(CharSequence s, int start, int before, int count)
		        {
		        	String pass1 = s.toString();
		        	
		        	if(pass1.equals(Data.pass[Data.currentPlayer]))
		        	{
		        		if(Timer.ACTIVITY == 3)
		        		{
		        			Timer.ACTIVITY = 0;
			        		Timer.T[Data.currentPlayer] = 1;
			        		Intent i = new Intent(getApplicationContext(), Build.class);
							startActivity(i);					
							finish();				
		        		}
		        		else
		        		{
		        			Timer.T[Data.currentPlayer] = 1;
			        		Intent i = new Intent(getApplicationContext(), PickCard.class);
							startActivity(i);					
							finish();
		        		}		        		
		        	}			        	
		        	else
					{
		        		ErrorImage.setImageResource(R.drawable.error);
					}	
		        }

				public void afterTextChanged(Editable arg0)
				{					
				}	
	       }
		);		
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
        				String path = save.SerializeGameData(Data.LOGIN_ACTIVITY, SAVED_GAME_TAG);
        				String Ex_Date = Data.No_of_Days(Date);
        				
        				rdb.READ_DATABASE(Login.this, SAVED_GAME_TAG, Date, Ex_Date, Name, path);
        				
        				Intent i1 = new Intent(Login.this ,MainMenu.class);
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
		indent.setClass(Login.this, MainMenu.class);
		indent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(indent);
	}
		
	public void onDestroy()
	{
		super.onDestroy();
		playerDB.close();
	}
	
	public void onBackPressed() 
	{
		Exit_Popup();
    }
	
	private void PhoneLock_EventHandler()
	{
    	Date d = new Date();
    	Save save = new Save();
    	ResumeDB rdb = new ResumeDB();
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	int i = 0;
		for(CharSequence NAMES :Data. name)
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
		String path = save.SerializeGameData(Data.LOGIN_ACTIVITY, DEFAULT_TAG);
		String Ex_Date = Data.No_of_Days(Date);
		rdb.READ_DATABASE(Login.this, DEFAULT_TAG, Date, Ex_Date, Name, path);
		
		Intent i1 = new Intent(Login.this ,MainMenu.class);
		i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i1);
    	finish();
	}
	
	protected void onPause()
	{
	    // If the screen is off then the device has been locked
	    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
	    boolean isScreenOn = powerManager.isScreenOn();

	    if (!isScreenOn) 
	    {
	        // The screen has been locked 
	        // do stuff...
	    	PhoneLock_EventHandler();
	    }
	    super.onPause();
	}
}