package com.example.minigameapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class TempMain extends Activity implements OnClickListener{
	// Layout Views
	private Button start;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set up the window layout
		setContentView(R.layout.new_main);
		start = (Button)findViewById(R.id.start);
		start.setOnClickListener(this);
	}

	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.start:
			startGames();
			break;
		}		
	}

	public void startGames(){
		Intent i = ActivityAccesser.getInstance().getRandomActivity(this);
		startActivity(i);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.account:
        	Intent i = new Intent(this, ManageAccount.class);
        	startActivity(i);
        	break;
        case R.id.test:
        	Intent i2 = new Intent(this, Test.class);
        	startActivity(i2);
        	break;
        case R.id.about:
        	Intent i3 = new Intent(this, AboutActivity.class);
        	startActivity(i3);
        	break;
        }
        return false;
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
