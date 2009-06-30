package com.auramap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Auramap extends Activity {
    /** Called when the activity is first created. */
    
    HappyState happyState;
    
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.v("Auramap", "OPEN"); 
        
        setContentView(R.layout.main);      
        
        		
        ImageButton button = (ImageButton)findViewById(R.id.PirateButton);
        button.setOnClickListener(happyButtonListener);
    }
    
    private OnClickListener happyButtonListener = new OnClickListener() {
        public void onClick(View v) {
          if(v.getId() == R.id.PirateButton ) {
        	  Log.v("Auramap", "1");
        	goToStateDisp();
          }
        }
    };
    
    private void goToStateDisp(){

    	Log.v("Auramap", "2");
    	setContentView(R.layout.state_disp);
    }
    }


