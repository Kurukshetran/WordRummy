package com.test.game.wordgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class ResumeDB 
{
	String File = "";
	
	public void READ_DATABASE(Context context, String GAME_ID, String DATE, String EX_DATE, String NAMES, String FILE_PATH)
	{
		DBHelper DB = new DBHelper(context);
		
		SQLiteDatabase db = DB.getWritableDatabase();
		  
		ContentValues values = new ContentValues();		  
		values.put("game_id", GAME_ID);
		values.put("date", DATE);
		values.put("expired_date", EX_DATE);
		values.put("playername", NAMES);
		values.put("file", FILE_PATH);
		  
		try
		{
			db.insert(DBHelper.DATABASE_TABLE_NAME_RESUME, null, values);						   
			Toast.makeText(context, "Inserted Successfully..", Toast.LENGTH_SHORT).show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		
		DB.close();
	}
}