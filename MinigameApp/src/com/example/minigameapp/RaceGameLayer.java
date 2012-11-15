package com.example.minigameapp;

import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.util.Log;

public class RaceGameLayer extends CCColorLayer{
	private CCSprite _player;
	private CCSprite _computer;
	private CCSprite background;
	private Account a;
	private static RaceActivity raceActivity;
	private static float prev;
	
	public static CCScene scene()
	{
		prev=0;
		ActivityAccesser ac = ActivityAccesser.getInstance();
		raceActivity=ac.getRaceActivity();
	    CCScene scene = CCScene.node();
	    CCColorLayer layer = new RaceGameLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
	
	protected RaceGameLayer(ccColor4B color)
	{
	    super(color);
	    this.setIsTouchEnabled(true);
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
	    
	    _player = CCSprite.sprite("race_car2.png");
	    _computer = CCSprite.sprite("race_car.png");
	    _player.setPosition(CGPoint.ccp(_player.getContentSize().width / 2.0f, winSize.height / 2.0f));
	    _computer.setPosition(CGPoint.ccp(_computer.getContentSize().width / 2.0f,winSize.height / 2.0f + _player.getContentSize().height*3));

	    background = CCSprite.sprite("racebackground.png");
	    //background.setTag(1);
	    background.setAnchorPoint(0, 0);
	    addChild(background);
	 
	    a=Account.getInstance();
	    //this.act=act;

	    addChild(_player);
	    addChild(_computer);
	    moveComputer();
	    schedule("checkFinished");
	    schedule("movePlayer");
	}
	public void moveComputer(){
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		float finalX=winSize.width;
		
	    Log.d("GameLayer","Set Action");
	    CGPoint point = CGPoint.ccp(finalX,winSize.height / 2.0f + _player.getContentSize().height*3);
	    CCMoveTo actionMove = CCMoveTo.action(10, point);
	 	_computer.runAction(actionMove);
	    Log.d("GameLayer", "Start Moving");
	}
	
	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
	    this.removeChild(_computer, true);
		Log.d("GameLayer", "Remove Sprite");
	}
	
	public void checkFinished(float dt){
		CGPoint computerPos = _computer.getPosition();
		CGPoint playerPos = _player.getPosition();
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		background.setContentSize(winSize.width, winSize.height);
		float finalX=winSize.width-_player.getContentSize().width;
		if((computerPos.x-_computer.getContentSize().width)>=finalX){
			Log.d("checkFinished","Computer Wins");
			a.decScore();
			ActivityAccesser.getInstance().setCompWin(true);
			raceActivity.finish();
		}
		else if((playerPos.x-_player.getContentSize().width)>=finalX){
			Log.d("checkFinished","Player Wins");
			a.incScore();
			ActivityAccesser.getInstance().setCompWin(false);
			raceActivity.finish();
		}
	}
	
	public void movePlayer(float dt){
		ActivityAccesser a = ActivityAccesser.getInstance();
		float curr = a.getValues();
		float distance = curr-prev;
		//Log.d("Curr",curr+"");
		//Log.d("Prev",prev+"");
		//Log.d("Distance",distance+"");
		if(prev==0){
			prev=curr;
			return;
		}
		prev=curr;
		
		CGPoint playerPos = _player.getPosition();
		//Log.d("x",playerPos.x+"");
		CGPoint newPoint = CGPoint.ccp(playerPos.x+Math.abs(distance), playerPos.y);
		_player.setPosition(newPoint);
	}
}
