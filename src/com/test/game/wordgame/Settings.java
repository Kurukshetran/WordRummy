package com.test.game.wordgame;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity 
{	
	// Stores names of traversed directories
	ArrayList<String> str = new ArrayList<String>();

	// Check if the first level of the directory structure is the one showing
	private Boolean firstLvl = true;

	private static final String WON_TAG = "WONF_PATH";
	private static final String LOSE_TAG = "LOSSF_PATH";

	private WonItem[] fileList_won;
	private LoseItem[] fileList_lose;
	
	private File Won_path = new File(Environment.getExternalStorageDirectory() + "");
	private File Lose_path = new File(Environment.getExternalStorageDirectory() + "");
	
	public static String Won_chosenFile;
	public static String Lose_chosenFile;
	
	private static final int DIALOG_LOAD_WONFILE = 1000;
	private static final int DIALOG_LOAD_LOSEFILE = 1001;
	
	String Temp_Won = "";
	String Temp_Lose = "";
	String Temp_Won_Recorder = "";
	String Temp_Lose_Recorder = "";
	
	Dialog dialog;
	
	int DIALOG_RECORDER = 1001;
	int DIALOG_STOP = 101;
	
	ListAdapter Wonadapter;
	ListAdapter Loseadapter;
	
	Spinner sp, time;
    ArrayAdapter<String> listadapter;
   
    public String[] Friends;
    public String FilePath;
    public int theme;
        
	protected Button Won_audioLocal, Won_audioRecorder, Lose_audioLocal, Lose_audioRecorder ;
	protected Button SaveButton;
		
	MediaRecorder Won_Recorder, Lose_Recorder;
	File Won_audiofile = null;
	File Lose_audiofile = null;
	private static final String RECORDER_TAG = "SoundRecordingActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    Utils.onActivityCreateSetTheme(this);
	    setContentView(R.layout.settings);
	 
	    Won_audioLocal = (Button) findViewById(R.id.wonaudiolocal);
	    Won_audioRecorder = (Button) findViewById(R.id.wonaudiorecorder);
	    
	    Lose_audioLocal = (Button) findViewById(R.id.loseaudiolocal);
	    Lose_audioRecorder = (Button) findViewById(R.id.loseaudiorecorder);
	    
	    SaveButton = (Button) findViewById(R.id.appsettingsave);
	    
	    sp = (Spinner) findViewById(R.id.themespinner);
	    
	    time = (Spinner) findViewById(R.id.timespinner);
	    
	    List<String> Time = new ArrayList<String>();
	    Time.add("5 Days");
	    Time.add("10 Days");
	    Time.add("15 Days");
        
        final ArrayAdapter<String> SaveTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Time);
        SaveTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(SaveTime);
          
        time.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
            public void onNothingSelected(AdapterView<?> arg0) 
            { 
            	Data.DAYS = 5;
            }
             
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) 
            {
            	switch (position)
         		{
	         		case 0:
	         			
	         			Data.DAYS = 5;           			
	           			break;
	         		case 1:
	         			
	         			Data.DAYS = 10; 			
	         			break;
	         		case 2:
	         			
	         			Data.DAYS = 15;
	         			break;
         		}
            }
        });
	    
	    List<String> Themes_Name = new ArrayList<String>();
	    Themes_Name.add("White");
	    Themes_Name.add("Black");
	    Themes_Name.add("Rose");
        
        final ArrayAdapter<String> themes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Themes_Name);
        themes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(themes);
          
        sp.setOnItemSelectedListener(new OnItemSelectedListener() 
        {
            public void onNothingSelected(AdapterView<?> arg0) 
            { 
            	theme = Utils.sTheme;
            }
             
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) 
            {
            	switch (position)
         		{
	         		case 0:
	         			
	           			theme = 0;
	           			//Utils.sTheme = theme;	           			
	           			break;
	         		case 1:
	         			
	           			theme = 1;
	           			//Utils.sTheme = theme;	           			
	         			break;
	         		case 2:
	         			
	           			theme = 2;
	           			//Utils.sTheme = theme;	           			
	         			break;
         		}
            }
        });	    
	     
	    Won_audioLocal.setOnClickListener(new View.OnClickListener() 
        {          
            public void onClick(View v) 
            {
            	LoadfileManager();
            	
            	showDialog(DIALOG_LOAD_WONFILE);
            	
        		Log.d(WON_TAG, Won_path.getAbsolutePath());
            }
        });
	    
	    Won_audioRecorder.setOnClickListener(new View.OnClickListener() 
        {          
            public void onClick(View v) 
            {
            	LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
            	View popupView = layoutInflater.inflate(R.layout.recorderpopup, null);
            	final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            	
            	final Button btnStart = (Button)popupView.findViewById(R.id.start);
            	final Button btnStop = (Button)popupView.findViewById(R.id.stop);
            	
            	btnStart.setOnClickListener(new Button.OnClickListener()
            	{
					public void onClick(View arg0) 
					{						
						try 
						{
							startRecording();
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					public void startRecording() throws IOException 
					{
						btnStart.setEnabled(false);
						btnStop.setEnabled(true);

						File sampleDir = Environment.getExternalStorageDirectory();
						
					    try 
					    {
					    	Won_audiofile = File.createTempFile("Won_sound", ".3gp", sampleDir);
					    } 
					    catch (IOException e) 
					    {
					    	Log.e(RECORDER_TAG, "sdcard access error");
					    	return;
					    }
					    
					    Won_Recorder = new MediaRecorder();
					    Won_Recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					    Won_Recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					    Won_Recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					    Won_Recorder.setOutputFile(Won_audiofile.getAbsolutePath());
					    
					    try 
					    {
					    	Won_Recorder.prepare();
						}  
					    catch (IOException e) 
					    {
							e.printStackTrace();
						}						    
					    Won_Recorder.start();						
					}            		
            	});
	    
            	btnStop.setOnClickListener(new Button.OnClickListener()
            	{
					public void onClick(View v) 
					{
						btnStart.setEnabled(true);
					    btnStop.setEnabled(false);
					    Won_Recorder.stop();
					    Won_Recorder.release();
					    addRecordingToMediaLibrary();
					    
					    popupWindow.dismiss();
					}
            	});
            	
            	popupWindow.showAsDropDown(Won_audioRecorder, 50, -30);
            }             
        });
	    
	    Lose_audioLocal.setOnClickListener(new View.OnClickListener() 
        {          
            public void onClick(View v) 
            {
            	LoadfileManager1();
            	
            	showDialog(DIALOG_LOAD_LOSEFILE);
            	
        		Log.d(LOSE_TAG, Lose_path.getAbsolutePath());
            }
        });
	    
	    Lose_audioRecorder.setOnClickListener(new View.OnClickListener() 
        {          
            public void onClick(View v) 
            {
            	LayoutInflater layoutInflater1 = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
            	View popupView = layoutInflater1.inflate(R.layout.recorderpopup, null);
            	final PopupWindow popupWindow1 = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            	
            	final Button btnStart1 = (Button)popupView.findViewById(R.id.start);
            	final Button btnStop1 = (Button)popupView.findViewById(R.id.stop);
            	
            	btnStart1.setOnClickListener(new Button.OnClickListener()
            	{
					public void onClick(View arg0) 
					{						
						try 
						{
							startRecording();
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					public void startRecording() throws IOException 
					{
						btnStart1.setEnabled(false);
						btnStop1.setEnabled(true);

						File sampleDir = Environment.getExternalStorageDirectory();
						
					    try 
					    {
					    	Lose_audiofile = File.createTempFile("Lose_sound", ".3gp", sampleDir);
					    } 
					    catch (IOException e) 
					    {
					    	Log.e(RECORDER_TAG, "sdcard access error");
					    	return;
					    }
					    
					    Lose_Recorder = new MediaRecorder();
					    Lose_Recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					    Lose_Recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					    Lose_Recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					    Lose_Recorder.setOutputFile(Lose_audiofile.getAbsolutePath());
					    
					    try 
					    {
					    	Lose_Recorder.prepare();
						}  
					    catch (IOException e) 
					    {
							e.printStackTrace();
						}						    
					    Lose_Recorder.start();						
					}            		
            	});
	    
            	btnStop1.setOnClickListener(new Button.OnClickListener()
            	{
					public void onClick(View v) 
					{
						btnStart1.setEnabled(true);
					    btnStop1.setEnabled(false);
					    Lose_Recorder.stop();
					    Lose_Recorder.release();
					    addRecordingToMediaLibrary1();
					    
					    popupWindow1.dismiss();
					}
            	});
            	
            	popupWindow1.showAsDropDown(Lose_audioRecorder, 50, -30);
            }             
        });
	    
	    SaveButton.setOnClickListener(new View.OnClickListener()
	    {			
			public void onClick(View arg0)
			{			
       			Utils.sTheme = theme;
       			
       			if(Temp_Won == "" && Temp_Lose == "")
       			{
       				Data.WON_PATH = Temp_Won_Recorder;
       				Data.LOSE_PATH = Temp_Lose_Recorder;
       			}
       			
       			if(Temp_Won == "" && Temp_Lose != "")
       			{
       				Data.WON_PATH = Temp_Won_Recorder;
       				Data.LOSE_PATH = Temp_Lose;
       			}
       			
       			if(Temp_Won != "" && Temp_Lose == "")
       			{
       				Data.WON_PATH = Temp_Won;
       				Data.LOSE_PATH = Temp_Lose_Recorder;
       			}
       			
       			if(Temp_Won != "" && Temp_Lose != "")
       			{
       				Data.WON_PATH = Temp_Won;
       				Data.LOSE_PATH = Temp_Lose;
       			}
       			
				Intent i = new Intent(Settings.this, MainMenu.class);  
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}
		});
	}
	
	public void LoadfileManager()
	{
		try 
		{
			Won_path.mkdirs();
		} 
		catch (SecurityException e) 
		{
			Log.e(WON_TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (Won_path.exists()) 
		{
			FilenameFilter filter = new FilenameFilter() 
			{
				public boolean accept(File dir, String filename) 
				{
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return (sel.isFile() || sel.isDirectory()) && !sel.isHidden();
				}
			};

			String[] fList = Won_path.list(filter);
			fileList_won = new WonItem[fList.length];
			
			for (int i = 0; i < fList.length; i++) 
			{
				fileList_won[i] = new WonItem(fList[i], R.drawable.file_icon);

				// Convert into file path
				File sel = new File(Won_path, fList[i]);

				// Set drawables
				if (sel.isDirectory()) 
				{
					fileList_won[i].icon = R.drawable.directory_icon;
					Log.d("DIRECTORY", fileList_won[i].file);
				} 
				else 
				{
					Log.d("FILE", fileList_won[i].file);
				}
			}

			if (!firstLvl) 
			{
				WonItem temp[] = new WonItem[fileList_won.length + 1];
				
				for (int i = 0; i < fileList_won.length; i++) 
				{
					temp[i + 1] = fileList_won[i];
				}
				temp[0] = new WonItem("Up", R.drawable.directory_up);
				fileList_won = temp;
			}
		} 
		else 
		{
			Log.e(WON_TAG, "path does not exist");
		}

		Wonadapter = new ArrayAdapter<WonItem>(this, android.R.layout.select_dialog_item, android.R.id.text1, fileList_won) 
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				// creates view
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view.findViewById(android.R.id.text1);

				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(fileList_won[position].icon, 0, 0, 0);

				// add margin between image and text (support various screen densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);

				return view;
			}
		};
	}
	
	private class WonItem 
	{
		public String file;
		public int icon;

		public WonItem(String file, Integer icon) 
		{
			this.file = file;
			this.icon = icon;
		}

		@Override
		public String toString() 
		{
			return file;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) 
	{
		Dialog dialog = null;
		AlertDialog.Builder builder = new Builder(this);

		if (fileList_won == null) 
		{
			Log.e(WON_TAG, "No files loaded");			
			dialog = builder.create();			
			return dialog;
		}

		switch (id) 
		{
			case DIALOG_LOAD_WONFILE:
				builder.setTitle("Choose your file");
				builder.setAdapter(Wonadapter, new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						Won_chosenFile = fileList_won[which].file;
						File sel = new File(Won_path + "/" + Won_chosenFile);
						
						if (sel.isDirectory()) 
						{
							firstLvl = false;
	
							// Adds chosen directory to list
							str.add(Won_chosenFile);
							fileList_won = null;
							Won_path = new File(sel + "");
	
							LoadfileManager();
	
							removeDialog(DIALOG_LOAD_WONFILE);
							showDialog(DIALOG_LOAD_WONFILE);
							Log.d(WON_TAG, Won_path.getAbsolutePath());	
						}
	
						// Checks if 'up' was clicked
						else if (Won_chosenFile.equalsIgnoreCase("up") && !sel.exists()) 
						{
							// present directory removed from list
							String s = str.remove(str.size() - 1);
	
							// path modified to exclude present directory
							Won_path = new File(Won_path.toString().substring(0, Won_path.toString().lastIndexOf(s)));
							fileList_won = null;
	
							// if there are no more directories in the list, then its the first level
							if (str.isEmpty()) 
							{
								firstLvl = true;
							}
							LoadfileManager();
	
							removeDialog(DIALOG_LOAD_WONFILE);
							showDialog(DIALOG_LOAD_WONFILE);
							Log.d(WON_TAG, Won_path.getAbsolutePath());	
						}
						// File picked
						else 
						{
							Temp_Won = Won_path.toString()+"/"+Won_chosenFile;
							Log.v("FILEPATH", Temp_Won);
							Toast.makeText(getApplicationContext(), Temp_Won, Toast.LENGTH_LONG).show();
						}	
					}
				});
				break;
				
			case DIALOG_LOAD_LOSEFILE:
				builder.setTitle("Choose your file");
				builder.setAdapter(Loseadapter, new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						Lose_chosenFile = fileList_lose[which].file;
						File sel = new File(Lose_path + "/" + Lose_chosenFile);
						
						if (sel.isDirectory()) 
						{
							firstLvl = false;
	
							// Adds chosen directory to list
							str.add(Lose_chosenFile);
							fileList_lose = null;
							Lose_path = new File(sel + "");
	
							LoadfileManager1();
	
							removeDialog(DIALOG_LOAD_LOSEFILE);
							showDialog(DIALOG_LOAD_LOSEFILE);
							Log.d(LOSE_TAG, Lose_path.getAbsolutePath());
	
						}
	
						// Checks if 'up' was clicked
						else if (Lose_chosenFile.equalsIgnoreCase("up") && !sel.exists()) 
						{
							// present directory removed from list
							String s = str.remove(str.size() - 1);
	
							// path modified to exclude present directory
							Lose_path = new File(Lose_path.toString().substring(0, Lose_path.toString().lastIndexOf(s)));
							fileList_lose = null;
	
							// if there are no more directories in the list, then its the first level
							if (str.isEmpty()) 
							{
								firstLvl = true;
							}
							LoadfileManager1();
	
							removeDialog(DIALOG_LOAD_LOSEFILE);
							showDialog(DIALOG_LOAD_LOSEFILE);
							Log.d(LOSE_TAG, Lose_path.getAbsolutePath());	
						}
						// File picked
						else 
						{
							Temp_Lose = Lose_path.toString()+"/"+Lose_chosenFile;
							Log.v("FILEPATH", Temp_Lose);
							Toast.makeText(getApplicationContext(), Temp_Lose, Toast.LENGTH_LONG).show();
						}	
					}
				});
				break;
		}
		dialog = builder.show();
		return dialog;
	}
	
	protected void addRecordingToMediaLibrary() 
	{
	    ContentValues values = new ContentValues(4);
	    long current = System.currentTimeMillis();
	    values.put(MediaStore.Audio.Media.TITLE, "audio" + Won_audiofile.getName().toString());
	    //values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
	    values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
	    values.put(MediaStore.Audio.Media.DATA, Won_audiofile.getAbsolutePath());
	    ContentResolver contentResolver = getContentResolver();

	    Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	    Uri newUri = contentResolver.insert(base, values);
	    Temp_Won_Recorder = newUri.toString();
	    Won_audioLocal.setEnabled(false);
	    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	    Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
	}
	
	public void LoadfileManager1()
	{
		try 
		{
			Lose_path.mkdirs();
		} 
		catch (SecurityException e) 
		{
			Log.e(LOSE_TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (Lose_path.exists()) 
		{
			FilenameFilter filter = new FilenameFilter() 
			{
				public boolean accept(File dir, String filename) 
				{
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return (sel.isFile() || sel.isDirectory()) && !sel.isHidden();
				}
			};

			String[] fList = Lose_path.list(filter);
			fileList_lose = new LoseItem[fList.length];
			
			for (int i = 0; i < fList.length; i++) 
			{
				fileList_lose[i] = new LoseItem(fList[i], R.drawable.file_icon);

				// Convert into file path
				File sel = new File(Lose_path, fList[i]);

				// Set drawables
				if (sel.isDirectory()) 
				{
					fileList_lose[i].icon = R.drawable.directory_icon;
					Log.d("DIRECTORY", fileList_lose[i].file);
				} 
				else 
				{
					Log.d("FILE", fileList_lose[i].file);
				}
			}

			if (!firstLvl) 
			{
				LoseItem temp[] = new LoseItem[fileList_lose.length + 1];
				
				for (int i = 0; i < fileList_lose.length; i++) 
				{
					temp[i + 1] = fileList_lose[i];
				}
				temp[0] = new LoseItem("Up", R.drawable.directory_up);
				fileList_lose = temp;
			}
		} 
		else 
		{
			Log.e(LOSE_TAG, "path does not exist");
		}

		Loseadapter = new ArrayAdapter<LoseItem>(this, android.R.layout.select_dialog_item, android.R.id.text1, fileList_lose) 
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				// creates view
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view.findViewById(android.R.id.text1);

				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(fileList_lose[position].icon, 0, 0, 0);

				// add margin between image and text (support various screen densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);

				return view;
			}
		};
	}
	
	private class LoseItem 
	{
		public String file;
		public int icon;

		public LoseItem(String file, Integer icon) 
		{
			this.file = file;
			this.icon = icon;
		}

		@Override
		public String toString() 
		{
			return file;
		}
	}
	
	protected void addRecordingToMediaLibrary1() 
	{
	    ContentValues values = new ContentValues(4);
	    long current = System.currentTimeMillis();
	    values.put(MediaStore.Audio.Media.TITLE, "audio" + Lose_audiofile.getName().toString());
	    //values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
	    values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
	    values.put(MediaStore.Audio.Media.DATA, Lose_audiofile.getAbsolutePath());
	    ContentResolver contentResolver = getContentResolver();

	    Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	    Uri newUri = contentResolver.insert(base, values);
	    Temp_Lose_Recorder = newUri.toString();
	    Lose_audioLocal.setEnabled(false);
	    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	    Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
	}
}