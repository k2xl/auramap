package com.auramap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;




public class Auramap extends Activity {
    /** Called when the activity is first created. */
    
    HappyState happyState;
	LocationManager manager;
	Location location; //location
	LocationListener locationListener;
    
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        happyState = new HappyState();
        location = manager.getLastKnownLocation( "gps" );
        locationListener = new MyLocationListener();
        
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2500,
                25,
                locationListener); 
        
        setContentView(R.layout.main);      
        
        createButtons();
    }
    
    private OnClickListener happyButtonListener = new OnClickListener() {
        public void onClick(View v) {
        	if( v.getId() == R.id.HappyButton00 ) {
        		happyState.stateName = "furious";
        		happyState.xVal = -100;
        		happyState.yVal = 100;        		
        	} else if( v.getId() == R.id.HappyButton04 ) {
        		happyState.stateName = "ecstatic";
        		happyState.xVal = 100;
        		happyState.yVal = 100;        		
        	} else if( v.getId() == R.id.HappyButton11 ) {
        		happyState.stateName = "mad";
        		happyState.xVal = -50;
        		happyState.yVal = 50;        		
        	} else if( v.getId() == R.id.HappyButton13 ) {
        		happyState.stateName = "happy";
        		happyState.xVal = -50;
        		happyState.yVal = 50;        		
        	} else if( v.getId() == R.id.HappyButton22 ) {
        		happyState.stateName = "neutral";
        		happyState.xVal = 0;
        		happyState.yVal = 0;        		
        	} else if( v.getId() == R.id.HappyButton31 ) {
        		happyState.stateName = "sad";
        		happyState.xVal = -50;
        		happyState.yVal = -50;        		
        	} else if( v.getId() == R.id.HappyButton33 ) {
        		happyState.stateName = "satisfied";
        		happyState.xVal = 50;
        		happyState.yVal = -50;        		
        	} else if( v.getId() == R.id.HappyButton40 ) {
        		happyState.stateName = "depressed";
        		happyState.xVal = -100;
        		happyState.yVal = -100;        		
        	} else if( v.getId() == R.id.HappyButton44 ) {
        		happyState.stateName = "content";
        		happyState.xVal = 100;
        		happyState.yVal = -100;        		
        	}
        	sendAuraPoint();
        }  
   };
  

   
   private class MyLocationListener implements LocationListener
   {      
         public void onLocationChanged(Location newLoc) {
                if (newLoc != null) {
                    location = newLoc;
                }
              
         }

		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
   }; 
    
    private void createButtons() {
    	
        ImageButton button00 = (ImageButton)findViewById(R.id.HappyButton00);
        button00.setOnClickListener(happyButtonListener);
        
        ImageButton button04 = (ImageButton)findViewById(R.id.HappyButton04);
        button04.setOnClickListener(happyButtonListener);
        
        ImageButton button11 = (ImageButton)findViewById(R.id.HappyButton11);
        button11.setOnClickListener(happyButtonListener);
        
        ImageButton button13 = (ImageButton)findViewById(R.id.HappyButton13);
        button13.setOnClickListener(happyButtonListener);
        
        ImageButton button22 = (ImageButton)findViewById(R.id.HappyButton22);
        button22.setOnClickListener(happyButtonListener);
        
        ImageButton button31 = (ImageButton)findViewById(R.id.HappyButton31);
        button31.setOnClickListener(happyButtonListener);
        
        ImageButton button33 = (ImageButton)findViewById(R.id.HappyButton33);
        button33.setOnClickListener(happyButtonListener);
        
        ImageButton button40 = (ImageButton)findViewById(R.id.HappyButton40);
        button40.setOnClickListener(happyButtonListener);
        
        ImageButton button44 = (ImageButton)findViewById(R.id.HappyButton44);
        button44.setOnClickListener(happyButtonListener);
        
    }
        

    private void sendAuraPoint() {
    Intent i = new Intent(this.getBaseContext(), GetTag.class);	
    i.putExtra("emote", happyState.stateName);
    i.putExtra("username", "test");
    i.putExtra("password", "test");
    i.putExtra("emotx", happyState.xVal);
    i.putExtra("emoty", happyState.yVal);
    double randomTechpointLat = 
    	33.78156339080061 - Math.random() * 0.010487523427205;
    double randomTechpointLon =
    -84.38984870910645 - Math.random() * 0.01780986785888;
    i.putExtra("lat", randomTechpointLat);
    i.putExtra("lon", randomTechpointLon);
    //i.putExtra("lat", location.getLatitude());
    //i.putExtra("lon", location.getLongitude());    
    startActivity(i);
    finish();
    }

 }





