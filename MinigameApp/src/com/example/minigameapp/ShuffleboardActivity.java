package com.example.minigameapp;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ShuffleboardActivity extends Activity implements SensorEventListener{
	protected CCGLSurfaceView glSurfaceView;
	private SensorManager sensorManager;
	private ActivityAccesser access;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	 
	    access = ActivityAccesser.getInstance();
	    access.setShuffle(this);
	    
	    glSurfaceView = new CCGLSurfaceView(this);
	    
	    setContentView(glSurfaceView);
	    
	    sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
		// add listener.
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		
	}
	
	@Override
	public void onStart()
	{
	    super.onStart();
	 
	    CCDirector.sharedDirector().attachInView(glSurfaceView);
	 
	    CCDirector.sharedDirector().setDisplayFPS(true);
	 
	    CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);
	    CCScene scene = ShuffleboardLayer.scene();
	    CCDirector.sharedDirector().runWithScene(scene);
	}
	
	@Override
	public void onPause()
	{
	    super.onPause();
	 
	    CCDirector.sharedDirector().pause();
	}
	 
	@Override
	public void onResume()
	{
	    super.onResume();
	 
	    CCDirector.sharedDirector().resume();
	}
	 
	@Override
	public void onStop()
	{
	    super.onStop();
	 
	    CCDirector.sharedDirector().end();
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event){
		
		// check sensor type
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

			// assign directions
			float deltax=event.values[0];
			float deltay=event.values[1];
			float deltaz=event.values[2];
			
			float deltax1=event.values[0];
			float deltay1=event.values[1];
			float deltaz1=event.values[2];
			
			float deltax2=event.values[0];
			float deltay2=event.values[1];
			float deltaz2=event.values[2];
			
			float deltax3=event.values[0];
			float deltay3=event.values[1];
			float deltaz3=event.values[2];
			
			//Moving Average
			float smoothx = (deltax+ deltax1 + deltax2 +deltax3)/4.0f;
			float smoothy = (deltay+ deltay1 + deltay2 +deltay3)/4.0f;
			float smoothz = (deltaz+ deltaz1 + deltaz2 +deltaz3)/4.0f;
			
			//updateRawCount(smoothx,smoothy,smoothz);
			float total = smoothx + smoothy + smoothz;
			//Log.d("total", total+"");
			ActivityAccesser a = ActivityAccesser.getInstance();
			a.setValues(total);
		}
	}
	
}
