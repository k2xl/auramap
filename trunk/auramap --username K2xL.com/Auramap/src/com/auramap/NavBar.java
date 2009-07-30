package com.auramap;

import java.util.Timer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class NavBar {
	
	private static Activity currentAct;

    private static OnClickListener navBarListener =new OnClickListener() {
    	public void onClick(View v) {
    		Intent i;
    		ProgressDialog.show(currentAct, "Loading...", "Switching screens give me a second!");
    		
    		switch(v.getId()) {
    		case R.id.querynav:
    			i = new Intent(currentAct.getBaseContext(), QueryScreen.class);				
    			currentAct.startActivity(i);
    		break;
    		case R.id.mapnav:
    			i = new Intent(currentAct.getBaseContext(), MoodMap.class);				
    			currentAct.startActivity(i);
    		break;
    		case R.id.buddynav:
    			i = new Intent(currentAct.getBaseContext(), BuddyScreen.class);				
    			currentAct.startActivity(i);
    		break;
    		case R.id.optionsnav:
    			i = new Intent(currentAct.getBaseContext(), OptionsScreen.class);				
    			currentAct.startActivity(i);
    			break;
    		}
    		currentAct.finish();
    	}
    };
    
    public static void setCurrentAct(Activity newAct) {
    	currentAct = newAct;
    }
    
	public static void adaptNav(Activity current) {
		ImageButton queryButton = (ImageButton)current.findViewById(R.id.querynav);
		Log.v("aaaaa","What's null is it queryButton? = "+queryButton+" or navBarListener = "+navBarListener);
		queryButton.setOnClickListener(navBarListener);
		ImageButton mapButton = (ImageButton)current.findViewById(R.id.mapnav);
		mapButton.setOnClickListener(navBarListener);
		ImageButton buddyButton = (ImageButton)current.findViewById(R.id.buddynav);
		buddyButton.setOnClickListener(navBarListener);
		ImageButton optionsButton = (ImageButton)current.findViewById(R.id.optionsnav);
		optionsButton.setOnClickListener(navBarListener);
		
		currentAct = current;
		
		
	}
	
}
