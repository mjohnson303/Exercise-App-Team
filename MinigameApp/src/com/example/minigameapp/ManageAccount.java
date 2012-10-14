package com.example.minigameapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ManageAccount extends Activity implements OnClickListener{
	private Account a;
	private String name;
	TextView score;
	EditText editName;
	Button submit;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	 
	    setContentView(R.layout.manage_account);
	    
	    a = Account.getInstance();
	    name=a.getName();
	    score = (TextView)findViewById(R.id.score);
	    editName = (EditText)findViewById(R.id.name);
	    submit = (Button)findViewById(R.id.submit);
	    submit.setOnClickListener(this);
	    if(name!="")
	    	editName.setText(name);
	    score.setText("Score: "+a.getScore());
	}
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.submit:
			a.setName(editName.getText().toString());
			Intent i = new Intent(this, MainPage.class);
			startActivity(i);
			break;
		}
		
	}
}
