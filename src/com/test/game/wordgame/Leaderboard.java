package com.test.game.wordgame;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Leaderboard extends Activity 
{	
	DBHelper playerDB = new DBHelper(Leaderboard.this);
	TableLayout leaderboard_table;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    Utils.onActivityCreateSetTheme(this);
	    setContentView(R.layout.leaderboard);
	    leaderboard_table=(TableLayout)findViewById(R.id.leaderboard);

	    RetrieveData();
	}
	
	public void RetrieveData()
	{
		try
		{			
			SQLiteDatabase db = playerDB.getReadableDatabase();
			
			String[] columns = {"_id","player_username", "player_played","player_won","player_loss","player_points"};
			
			Cursor cursor = db.query(DBHelper.DATABASE_TABLE_NAME_USERINFO, columns, null, null, null, null, null);
			
			if(cursor != null)
			{
				System.out.println("database showing");
				startManagingCursor(cursor);
				showDatabase(cursor);
			}
			System.out.println("Cursor NuLL");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void showDatabase(Cursor cursor) 
	{
		String id = null;
		String name = null;
		String played = null;
		String won = null;
		String loss = null;
		String points = null;
				
		while (cursor.moveToNext()) 
		{	
	       id = cursor.getString(0);	
		   name = cursor.getString(1);
	       played = cursor.getString(2);	      
	       won = cursor.getString(3);
	       loss = cursor.getString(4);
	       points = cursor.getString(5);
	       
	       TableRow row;
		   TextView t1, t2,t3,t4,t5,t6;
			
			 //Converting to dip unit
		   int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(float) 1, getResources().getDisplayMetrics());
	   
	       row = new TableRow(this);
	       
	       t1 = new TextView(this);
	       t2 = new TextView(this);
	       t3 = new TextView(this);
	       t4 = new TextView(this);
	       t5 = new TextView(this);
	       t6 = new TextView(this);
	         
	       t1.setTextSize(18);
	       t2.setTextSize(18);
	       t3.setTextSize(18);
	       t4.setTextSize(18);
	       t5.setTextSize(18);
	       t6.setTextSize(18);
	       
	       t1.setTextColor(Color.BLUE);
	       t2.setTextColor(Color.BLUE);
	       t3.setTextColor(Color.BLUE);
	       t4.setTextColor(Color.BLUE);
	       t5.setTextColor(Color.BLUE);
	       t6.setTextColor(Color.BLUE);
	         
	       t1.setText(id);
	       t2.setText(name);
	       t3.setText(played);
	       t4.setText(won);
	       t5.setText(loss);
	       t6.setText(points);
	       
	       t1.setWidth(50 * dip);
	       t2.setWidth(120 * dip);
	       t3.setWidth(80 * dip);
	       t4.setWidth(80 * dip);
	       t5.setWidth(80 * dip);
	       t6.setWidth(100 * dip);
	       
	       t1.setPadding(20*dip, 0, 0, 0);
	       t2.setPadding(20*dip, 0, 0, 0);
	       t3.setPadding(20*dip, 0, 0, 0);
	       t4.setPadding(20*dip, 0, 0, 0);
	       t5.setPadding(20*dip, 0, 0, 0);
	       t6.setPadding(20*dip, 0, 0, 0);
	       
	       row.addView(t1);
	       row.addView(t2);
	       row.addView(t3);
	       row.addView(t4);
	       row.addView(t5);
	       row.addView(t6);
	       
	       leaderboard_table.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

	    }		             
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		playerDB.close();
	}
}
