package com.test.game.wordgame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.net.Uri;

class Data 
{		
	public static String shuffledDecks = "";
	public static int numOfdecks = 3 ;//
	public static int numOfCards =  26 * numOfdecks;
	public static int numOfPlayers = 4;//
	public static int currentPlayer = 0;
	public static int setOfCards = 3;//
	public static int words  = (setOfCards * 3);
	public static int ButtonArray = words + 4;
	public static int OpenCard ;
	public static int CloseCard ;
	static String UnshuffledDecks ;
	public static int n = numOfCards - (numOfPlayers * words);
	
	public static String WON_PATH = "";
	public static String LOSE_PATH = "";
	
	public static int NotOut_Players;

	public static int[] Row = new int[numOfPlayers];
	
	public static int Check_count = 0;
	
	public static int ACTIVITY = 1;
	public static int LOGIN_ACTIVITY = 1;
	public static int PICK_ACTIVITY = 2;
	public static int BUILD_ACTIVITY = 3;
	
	public static int DAYS = 5;
	
	public static String[] name = new String[Data.numOfPlayers];	
	public static String[] pass= new String[Data.numOfPlayers];	
	public static String[] path = new String[Data.numOfPlayers];
	public static int[] Player_ID = new int[Data.numOfPlayers];
	
	public static String I_PATH = "";
	public static Uri fileUri  = null;
	
	public static String random()
	{
		DataDeclaration();
		
		UnshuffledDecks="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    int cnter=0;
		Random ran=new Random();
		int x;
		int y;
	   
		int counter;
		int sto[]=new int[numOfCards];
		int ref[]=new int[numOfCards];
		String deshuffledDecks="";
				
	    for (int i = 0; i <numOfdecks; i++)
		{
			deshuffledDecks += UnshuffledDecks; 
		}
	
		char[] shufstringarray = new char[numOfCards];
		char[] unshufstringarray = new char[numOfCards];
		
		shufstringarray=deshuffledDecks.toCharArray();
		unshufstringarray=UnshuffledDecks.toCharArray();
		
		label1:
			while(cnter<numOfCards)
			{
				sto[cnter]=ran.nextInt(numOfCards);
				x=cnter;
				counter=0;
				
				for(y=0;y<=x;y++)
				{
					if(sto[x]!=ref[y] && counter==0)
					{
						ref[x]=sto[x];
						counter++;
				    }/// IF CLOSE
				
					else
					{
						if(sto[x]==ref[y] && y!=cnter)
						{
						  continue label1;
						}
						
						else if(sto[x]==ref[y] && y==cnter)
						{
						  ref[x]=sto[x];
						}
						
					}//ELSE CLOSE
					
		    	}///for loop close
				
				cnter++;
					
			}
		//--------------for get Random string	
		for(int i=0;i<numOfCards;i++)
		{
			int j=ref[i];
			int s;
			int d=26;
			int temp = (int) (j*0.0384615384615385);
			s = (j-(temp*d));
			
		    shufstringarray[i]=unshufstringarray[s];							
		}
		
		deshuffledDecks =new String(shufstringarray);
		return deshuffledDecks;				
	}
	
	private static void DataDeclaration()
	{		
		 words = (setOfCards * 3);
		 OpenCard = numOfPlayers  * words;
		 CloseCard = numOfPlayers * words;
		 
		 NotOut_Players = numOfPlayers ;

		 n = numOfCards - (numOfPlayers * words);	
		
		 name = new String[Data.numOfPlayers];	
		 pass = new String[Data.numOfPlayers];	
		 path = new String[Data.numOfPlayers];
		 Player_ID = new int[Data.numOfPlayers];
	}
	
	public static String randomForDrop()
	{	    
	    UnshuffledDecks = shuffledDecks.substring(OpenCard);	    
	    
		int cnter=0;
		
		Random ran=new Random();
		int x;
		int y;
	   
		int counter;
		int sto[]=new int[n];
		int ref[]=new int[n];
		String deshuffledDecks="";				
	    
		deshuffledDecks += UnshuffledDecks;		
	
		char[] shufstringarray = new char[n];
		char[] unshufstringarray = new char[n];
		
		shufstringarray=deshuffledDecks.toCharArray();
		unshufstringarray=UnshuffledDecks.toCharArray();
		label1:
			while(cnter<n)
			{
				sto[cnter]=ran.nextInt(n);
				x=cnter;
				counter=0;
				
				for(y=0;y<=x;y++)
				{
					if(sto[x]!=ref[y] && counter==0)
					{
						ref[x]=sto[x];
						counter++;
				    }
				
					else
					{
						if(sto[x]==ref[y] && y!=cnter)
						{
						  continue label1;
						}
						
						else if(sto[x]==ref[y] && y==cnter)
						{
						  ref[x]=sto[x];
						}						
					}					
		    	}
				
				cnter++;					
			}
			
		for(int i=0;i<n;i++)
		{
			int j=ref[i];
			shufstringarray[i]=unshufstringarray[j];								
		}
		deshuffledDecks =new String(shufstringarray);
		return deshuffledDecks;				
	}
	
	public static void CPSuffled()
	{
		boolean Overflow = false;
				
		while(Overflow == false)
		{
			Random ran1 = new Random();
			currentPlayer  = ran1.nextInt(numOfPlayers);
			
			if(currentPlayer >= numOfPlayers)
			{
				Overflow = false;
			}
			else
			{
				Overflow = true;
				break;
			}
		}
	}

	public static void GameReset()
	{
		Row[currentPlayer]=0;
		Data.shuffledDecks = "";
		NotOut_Players = numOfPlayers;
			
		for(int k = 0; k < numOfPlayers; k++)
		{
			Timer.cummulativeTime[k] = 0;
			Timer.currentTime[k] = 0;
			Timer.rounds[k] = 0;
			WrongDeclare.WrongDeclarePlayer[k] = 0;
						
		}
	}
	
	public static String No_of_Days(String Date)
	{
	    String format = "MM/dd/yy";

	    SimpleDateFormat sdf = new SimpleDateFormat(format);
         
	    Date dateObj1 = null;
		try 
		{
			dateObj1 = sdf.parse(Date);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
	    
		Calendar c = Calendar.getInstance();
		c.setTime(dateObj1);
		c.add(Calendar.DATE, DAYS);
		String output = sdf.format(c.getTime());
		
		return output;
	}
}