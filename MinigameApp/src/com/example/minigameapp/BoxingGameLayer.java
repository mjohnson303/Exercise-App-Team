package com.example.minigameapp;

import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
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

import android.util.Log;

public class BoxingGameLayer extends CCColorLayer{
	private CCSprite _glove;
	private CCSprite background;
	private CCSprite _bag;
	private static BoxingActivity baActivity;
	private int time;
	private CCMenuItemFont item6;
	private CCMenu menu;
	private float total;

	public static CCScene scene()
	{
		ActivityAccesser ac = ActivityAccesser.getInstance();
		baActivity=ac.getBa();
		CCScene scene = CCScene.node();
		CCColorLayer layer = new BoxingGameLayer(ccColor4B.ccc4(255, 255, 255, 255));

		scene.addChild(layer);

		return scene;
	}

	protected BoxingGameLayer(ccColor4B color)
	{
		super(color);
		total=0;
		this.setIsTouchEnabled(true);
		CGSize winSize = CCDirector.sharedDirector().displaySize();

		_bag = CCSprite.sprite("bag.png");
		//_bag.setPosition(CGPoint.ccp(winSize.width/2.0f,_bag.getContentSize().height-35));
		_bag.setPosition(CGPoint.ccp(winSize.width/2.0f,winSize.height/2.0f));
		_glove = CCSprite.sprite("glove.png");
		
		//_glove.setPosition(CGPoint.ccp(winSize.width/2.0f-_bag.getContentSize().width+15,_bag.getContentSize().height/2+5));
		_glove.setPosition(CGPoint.ccp(winSize.width/2.0f-_bag.getContentSize().width+50,winSize.height/2.0f+50));

		
		background = CCSprite.sprite("gymbg.jpg");
		//background.setTag(1);
		background.setAnchorPoint(0, 0);
		addChild(background);
		time=5;


		item6 = CCMenuItemFont.item("SHAKE FOR STRENGTH.");
		CCMenuItemFont.setFontSize(14);
		item6.setColor( new ccColor3B(30,0,0));
		menu = CCMenu.menu(item6);
		menu.alignItemsVertically();
		addChild(menu);

		addChild(_glove);
		addChild(_bag);
		schedule("displayTime", 1);
	}

	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
		//CCSprite sprite = (CCSprite)sender;
		//this.removeChild(sprite, true);
		this.removeChild(_glove, true);
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
			item6.setColor( new ccColor3B(30,0,0));
			menu = CCMenu.menu(item6);
			menu.alignItemsVertically();
			addChild(menu);
			time--;
		}
		else{
			moveAxe();
		}
	}

	public void checkFinished(){
		ActivityAccesser.getInstance().setPlayedGame(true);
		baActivity.finish();
	}

	public void moveAxe(){
		Log.d("Curr",total+"");
		if(total<(12*5)){
			fail();
		}
		else{
			success();
		}
	}

	public void fail(){
		//CCRotateTo actionMove = CCRotateTo.action(1, 45);
		//_glove.runAction(actionMove);
		//CGPoint playerPos = _axe.getPosition();
		//Log.d("x",playerPos.x+"");
		//CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y);
		//_axe.setPosition(newPoint);
		CGPoint bagPos = _bag.getPosition();
		CGPoint glovePos = _glove.getPosition();
		CGPoint point = CGPoint.ccp(bagPos.x,glovePos.y);
	    CCMoveTo actionMove = CCMoveTo.action(10, point);
	 	_glove.runAction(actionMove);
		CCMenuItemFont item6 = CCMenuItemFont.item("YOU DIDN'T HIT HARD ENOUGH", this, "");
		CCMenuItemFont.setFontSize(14);
		item6.setColor( new ccColor3B(30,0,0));
		CCMenu menu = CCMenu.menu(item6);
		menu.alignItemsVertically();
		addChild(menu);
		ActivityAccesser.getInstance().setCompWin(true);
		Account.getInstance().decScore();
		checkFinished();
	}

	public void success(){
		//CCRotateTo actionMove = CCRotateTo.action(1, 45);
		//_axe.runAction(actionMove);
		//CGPoint playerPos = _axe.getPosition();
		//Log.d("x",playerPos.x+"");
		//CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y);
		//_axe.setPosition(newPoint);
		CGPoint bagPos = _bag.getPosition();
		CGPoint glovePos = _glove.getPosition();
		CGPoint point = CGPoint.ccp(bagPos.x,glovePos.y);
	    CCMoveTo actionMove = CCMoveTo.action(10, point);
	 	_glove.runAction(actionMove);
	 	while(_glove.getPosition().x!=_bag.getPosition().x){}
	 	CCRotateTo actionMove2 = CCRotateTo.action(1, 45);
	 	_bag.runAction(actionMove2);
		CCMenuItemFont item6 = CCMenuItemFont.item("CONGRATULATIONS YOU HIT HARD ENOUGH", this, "");
		CCMenuItemFont.setFontSize(14);
		item6.setColor( new ccColor3B(30,0,0));
		CCMenu menu = CCMenu.menu(item6);
		menu.alignItemsVertically();
		addChild(menu);
		ActivityAccesser.getInstance().setCompWin(false);
		Account.getInstance().incScore();
		checkFinished();
	}
}
