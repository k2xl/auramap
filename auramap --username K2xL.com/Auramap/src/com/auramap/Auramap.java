package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Auramap extends Activity {
    /** Called when the activity is first created. */
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        
        
        Intent i = new Intent(this.getBaseContext(), QueryScreen.class);
        startActivity(i);
        
        finish();
        
    }    
   

 }





