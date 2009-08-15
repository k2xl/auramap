package com.auramap;

import android.app.Activity;
import android.os.Bundle;

public class OptionsScreen extends Activity {	
	@Override
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        NavBar.adaptNav(this);
	}
}
