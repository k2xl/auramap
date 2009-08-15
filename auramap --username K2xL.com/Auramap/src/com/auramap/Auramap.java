package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Auramap extends Activity {
    /** Called when the activity is first created. */
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
              
        
        Intent i = new Intent(this.getBaseContext(), RegisterPhone.class);
        startActivityForResult(i, 79);
               
    }   
    
	public void onActivityResult (int requestCode, int resultCode, Intent data) 
    {
    	Log.v("ddfs", "ActivityResulted: " + requestCode);
    	Intent i = new Intent(this.getBaseContext(), QueryScreen.class);
        startActivity(i);
        finish();
    }
   

 }





