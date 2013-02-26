package com.example.minigameapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Test extends Activity implements OnClickListener{
	Button one, two, three, four, five,six;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        one = (Button)findViewById(R.id.one);
        two = (Button)findViewById(R.id.two);
        three = (Button)findViewById(R.id.three);
        four = (Button)findViewById(R.id.four);
        five = (Button)findViewById(R.id.five);
        six = (Button)findViewById(R.id.six);
        six.setOnClickListener(this);
        five.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
	}

	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.one:
			Intent i = new Intent(this, RaceActivity.class);
			startActivity(i);
			break;
		case R.id.two:
			Intent i2 = new Intent(this, FootballActivity.class);
			startActivity(i2);
			break;
		case R.id.three:
			Intent i3 = new Intent(this, ChopWoodActivity.class);
			startActivity(i3);
			break;
		case R.id.four:
			Intent i4 = new Intent(this, JumpingJackActivity.class);
			startActivity(i4);
			break;
		case R.id.five:
			Intent i5 = new Intent(this, RGLightActivity.class);
			startActivity(i5);
			break;
		case R.id.six:
			Intent i6 = new Intent(this, BoxingActivity.class);
			startActivity(i6);
			break;
		}
		
	}
	
	@Override
    public void onResume(){
		super.onResume();
		ActivityAccesser ac = ActivityAccesser.getInstance();
		if(ac.isPlayedGame()){
	    	if(ac.isCompWin())
	    		Toast.makeText(getApplicationContext(), "COMPUTER WINS",	Toast.LENGTH_LONG).show();
	    	else
	    		Toast.makeText(getApplicationContext(), "YOU WIN",	Toast.LENGTH_LONG).show();
		}
		ac.setPlayedGame(false);

    }
}
