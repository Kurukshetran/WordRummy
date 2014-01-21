package com.test.game.wordgame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper 
{
	static final String DATABASE_NAME = "PlayerInfo.db";
	public static final String DATABASE_TABLE_NAME_USERINFO = "PlayerData";
	public static final String DATABASE_TABLE_NAME_WORDINFO = "wordlists";
	public static final String DATABASE_TABLE_NAME_RESUME = "Resume";
	private static final int DATABASE_VERSION = 1;   
	String DB_PATH =null;
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public DBHelper(Context context) 
	{		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		System.out.println("In constructor");	
	}
	
    public void createDataBase() throws IOException
    {    	 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist)
    	{
    		//do nothing - database already exist
    	}
    	else
    	{ 
        	this.getReadableDatabase();
 
        	try 
        	{ 
    			copyDataBase();
    			
    			System.out.println("In onCreate");
 
    		}
        	catch (IOException e) 
        	{ 
        		throw new Error("Error copying database"); 
        	}
    	} 
    }
 
    private boolean checkDataBase()
    {
    	DB_PATH="/data/data/"+myContext.getPackageName()+"/"+"databases/";
    	
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e)
    	{ 
    		//database does't exist yet. 
    	}
 
    	if(checkDB != null)
    	{ 
    		checkDB.close(); 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    private void copyDataBase() throws IOException
    { 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DATABASE_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	
    	while ((length = myInput.read(buffer))>0)
    	{
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close(); 
    }
 
    public void openDataBase() throws SQLException
    { 
        String myPath = DB_PATH + DATABASE_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); 
    }
 
    @Override
	public synchronized void close() 
    { 
    	if(myDataBase != null)
    		myDataBase.close();
 
    	super.close(); 
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) 
	{
	}
}
