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
		
		String res = data.getExtras().getString("Result");
		Log.v("Auramap","resultCode = "+res);
		if (res.equals("ERROR")){
    		setContentView(R.layout.default_error);
			//finish();
		}
		else if (res.equals("OK"))
		{
			Log.v("Auramap", "ActivityResulted: " + requestCode+", result code = "+resultCode);

			Intent i = new Intent(this.getBaseContext(), UpdateBuddies.class);
			startActivity(i);
			Intent o = new Intent(this.getBaseContext(), QueryScreen.class);
			startActivity(o);
	        finish();
		}
		else if (res.equals("FIRST"))
		{
			Log.v("Auramap","First time eh?!");
		}
    }
   

 }





