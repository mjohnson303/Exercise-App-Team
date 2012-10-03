package com.example.minigameapp;

import java.util.Random;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;


import android.util.Log;

public class GameLayer extends CCColorLayer{
	CCSprite player;
	CCSprite computer;
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
	
	protected GameLayer(ccColor4B color)
	{
	    super(color);
	    this.setIsTouchEnabled(true);
	    CGSize winSize = CCDirector.sharedDirector().displaySize();
	    
	    player = CCSprite.sprite("race_car.png");
	    computer = CCSprite.sprite("race_car.png");
	    player.setPosition(CGPoint.ccp(player.getContentSize().width / 2.0f,player.getContentSize().height/2.0f));
	    computer.setPosition(CGPoint.ccp(computer.getContentSize().width / 2.0f+player.getContentSize().width,computer.getContentSize().height/2.0f + player.getContentSize().height));

	 
	    addChild(player);
	}
	public void moveCar(){
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		float finalX=winSize.width;
		
		// Determine speed of the target
		Random rand = new Random();
	    int minDuration = 2;
	    int maxDuration = 4;
	    int rangeDuration = maxDuration - minDuration;
	    int actualDuration = rand.nextInt(rangeDuration) + minDuration;
	    
	    Log.d("GameLayer","Set Action");
		CCMoveTo actionMove = CCMoveTo.action(actualDuration, CGPoint.ccp(-computer.getContentSize().width / 2.0f, finalX));
	    CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "spriteMoveFinished");
	    CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
	    Log.d("GameLayer", "Start Moving");
	}
	
	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
	    //CCSprite sprite = (CCSprite)sender;
	    //this.removeChild(sprite, true);
	    this.removeChild(computer, true);
		Log.d("GameLayer", "Remove Sprite");
	}
}
