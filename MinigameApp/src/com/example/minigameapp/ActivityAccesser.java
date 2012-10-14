package com.example.minigameapp;

public class ActivityAccesser {
	private RaceActivity ra;
	private static volatile ActivityAccesser instance = null;
	private float totalValues;

	public ActivityAccesser(){
		ra=null;
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
}
