package com.example.minigameapp;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.util.Log;

public class ShuffleboardLayer extends CCColorLayer{
	private CCSprite _player;
	private CCSprite background;
	private Account a;
	private static ShuffleboardActivity rlActivity;
	private static float prev;
	private boolean isMoving;
	
	public static CCScene scene()
	{
		prev=0;
		ActivityAccesser ac = ActivityAccesser.getInstance();
		rlActivity=ac.getShuffle();
	    CCScene scene = CCScene.node();
	    CCColorLayer layer = new RGLightLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
	
	protected ShuffleboardLayer(ccColor4B color)
	{
	    super(color);
	    this.setIsTouchEnabled(true);
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
	    
	    isMoving=false;
	    
	    _player = CCSprite.sprite("shuffle_puck.png");
	    _player.setPosition(CGPoint.ccp(winSize.width/2.0f, winSize.height-_player.getContentSize().height));

	    background = CCSprite.sprite("shuffleboard_bg.png");
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
	    this.removeChild(_player, true);
		Log.d("GameLayer", "Remove Sprite");
	}
	
	public void checkFinished(float dt){
		CGPoint playerPos = _player.getPosition();
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		background.setContentSize(winSize.width, winSize.height);
		if(!isMoving && playerPos.y>200){
			Log.d("checkFinished","Player Loses");
			a.decScore();
			ActivityAccesser.getInstance().setCompWin(true);
			ActivityAccesser.getInstance().setPlayedGame(true);
			rlActivity.finish();
		}
		else if(isMoving && playerPos.y<100){
			Log.d("checkFinished","Player Loses");
			a.decScore();
			ActivityAccesser.getInstance().setCompWin(true);
			ActivityAccesser.getInstance().setPlayedGame(true);
			rlActivity.finish();
		}
		else if(!isMoving){
			Log.d("checkFinished","Player Wins");
			a.incScore();
			ActivityAccesser.getInstance().setCompWin(false);
			ActivityAccesser.getInstance().setPlayedGame(true);
			rlActivity.finish();
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
		if(Math.abs(distance)>2){
			CGPoint playerPos = _player.getPosition();
			//Log.d("x",playerPos.x+"");
			CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y+Math.abs(distance));
			_player.setPosition(newPoint);
			isMoving=true;
		}
		else
			isMoving=false;
			
	}
}
