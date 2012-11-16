package com.example.minigameapp;

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

public class JJLayer extends CCColorLayer{
	private CCSprite _player, _comp;
	private CCSprite background;
	private static JumpingJackActivity jjActivity;
	private int count, playerCount;
	private CCMenuItemFont item6;
	private CCMenu menu;
	private int cpPrev, pPrev;
	private float prevValue;

	public static CCScene scene()
	{
		ActivityAccesser ac = ActivityAccesser.getInstance();
		jjActivity=ac.getJumpingJackActivity();
		CCScene scene = CCScene.node();
		CCColorLayer layer = new JJLayer(ccColor4B.ccc4(255, 255, 255, 255));

		scene.addChild(layer);

		return scene;
	}

	protected JJLayer(ccColor4B color)
	{
		super(color);
		count=0;
		cpPrev=1; 
		pPrev=1;
		playerCount=0;
		prevValue=0;
		this.setIsTouchEnabled(true);
		CGSize winSize = CCDirector.sharedDirector().displaySize();

		_player = CCSprite.sprite("stick1b.png");
		_player.setPosition(CGPoint.ccp(winSize.width/4.0f,_player.getContentSize().height/2));
		_comp = CCSprite.sprite("stick1r.png");
		_comp.setPosition(CGPoint.ccp(3*(winSize.width/4.0f),_player.getContentSize().height/2));

		background = CCSprite.sprite("swingset_bg.png");
		//background.setTag(1);
		background.setAnchorPoint(0, 0);
		addChild(background);

		item6 = CCMenuItemFont.item("Do the Most Jumping Jacks!");
		CCMenuItemFont.setFontSize(14);
		item6.setColor( new ccColor3B(0,0,0));
		menu = CCMenu.menu(item6);
		menu.alignItemsVertically();
		addChild(menu);

		addChild(_player);
		addChild(_comp);
		schedule("movePlayer", .1F);
		schedule("moveComp", 1);
	}

	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
		//CCSprite sprite = (CCSprite)sender;
		//this.removeChild(sprite, true);
		this.removeChild(_player, true);
		this.removeChild(_comp, true);
		Log.d("GameLayer", "Remove Sprite");
	}

	public void moveComp(float dt){
		Log.d("moveComp","START");
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		if(cpPrev==1 && count!=10){
			//Log.d("moveComp","1");
			this.removeChild(_comp, true);
			_comp = CCSprite.sprite("stick2r.png");
			_comp.setPosition(CGPoint.ccp(3*(winSize.width/4.0f),_player.getContentSize().height/2));
			addChild(_comp);
			cpPrev=2;
		}
		else if(count!=10){
			//Log.d("moveComp","2");
			this.removeChild(_comp, true);
			_comp = CCSprite.sprite("stick1r.png");
			_comp.setPosition(CGPoint.ccp(3*(winSize.width/4.0f),_player.getContentSize().height/2));
			addChild(_comp);
			cpPrev=1;
		}
		count++;
		if(count==10){
			gameOver();
		}
	}

	public void movePlayer(float dt){
		Log.d("movePlayer","START");
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		float value = ActivityAccesser.getInstance().getValues();
		float change=value-prevValue;
		if(change>1 && pPrev==1 && count!=10){
			this.removeChild(_player, true);
			_player = CCSprite.sprite("stick2b.png");
			_player.setPosition(CGPoint.ccp((winSize.width/4.0f),_player.getContentSize().height/2));
			addChild(_player);
			playerCount++;
			pPrev=2;
		}
		else if(change>1 && count!=10){
			this.removeChild(_player, true);
			_player = CCSprite.sprite("stick1b.png");
			_player.setPosition(CGPoint.ccp((winSize.width/4.0f),_player.getContentSize().height/2));
			addChild(_player);
			playerCount++;
			pPrev=1;
		}
		prevValue=value;
	}

	public void gameOver(){
		this.removeChild(menu, true);
		if(playerCount>5){
			Log.d("gameOver","Win");
			item6 = CCMenuItemFont.item("You Win!");
			CCMenuItemFont.setFontSize(14);
			item6.setColor( new ccColor3B(0,0,0));
			menu = CCMenu.menu(item6);
			menu.alignItemsVertically();
			addChild(menu);
			Account.getInstance().incScore();
			ActivityAccesser.getInstance().setCompWin(false);
		}
		else{
			Log.d("gameOver","Lose");
			item6 = CCMenuItemFont.item("You Lose!");
			CCMenuItemFont.setFontSize(14);
			item6.setColor( new ccColor3B(0,0,0));
			menu = CCMenu.menu(item6);
			menu.alignItemsVertically();
			addChild(menu);
			Account.getInstance().decScore();			
			ActivityAccesser.getInstance().setCompWin(true);
		}
		jjActivity.finish();
	}
}
