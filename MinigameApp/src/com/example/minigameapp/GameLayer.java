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
	CCSprite _player;
	CCSprite _computer;
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
	    
	    _player = CCSprite.sprite("race_car2.png");
	    _computer = CCSprite.sprite("race_car.png");
	    _player.setPosition(CGPoint.ccp(_player.getContentSize().width / 2.0f, winSize.height / 2.0f));
	    _computer.setPosition(CGPoint.ccp(_computer.getContentSize().width / 2.0f,winSize.height / 2.0f + _player.getContentSize().height*3));

	 
	    addChild(_player);
	    addChild(_computer);
	    //this.schedule("moveComputer",1.0f);
	}
	public void moveComputer(){
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		float finalX=winSize.width;
		
		// Determine speed of the target
		Random rand = new Random();
	    int minDuration = 2;
	    int maxDuration = 15;
	    int rangeDuration = maxDuration - minDuration;
	    //int actualDuration = rand.nextInt(rangeDuration) + minDuration;
	    int actualDuration=1;
	    
	    Log.d("GameLayer","Set Action");
		//CCMoveTo actionMove = CCMoveTo.action(actualDuration, CGPoint.ccp(-computer.getContentSize().width / 2.0f, finalX));
	    //CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "spriteMoveFinished");
	    //CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
	    //computer.runAction(actions);
	    CGPoint point = CGPoint.ccp(finalX,winSize.height / 2.0f + _player.getContentSize().height*3);
	    CCMoveTo actionMove = CCMoveTo.action(10, point);
	 	_computer.runAction(actionMove);
	    Log.d("GameLayer", "Start Moving");
	}
	
	public void spriteMoveFinished(Object sender)
	{
		Log.d("GameLayer", "Finished Moving");
	    //CCSprite sprite = (CCSprite)sender;
	    //this.removeChild(sprite, true);
	    this.removeChild(_computer, true);
		Log.d("GameLayer", "Remove Sprite");
	}
}
