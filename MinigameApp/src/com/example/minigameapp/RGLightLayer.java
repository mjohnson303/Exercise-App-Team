package com.example.minigameapp;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.util.Log;

public class RGLightLayer extends CCColorLayer{
	private CCSprite _player;
	private CCSprite background;
	private CCSprite light;
	private Account a;
	private static RGLightActivity rlActivity;
	private static float prev;
	private boolean isMoving;
	private boolean isRed;
	private int warning;
	
	public static CCScene scene()
	{
		prev=0;
		ActivityAccesser ac = ActivityAccesser.getInstance();
		rlActivity=ac.getLa();
	    CCScene scene = CCScene.node();
	    CCColorLayer layer = new RGLightLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
	
	protected RGLightLayer(ccColor4B color)
	{
	    super(color);
	    this.setIsTouchEnabled(true);
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
	    
	    warning=0;
	    isMoving=false;
	    isRed=true;
	    
	    _player = CCSprite.sprite("light_runner.png");
	    _player.setPosition(CGPoint.ccp(_player.getContentSize().width / 2.0f, 75));

	    background = CCSprite.sprite("backyard_bg.png");
	    //background.setTag(1);
	    background.setAnchorPoint(0, 0);
	    addChild(background);
	 
	    light = CCSprite.sprite("redlight.png");
	    light.setPosition(CGPoint.ccp(winSize.width-50,100));
	    a=Account.getInstance();
	    //this.act=act;

	    addChild(light);
	    addChild(_player);
	    schedule("checkFinished");
	    schedule("movePlayer");
	    schedule("changeColor", 1.5F);
	}
	
	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
	    this.removeChild(_player, true);
		Log.d("GameLayer", "Remove Sprite");
	}
	
	public void changeColor(float dt){
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
		removeChild(light, true);
		if(isRed){
		    light = CCSprite.sprite("greenlight.png");
		    isRed=false;
		}
		else{
		    light = CCSprite.sprite("redlight.png");
		    isRed=true;
		}
		warning=0;
	    light.setPosition(CGPoint.ccp(winSize.width-50,100));
		addChild(light);
	}
	
	public void checkFinished(float dt){
		CGPoint playerPos = _player.getPosition();
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		background.setContentSize(winSize.width, winSize.height);
		if(isMoving && isRed && warning==1){
			Log.d("checkFinished","Player Loses");
			a.decScore();
			ActivityAccesser.getInstance().setCompWin(true);
			rlActivity.finish();
		}
		else if(isMoving && isRed && warning==1){
			warning++;
		}
		else if(playerPos.x>=(winSize.width-50)){
			Log.d("checkFinished","Player Wins");
			a.incScore();
			ActivityAccesser.getInstance().setCompWin(false);
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
			CGPoint newPoint = CGPoint.ccp(playerPos.x+Math.abs(distance), playerPos.y);
			_player.setPosition(newPoint);
			isMoving=true;
		}
		else
			isMoving=false;
			
	}
}
