package com.test.game.wordgame;

import java.io.Serializable;

public class ResumePackage implements Serializable
{
	private static final long serialVersionUID = 123;
	
	/*VARIABLES FOR GAME PARAMETERS*/
	public String DECKS;
	public int NUM_OF_DECKS;
	public int NUM_OF_CARDS;
	public int SET_OF_CARDS;
	public int NUM_OF_PLAYERS;
	public int OPENCARD;
	public int CLOSECARD;
	public int CURRENT_PLAYER;
	public int ACTIVITY_ID;
	
	/*PLAYERS INDIVIDUAL DATAS*/
	public String[] PLAYERS_NAME;
	public String[] PLAYERS_PASSWORD;
	public String[] PLAYERS_IMAGE;
	public int[] ID;
	public int[] C_TIME;
	public int[] T_TIME;
	public int[] ROUNDS;
	public int[] WRONG_DECLARE_PLAYER;
}
