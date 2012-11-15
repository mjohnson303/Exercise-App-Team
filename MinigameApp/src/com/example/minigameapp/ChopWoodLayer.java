package com.example.minigameapp;

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

public class ChopWoodLayer extends CCColorLayer{
	private CCSprite _axe;
	private CCSprite background;
	private CCSprite _log;
	private static ChopWoodActivity cwActivity;
	private int time;
	private CCMenuItemFont item6;
	private CCMenu menu;
	private float total;

	public static CCScene scene()
	{
		ActivityAccesser ac = ActivityAccesser.getInstance();
		cwActivity=ac.getChopWoodActivity();
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

		_log = CCSprite.sprite("log.png");
		_log.setPosition(CGPoint.ccp(winSize.width/2.0f,_log.getContentSize().height-35));
		_axe = CCSprite.sprite("axe.png");
		_axe.setPosition(CGPoint.ccp(winSize.width/2.0f-_log.getContentSize().width+15,_log.getContentSize().height/2+5));

		background = CCSprite.sprite("farmbg.jpg");
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

		addChild(_axe);
		addChild(_log);
		schedule("displayTime", 1);
	}

	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
		//CCSprite sprite = (CCSprite)sender;
		//this.removeChild(sprite, true);
		this.removeChild(_axe, true);
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
		cwActivity.finish();
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
	CCRotateTo actionMove = CCRotateTo.action(1, 45);
	_axe.runAction(actionMove);
	CGPoint playerPos = _axe.getPosition();
	Log.d("x",playerPos.x+"");
	CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y);
	_axe.setPosition(newPoint);
	CCMenuItemFont item6 = CCMenuItemFont.item("YOU DIDN'T CUT HARD ENOUGH", this, "");
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
	CCRotateTo actionMove = CCRotateTo.action(1, 45);
	_axe.runAction(actionMove);
	CGPoint playerPos = _axe.getPosition();
	Log.d("x",playerPos.x+"");
	CGPoint newPoint = CGPoint.ccp(playerPos.x, playerPos.y);
	_axe.setPosition(newPoint);
	CCMenuItemFont item6 = CCMenuItemFont.item("CONGRATULATIONS YOU CUT THE LOG", this, "");
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
