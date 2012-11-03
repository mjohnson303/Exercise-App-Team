package com.example.minigameapp;

import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
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

public class ChopWoodLayer extends CCColorLayer{
	private CCSprite _fball;
	private CCSprite background;
	private Account a;
	private static FootballActivity fbActivity;
	private int time;
	private CCMenuItemFont item6;
	private CCMenu menu;
	private float total;
	
	public static CCScene scene()
	{
		ActivityAccesser ac = ActivityAccesser.getInstance();
		fbActivity=ac.getFootballActivity();
	    CCScene scene = CCScene.node();
	    CCColorLayer layer = new ChopWoodLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
	
	protected ChopWoodLayer(ccColor4B color)
	{
	    super(color);
	    total=0;
	    this.setIsTouchEnabled(true);
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
	    
	    _fball = CCSprite.sprite("football.png");
	    _fball.setPosition(CGPoint.ccp(winSize.width/2.0f,_fball.getContentSize().height));

	    background = CCSprite.sprite("fballbackground.png");
	    //background.setTag(1);
	    background.setAnchorPoint(0, 0);
	    addChild(background);
	    time=5;
	 
	    a=Account.getInstance();
	    //this.act=act;

	    item6 = CCMenuItemFont.item("SHAKE FOR STRENGTH.");
        CCMenuItemFont.setFontSize(14);
        item6.setColor( new ccColor3B(0,0,0));
        menu = CCMenu.menu(item6);
        menu.alignItemsVertically();
        addChild(menu);
	    
	    addChild(_fball);
	    schedule("checkFinished");
	    schedule("displayTime", 1);
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
			ActivityAccesser a = ActivityAccesser.getInstance();
			total+= a.getValues();
			item6 = CCMenuItemFont.item("SHAKE FOR STRENGTH. "+time+" SECONDS REMAINING", this, "");
	        CCMenuItemFont.setFontSize(14);
	        item6.setColor( new ccColor3B(0,0,0));
	        menu = CCMenu.menu(item6);
	        menu.alignItemsVertically();
	        addChild(menu);
	        time--;
		}
		else{
			moveFootball();
		}
	}
	
	public void checkFinished(float dt){
		CGPoint fbPos = _fball.getPosition();
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		background.setContentSize(winSize.width, winSize.height);
		if(fbPos.equals(CGPoint.ccp(0,winSize.height))){
			Log.d("checkFinished","YOU LOSE");
			removeChild(_fball, true);
			CCMenuItemFont item6 = CCMenuItemFont.item("YOU MISSED THE GOAL", this, "");
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
		else if(fbPos.equals(CGPoint.ccp(winSize.width/2,winSize.height))){
			Log.d("checkFinished","YOU WIN");
			removeChild(_fball, true);
			CCMenuItemFont item6 = CCMenuItemFont.item("YOU MADE THE GOAL", this, "");
            CCMenuItemFont.setFontSize(14);
            item6.setColor( new ccColor3B(0,0,0));
            CCMenu menu = CCMenu.menu(item6);
            menu.alignItemsVertically();
            addChild(menu);
			a.incScore();
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
		Log.d("Curr",total+"");
		if(total<(12*5)){
			kickFails();
		}
		else{
			kickSucceeds();
		}
	}
	
	public void kickFails(){
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CGPoint point = CGPoint.ccp(0,winSize.height);
	    CCRotateTo actionMove = CCRotateTo.action(1, 900);
	    CCMoveTo actionMove2 = CCMoveTo.action(10, point);
	    CCScaleTo action3 = CCScaleTo.action(10f, .3f, .3f);
	 	_fball.runAction(actionMove);
	 	_fball.runAction(actionMove2);
	 	_fball.runAction(action3);
		CGPoint playerPos = _fball.getPosition();
		Log.d("x",playerPos.x+"");
		CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y);
		_fball.setPosition(newPoint);
	}
	
	public void kickSucceeds(){
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CGPoint point = CGPoint.ccp(winSize.width/2,winSize.height);
	    CCRotateTo actionMove = CCRotateTo.action(1, 900);
	    CCMoveTo actionMove2 = CCMoveTo.action(10, point);
	    CCScaleTo action3 = CCScaleTo.action(10f, .3f, .3f);
	 	_fball.runAction(actionMove);
	 	_fball.runAction(actionMove2);
	 	_fball.runAction(action3);
		CGPoint playerPos = _fball.getPosition();
		Log.d("x",playerPos.x+"");
		CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y);
		_fball.setPosition(newPoint);
	}
}