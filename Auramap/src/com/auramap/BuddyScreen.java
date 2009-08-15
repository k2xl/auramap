package com.auramap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BuddyScreen extends Activity {	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddyscreen);
        NavBar.adaptNav(this);
	}
}
