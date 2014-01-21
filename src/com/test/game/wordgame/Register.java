package com.test.game.wordgame;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener 
{	
	private Button bRegisterButton, bTakePhoto,bPickPhoto;
	 
	private static EditText eUsername;
	private static EditText ePassword;
		
	public static File mediaFile, mediaStorageDir;
	
	public static final int MEDIA_TYPE_IMAGE = 1;

	public static String IMAGE_TAG = "User";
	public static String APPEND_TAG;
	
	int CAMERA_PIC_REQUEST = 1337;
	int GALLERY_PIC_REQUEST = 0;
	
	private boolean imgCapFlag = false;
	protected boolean taken = false;
	protected boolean Default_Image = true;
	protected static final String PHOTO_TAKEN = "photo_taken";
		 
	protected DBHelper playerDB = new DBHelper(Register.this);
	 
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    Utils.onActivityCreateSetTheme(this);
	    setContentView(R.layout.register);
	    
		eUsername = (EditText)findViewById(R.id.enterUsername);
		ePassword = (EditText)findViewById(R.id.enterPassword);
	    
	    if(Data.fileUri != null)
	    {
	    	Data.I_PATH = Data.fileUri.getPath();
	    }
	    	    
	    bTakePhoto = (Button) findViewById(R.id.takePhoto);
	    bTakePhoto.setOnClickListener(this);
	    
	    bPickPhoto = (Button) findViewById(R.id.addPhoto);
	    bPickPhoto.setOnClickListener(this);
	    
	    bRegisterButton = (Button)findViewById(R.id.addRegister);
	    bRegisterButton.setOnClickListener(this);
	}
	
	public void onClick(View v) 
	{	  
		switch(v.getId())
		{		  
			case R.id.takePhoto:				
				
			    Data. fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);	
			    Data.I_PATH = Data.fileUri.getPath();			    	    
			    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Data.fileUri);			      
			    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);				
		    break;
			  
			case R.id.addPhoto:
				  
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, GALLERY_PIC_REQUEST);
			break;
			   
			case R.id.addRegister:
				 					   
				String IMAGE_PATH = Data.I_PATH;	   
				String USER_NAME = eUsername.getText().toString();
				String PASSWORD = ePassword.getText().toString();
				String PLAYED = "0";
				String WON = "0";
				String LOSS = "0";
				String STATUS = "0";
				String POINTS = "1000";
					   
				boolean invalid = false;
					      	         
				if(USER_NAME.equals(""))
				{
					invalid = true;
					Toast.makeText(getApplicationContext(), "Please enter Username", Toast.LENGTH_SHORT).show();
				}
				else if(PASSWORD.equals(""))
				{
					invalid = true;
					Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
				}
				
				//if(IMAGE_PATH.equals(""))
				//{
					//invalid = true;
					//Toast.makeText(getApplicationContext(), "Choose Picture", Toast.LENGTH_SHORT).show();
				//}
				else if(invalid == false)
				{
					if(Default_Image == false)
					{
						if(taken == false)
						{
							IMAGE_PATH = Data.I_PATH;
						}
						else
						{
							IMAGE_PATH = "/mnt/sdcard/ShuffledWords/MyCameraApp/"+  USER_NAME +"_"+PASSWORD +".png";
						}
					}
					else
					{
						IMAGE_PATH = "/mnt/sdcard/ShuffledWords/MyCameraApp/user.png";
					}
					addEntry(IMAGE_PATH, USER_NAME, PASSWORD, PLAYED, WON, LOSS, STATUS, POINTS);
					Intent i_register = new Intent(this, MainMenu.class);
					i_register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i_register);
					finish();
				}		      
			break;
		}
	}	 
	
	 private void addEntry(String path, String uname, String pass, String played, String won, String loss,String status, String points) 
	 {		 
		  SQLiteDatabase db = playerDB.getWritableDatabase();
		  
		  ContentValues values = new ContentValues();
		  
		  values.put("player_photo", path);
		  values.put("player_username", uname);
		  values.put("player_pass", pass);
		  values.put("player_played", played);
		  values.put("player_won", won);
		  values.put("player_loss", loss);
		  values.put("player_status", loss);
		  values.put("player_points",points);
		  
		  try
		  {
			  db.insert(DBHelper.DATABASE_TABLE_NAME_USERINFO, null, values);
			  Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
	 }
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {	 
		 if(requestCode == GALLERY_PIC_REQUEST)
		 {
			 if (resultCode == RESULT_OK)
			 {
				 Default_Image = false;
				 bTakePhoto.setEnabled(false);
				 Uri targetUri = data.getData();			 
				 
				 String[] filePathColumn = { MediaStore.Images.Media.DATA };
	
				 Cursor cursor = getContentResolver().query(targetUri, filePathColumn, null, null, null);
				 cursor.moveToFirst();
	
				 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				 Data.I_PATH = cursor.getString(columnIndex);
				 cursor.close();
			 }
			 else if(resultCode == RESULT_CANCELED)
			 {				 
			 }
			 else
			 {				 
			 }
		 }
		 
		 if(requestCode == CAMERA_PIC_REQUEST)
		 {
			 if(resultCode == RESULT_OK)
			 {
				 Default_Image = false;
				 taken = true;
				 bPickPhoto.setEnabled(false);
				 //Toast.makeText(Register.this, Data.I_PATH, Toast.LENGTH_SHORT).show();
			 }
			 else if(resultCode == RESULT_CANCELED)
			 {				 
			 }
			 else
			 {				 
			 }
		 }
	 }

	 private static Uri getOutputMediaFileUri(int type)
	 {
		 return Uri.fromFile(getOutputMediaFile(type));
	 }

	 private static File getOutputMediaFile(int type)
	 {		 
		 mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory("ShuffledWords"), "MyCameraApp");

	     if (! mediaStorageDir.exists())
	     {
	         if (! mediaStorageDir.mkdirs())
	         {
	             Log.v("MyCameraApp", "failed to create directory");
	             return null;
	         }
	     }
	    
	     if (type == MEDIA_TYPE_IMAGE)
	     {	    	 
	         Log.v("PATH", Data.I_PATH);

	         String USER_NAME = eUsername.getText().toString();
	         String PASSWORD = ePassword.getText().toString(); 
	    	 mediaFile = new File(mediaStorageDir.getPath() + File.separator +  USER_NAME +"_"+PASSWORD+ ".png");
	     } 
	     else 
	     {
	         return null;
	     }
	     
	     return mediaFile;
	 }
	 
	 public void onDestroy()
	 {
         super.onDestroy();
		 playerDB.close();
	 }
}