package com.auramap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class OptionsScreen extends Activity {	
	@Override
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        NavBar.adaptNav(this);
        
        Toast.makeText(this, "Right now there are no options,but this is an addition we plan to make in the near future.", 60000).show();
	}
}
