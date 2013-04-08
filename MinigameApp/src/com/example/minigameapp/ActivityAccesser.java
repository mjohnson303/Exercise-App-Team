package com.example.minigameapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActivityAccesser {
	private RaceActivity ra;
	private FootballActivity fa;
	private ChopWoodActivity cwa;
	private JumpingJackActivity jja;
	private RGLightActivity la;
	private BoxingActivity ba;
	private SpaceActivity sa;
	private ShuffleboardActivity shuffle;
	private static volatile ActivityAccesser instance = null;
	private float totalValues;
	private String[] activities = {"Race","Football","Chop Wood","Jumping Jacks","Red Light, Green Light","Boxing","Space"};
	private boolean compWin;
	private boolean playedGame;

	public ActivityAccesser(){
		totalValues=0;
		playedGame=false;
	}

	public static ActivityAccesser getInstance(){
		if (instance == null) {
			synchronized (Account.class){
				if (instance == null) {
					instance = new ActivityAccesser();
				}
			}
		}
		return instance;
	}
	
	public void setRaceActivity(RaceActivity ractivity){
		ra=ractivity;
	}
	
	public RaceActivity getRaceActivity(){
		return ra;
	}
	
	public void setValues(float x){
		totalValues=x;
	}
	
	public float getValues(){
		return totalValues;
	}

	public FootballActivity getFootballActivity() {
		return fa;
	}

	public void setFootballActivity(FootballActivity fa) {
		this.fa = fa;
	}

	public String[] getActivities() {
		return activities;
	}

	public void setActivities(String[] activities) {
		this.activities = activities;
	}
	
	public Intent getRandomActivity(Context context){
		int x = (int)(Math.random()*7+1);
		if(x==1)
			return new Intent(context, RaceActivity.class);
		else if(x==2)
			return new Intent(context, FootballActivity.class);
		else if(x==3)
			return new Intent(context, ChopWoodActivity.class);
		else if(x==4)
			return new Intent(context, JumpingJackActivity.class);
		else if(x==5)
			return new Intent(context, RGLightActivity.class);
		else if(x==6)
			return new Intent(context, BoxingActivity.class);
		else if(x==7)
			return new Intent(context, SpaceActivity.class);
		else if(x==7)
			return new Intent(context, ShuffleboardActivity.class);
		else{
			Log.e("ActivityAccesser", "getRandomActivity failed");
			return new Intent(context, RaceActivity.class);
		}
	}

	public ChopWoodActivity getChopWoodActivity() {
		return cwa;
	}

	public void setChopWoodActivity(ChopWoodActivity cwa) {
		this.cwa = cwa;
	}

	public JumpingJackActivity getJumpingJackActivity() {
		return jja;
	}

	public void setJumpingJackActivity(JumpingJackActivity jja) {
		this.jja = jja;
	}

	public boolean isCompWin() {
		return compWin;
	}

	public void setCompWin(boolean compWin) {
		this.compWin = compWin;
	}

	public RGLightActivity getLa() {
		return la;
	}

	public void setLa(RGLightActivity la) {
		this.la = la;
	}

	public boolean isPlayedGame() {
		return playedGame;
	}

	public void setPlayedGame(boolean playedGame) {
		this.playedGame = playedGame;
	}

	public BoxingActivity getBa() {
		return ba;
	}

	public void setBa(BoxingActivity ba) {
		this.ba = ba;
	}

	public SpaceActivity getSpaceActivity() {
		return sa;
	}

	public void setSpaceActivity(SpaceActivity sa) {
		this.sa = sa;
	}

	public ShuffleboardActivity getShuffle() {
		return shuffle;
	}

	public void setShuffle(ShuffleboardActivity shuffle) {
		this.shuffle = shuffle;
	}
}
