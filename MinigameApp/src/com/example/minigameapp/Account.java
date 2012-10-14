package com.example.minigameapp;

public class Account {
	private String name;
	private int score;
	private static volatile Account instance = null;

	public Account(){
		name="";
		score=0;
	}

	public static Account getInstance(){
		if (instance == null) {
			synchronized (Account.class){
				if (instance == null) {
					instance = new Account();
				}
			}
		}
		return instance;
	}
	
	public void setName(String s){
		this.name=s;
	}
	
	public void incScore(){
		score++;
	}
	
	public void decScore(){
		score--;
	}
	
	public String getName(){
		return name;
	}
	
	public int getScore(){
		return score;
	}
	
}
