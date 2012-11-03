package com.example.minigameapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActivityAccesser {
	private RaceActivity ra;
	private FootballActivity fa;
	private ChopWoodActivity cwa;
	private static volatile ActivityAccesser instance = null;
	private float totalValues;
	private String[] activities = {"Race","Football"};

	public ActivityAccesser(){
		totalValues=0;
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
		int x = (int)(Math.random()*2+1);
		if(x==1)
			return new Intent(context, RaceActivity.class);
		else if(x==2)
			return new Intent(context, FootballActivity.class);
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
}
