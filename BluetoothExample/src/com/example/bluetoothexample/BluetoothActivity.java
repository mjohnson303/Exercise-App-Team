package com.example.bluetoothexample;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BluetoothActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bluetooth, menu);
        return true;
    }
}
