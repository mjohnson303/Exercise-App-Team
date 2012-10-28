package com.example.minigameapp;

import java.util.Random;

import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemFont;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class FootballGameLayer extends CCColorLayer{
	private CCSprite _fball;
	private CCSprite background;
	private Account a;
	private static FootballActivity fbActivity;
	private static float prev;
	private int time;
	private CCMenuItemFont item6;
	private CCMenu menu;
	
	public static CCScene scene()
	{
		prev=0;
		ActivityAccesser ac = ActivityAccesser.getInstance();
		fbActivity=ac.getFootballActivity();
	    CCScene scene = CCScene.node();
	    CCColorLayer layer = new FootballGameLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
	
	protected FootballGameLayer(ccColor4B color)
	{
	    super(color);
	    this.setIsTouchEnabled(true);
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
	    
	    _fball = CCSprite.sprite("football.png");
	    _fball.setPosition(CGPoint.ccp(winSize.width/2.0f, winSize.height - _fball.getContentSize().height));

	    background = CCSprite.sprite("fballbackground.png");
	    //background.setTag(1);
	    background.setAnchorPoint(0, 0);
	    addChild(background);
	    time=5;
	 
	    a=Account.getInstance();
	    //this.act=act;

	    item6 = CCMenuItemFont.item("SHAKE FOR STRENGTH. "+time+" SECONDS REMAINING", this, "");
        CCMenuItemFont.setFontSize(14);
        item6.setColor( new ccColor3B(0,0,0));
        menu = CCMenu.menu(item6);
        menu.alignItemsVertically();
        addChild(menu);
	    
	    addChild(_fball);
	    schedule("checkFinished");
	    schedule("displayTime", 2.0f);
	}
	
	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
	    //CCSprite sprite = (CCSprite)sender;
	    //this.removeChild(sprite, true);
	    this.removeChild(_fball, true);
		Log.d("GameLayer", "Remove Sprite");
	}
	
	public void displayTime(float dt){
		Log.d("diplayTime","START");
		removeChild(menu, true);
		if(time>0){
			item6 = CCMenuItemFont.item("SHAKE FOR STRENGTH. "+time+" SECONDS REMAINING", this, "");
	        CCMenuItemFont.setFontSize(14);
	        item6.setColor( new ccColor3B(0,0,0));
	        menu = CCMenu.menu(item6);
	        menu.alignItemsVertically();
	        addChild(menu);
		}
		else{
			moveFootball();
		}
	}
	
	public void checkFinished(float dt){
		CGPoint fbPos = _fball.getPosition();
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		background.setContentSize(winSize.width, winSize.height);
		if(true){
			Log.d("checkFinished","Computer Wins");
			CCMenuItemFont item6 = CCMenuItemFont.item("COMPUTER WINS", this, "");
            CCMenuItemFont.setFontSize(14);
            item6.setColor( new ccColor3B(0,0,0));
            CCMenu menu = CCMenu.menu(item6);
            menu.alignItemsVertically();
            addChild(menu);
			a.decScore();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e("checkFinished","Computer - sleep error");
				e.printStackTrace();
			}
			Activity context = CCDirector.sharedDirector().getActivity();
			Intent intent = new Intent(context, MainPage.class);
			removeChild(menu, true);
			context.startActivity(intent);
			fbActivity.finish();
		}
	}
	
	public void moveFootball(){
		ActivityAccesser a = ActivityAccesser.getInstance();
		float curr = a.getValues();
		Log.d("Curr",curr+"");
		if(curr<12){
			kickFails();
		}
		else{
			kickSucceeds();
		}
	}
	
	public void kickFails(){
		/** CGPoint point = CGPoint.ccp(finalX,winSize.height / 2.0f + _player.getContentSize().height*3);
	    CCMoveTo actionMove = CCMoveTo.action(10, point);
	 	_fball.runAction(actionMove);*/
		CGPoint playerPos = _fball.getPosition();
		Log.d("x",playerPos.x+"");
		CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y);
		_fball.setPosition(newPoint);
	}
	
	public void kickSucceeds(){
		/** CGPoint point = CGPoint.ccp(finalX,winSize.height / 2.0f + _player.getContentSize().height*3);
	    CCMoveTo actionMove = CCMoveTo.action(10, point);
	 	_fball.runAction(actionMove);*/
		CGPoint playerPos = _fball.getPosition();
		Log.d("x",playerPos.x+"");
		CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y);
		_fball.setPosition(newPoint);
	}
}
