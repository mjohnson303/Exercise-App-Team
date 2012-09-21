package com.example.bluetoothexample;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;

public class BluetoothActivity extends Activity {

	InputStream tmpIn = null;
	BluetoothSocket socket = null;
	BluetoothDevice d = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
    }
    
    public void setUpBluetooth(){
    	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {
    	    Log.e("Bluetooth", "No Adapter");
    	}
    	if (!mBluetoothAdapter.isEnabled()) {
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, 1);
    	}
    	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    	for(BluetoothDevice d1: pairedDevices){
    		//TODO check to see if we already are set up with the bluetooth device
    	}
    	// Create a BroadcastReceiver for ACTION_FOUND
    	BroadcastReceiver mReceiver = new BroadcastReceiver() {
    		BluetoothDevice device;
    	    public void onReceive(Context context, Intent intent) {
    	        String action = intent.getAction();
    	        // When discovery finds a device
    	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
    	            // Get the BluetoothDevice object from the Intent
    	            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    	            // Add the name and address to an array adapter to show in a ListView
    	            if(device.getName().equals("WHATEVER OURS NAME IS") || device.getAddress().equals("OUR ADDRESS"))
    	            	d=device;
    	            	//TODO handle this
    	        }
    	    }
    	};
    	// Register the BroadcastReceiver
    	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    	ConnectThread ct = new ConnectThread(d);
    	ct.start();
    	socket = ct.getSocket();
    	try {
			tmpIn = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //gets the input
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bluetooth, menu);
        return true;
    }
}
