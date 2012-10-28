package com.example.minigameapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ManageAccount extends Activity implements OnClickListener, OnItemSelectedListener{
	private Account a;
	private String name;
	private TextView score;
	private EditText editName;
	private Button submit;
	private Spinner difficulty;
	private String dif;
	
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
	    difficulty = (Spinner)findViewById(R.id.difficultySpinner);
	    if(name!="")
	    	editName.setText(name);
	    score.setText("Score: "+a.getScore());
	}
	
	/**
	 * instantiates and creates Spinner option
	 */
	public void createSpinner(){
		difficulty = (Spinner) findViewById(R.id.difficultySpinner);
		String[] items = new String[] {"Beginner","Normal", "Hard"}; 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		difficulty.setAdapter(adapter); 
		difficulty.setOnItemSelectedListener(this);
	}
	
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.submit:
			a.setName(editName.getText().toString());
			a.setDifficulty(dif);
			Intent i = new Intent(this, MainPage.class);
			startActivity(i);
			break;
		}
		
	}

	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long arg3) {
		dif= parent.getItemAtPosition(pos).toString();
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
