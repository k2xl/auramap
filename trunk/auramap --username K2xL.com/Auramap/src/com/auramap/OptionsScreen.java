package com.auramap;

import android.app.Activity;
import android.os.Bundle;

public class OptionsScreen extends Activity {
	public void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        NavBar.adaptNav(this);
	}
}
