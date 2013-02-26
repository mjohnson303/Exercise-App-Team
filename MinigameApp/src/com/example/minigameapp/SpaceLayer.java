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

public class SpaceLayer extends CCColorLayer{
	private CCSprite _player;
	private CCSprite background;
	private Account a;
	private static SpaceActivity spaceActivity;
	private static float prev;
	
	public static CCScene scene()
	{
		prev=0;
		ActivityAccesser ac = ActivityAccesser.getInstance();
		spaceActivity=ac.getSpaceActivity();
	    CCScene scene = CCScene.node();
	    CCColorLayer layer = new RaceGameLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
	
	protected SpaceLayer(ccColor4B color)
	{
	    super(color);
	    this.setIsTouchEnabled(true);
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
	    
	    _player = CCSprite.sprite("ship1.png");
	    _player.setPosition(CGPoint.ccp(_player.getContentSize().width, winSize.height));

	    background = CCSprite.sprite("space_bg.jpg");
	    //background.setTag(1);
	    background.setAnchorPoint(0, 0);
	    addChild(background);
	 
	    a=Account.getInstance();
	    //this.act=act;

	    addChild(_player);
	    schedule("checkFinished");
	    schedule("movePlayer");
	}
	
	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
		Log.d("GameLayer", "Remove Sprite");
	}
	
	public void checkFinished(float dt){
		CGPoint playerPos = _player.getPosition();
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		background.setContentSize(winSize.width, winSize.height);
		
		if((playerPos.y+_player.getContentSize().height)>=winSize.height){
			Log.d("checkFinished","Player Wins");
			a.incScore();
			ActivityAccesser.getInstance().setCompWin(false);
			ActivityAccesser.getInstance().setPlayedGame(true);
			spaceActivity.finish();
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
		CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y+Math.abs(distance));
		_player.setPosition(newPoint);
	}
}
