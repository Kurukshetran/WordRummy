package com.test.game.wordgame;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ResumeListView extends Activity 
{
	 private DBHelper dbHelper = new DBHelper(ResumeListView.this);
	 private SimpleCursorAdapter dataAdapter;
	 
	 public static View header;
	 
	 public static String Id = "";
	 public static String GameID = "";
	 public static String Date = "";
	 public static String PlayersName = "";	
	 public static String File;
	 
	 int count = 0;
	 int g = 0;
	 TableLayout resume_table;
	 
	 @Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
		 Utils.onActivityCreateSetTheme(this);
		 
		 if(CheckResumeTableForEmpty() == false)
		 {
			 setContentView(R.layout.resumeempty);
			 resume_table = (TableLayout)findViewById(R.id.resume);
			 DisplayPopUp();
		 }
		 else
		 {
			 setContentView(R.layout.resume);
			 DisplayListView(); 
		 }	 
	 }
	 
	 private void DisplayPopUp()
	 {
		 TableRow row;
		 TextView t1;
			
	     int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(float) 1, getResources().getDisplayMetrics());
   
         row = new TableRow(this);
       
         t1 = new TextView(this);
                
         t1.setTextSize(18);
       
         t1.setText("Currently no saved games");
        
         t1.setWidth(70 * dip);
         t1.setGravity(Gravity.CENTER);
       
         t1.setPadding(20*dip, 0, 0, 0);
       
         row.addView(t1);       
    
         resume_table.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	 }
	 
	 private boolean CheckResumeTableForEmpty()
	 {
		 SQLiteDatabase db = dbHelper.getReadableDatabase();
	     
		 boolean chk;
			
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
	 
	 private void DisplayListView() 
	 {	  
		 SQLiteDatabase db = dbHelper.getReadableDatabase();
						
		 Cursor mCursor = db.query(DBHelper.DATABASE_TABLE_NAME_RESUME, new String[] {"_id", "game_id",
				    "date", "expired_date", "playername", "file"}, null, null, null, null, null);
				 
		 if (mCursor != null) 
		 {
			 mCursor.moveToFirst();
		 }
		 
		 String[] columns = new String[] 
		 {
		     "date",
		     "expired_date",
		     "game_id"		     
		 };
		 
		 int[] to = new int[] 
		 {
			 R.id.date,			 
			 R.id.playersname,
			 R.id.resumeGameTag
		 };
		 
		 dataAdapter = new SimpleCursorAdapter(this, R.layout.resumelist,mCursor,columns,to,0);
		 
		 ListView listView = (ListView) findViewById(R.id.listview1);
		 listView.setAdapter(dataAdapter);		 
		 
		 listView.setOnItemClickListener(new OnItemClickListener() 
		 {
			 public void onItemClick(AdapterView<?> listView, View view, int position, long id) 
			 {		
			 }
		 });			 
	 }
	 
	public void PlayEventHandler(View v)
	{
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
         
        TextView child = (TextView)vwParentRow.getChildAt(0);
        TextView child1 = (TextView)vwParentRow.getChildAt(1);
        TextView child2 = (TextView)vwParentRow.getChildAt(2);
        
        Button btnChild = (Button)vwParentRow.getChildAt(3);
        
        String filepath = RetrieveData(child1.getText().toString());
        DeserilizeGame(filepath, child1.getText().toString());
	}
	
	public void DeserilizeGame(String path, String savedgame)
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
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
	   	db.delete(DBHelper.DATABASE_TABLE_NAME_RESUME, "game_id ="+"'"+savedgame+"'", null);	   	
        db.close();
		    
	    switch(deseriliaze.ACTIVITY_ID)
	    {
	        case 1:
		    	Timer.ACTIVITY = 1;
				Intent i1 = new Intent(ResumeListView.this, Login.class);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
		    	break;
		    
	        case 2:
		    	Timer.ACTIVITY = 2;
				Intent i2 = new Intent(ResumeListView.this, Login.class);
				i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i2);
		    	break;
		    
	        case 3:
		    	Timer.ACTIVITY = 3;
				Intent i3 = new Intent(ResumeListView.this, Login.class);
				i3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i3);
		    	break;
	    }      
	}
	
	public String RetrieveData(String gameTag)
	{		
		try
		{			
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			
			Cursor cursor = db.rawQuery("select file from Resume where game_id="+"'"+gameTag+"'",null);//db.query(DBHelper.DATABASE_TABLE_NAME_RESUME, columns, null, null, null, null, null);
			
			if(cursor != null)
			{								
				System.out.println("database showing");
				startManagingCursor(cursor);
				while (cursor.moveToNext()) 
				{				  
			       File = cursor.getString(0);			      
				}					
			}			   
			   System.out.println("Cursor NuLL");			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return File;
	}
	
	public void DeleteEventHandler(View v)
	{
		RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        
        TextView child = (TextView)vwParentRow.getChildAt(0);
        TextView child1 = (TextView)vwParentRow.getChildAt(1);
        TextView child2 = (TextView)vwParentRow.getChildAt(2);
        
        Button btnChild = (Button)vwParentRow.getChildAt(3);
        
        DeleteResumeListItem(child1.getText().toString());
   	
        Toast.makeText(getApplicationContext(), child1.getText(), Toast.LENGTH_SHORT).show();		
	}
	
	public void DeleteResumeListItem(String saved_game)
	{
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	   	db.delete(DBHelper.DATABASE_TABLE_NAME_RESUME, "game_id ="+"'"+saved_game+"'", null);	   	
        db.close();
      
        Intent intent = getIntent();
        finish();
        startActivity(intent);
	}
		
	public void UpdateStatus(int[] id)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
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
}