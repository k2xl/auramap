package com.auramap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Auramap extends Activity {
    /** Called when the activity is first created. */
    
    HappyState happyState;
	LocationManager manager;
	Location location; //location
	LocationListener locationListener;
    String MyPhoneNumber;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        happyState = new HappyState();
        location = manager.getLastKnownLocation( "gps" );
        locationListener = new MyLocationListener();
        
        TelephonyManager telephony = (TelephonyManager)
        
        getSystemService(TELEPHONY_SERVICE);
        MyPhoneNumber = telephony.getLine1Number();
        Log.v("aaa","My telephone number = "+MyPhoneNumber);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2500,
                25,
                locationListener); 
        
        setContentView(R.layout.main);     
        ((ImageButton)findViewById(R.id.FaceButton)).setBackgroundDrawable( getResources().getDrawable(R.drawable.neutral2) );
		
    	happyState.xVal = .5;
        createButtons();
    }
    
    private OnClickListener happyButtonListener = new OnClickListener() {
        public void onClick(View v) {
        	int id = v.getId();
        	switch(id) {
        	case R.id.HappyButton0: 
        	    happyState.stateName = "depressed";
        		happyState.xVal = 0;
        		((ImageButton)findViewById(R.id.FaceButton)).setBackgroundDrawable( getResources().getDrawable(R.drawable.sad2) );
        		break;
        	case R.id.HappyButton1: 
        	    happyState.stateName = "sad";
        		((ImageButton)findViewById(R.id.FaceButton)).setBackgroundDrawable( getResources().getDrawable(R.drawable.lesssad2) );
        		
        		happyState.xVal = .25;
        		break;
        	case R.id.HappyButton2: 
        	    happyState.stateName = "neutral";
        		((ImageButton)findViewById(R.id.FaceButton)).setBackgroundDrawable( getResources().getDrawable(R.drawable.neutral2) );
        		
        		happyState.xVal = .5;
        		break;
        	case R.id.HappyButton3: 
        	    happyState.stateName = "happy";
        		((ImageButton)findViewById(R.id.FaceButton)).setBackgroundDrawable( getResources().getDrawable(R.drawable.lesshappy2) );
        		
        		happyState.xVal = .75;
        		break;
        	case R.id.HappyButton4: 
        	    happyState.stateName = "ecstatic";
        		((ImageButton)findViewById(R.id.FaceButton)).setBackgroundDrawable( getResources().getDrawable(R.drawable.happy2) );
        		
        		happyState.xVal = 1;
        		break;
        	case R.id.nextButton: 
        	    sendAuraPoint();
        		break;        	
        	}
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
    	
        ImageButton button00 = (ImageButton)findViewById(R.id.HappyButton0);
        button00.setOnClickListener(happyButtonListener);
        
        ImageButton button04 = (ImageButton)findViewById(R.id.HappyButton1);
        button04.setOnClickListener(happyButtonListener);
        
        ImageButton button11 = (ImageButton)findViewById(R.id.HappyButton2);
        button11.setOnClickListener(happyButtonListener);
        
        ImageButton button13 = (ImageButton)findViewById(R.id.HappyButton3);
        button13.setOnClickListener(happyButtonListener);
        
        ImageButton button22 = (ImageButton)findViewById(R.id.HappyButton4);
        button22.setOnClickListener(happyButtonListener);
        
        ImageButton next = (ImageButton)findViewById(R.id.nextButton);
        next.setOnClickListener(happyButtonListener);
    }
        

    private void sendAuraPoint() {
    Intent i = new Intent(this.getBaseContext(), GetTag.class);	
    i.putExtra("emote", happyState.stateName);
    i.putExtra("username", "7707120740");
    i.putExtra("password", "788eb77a3c1f739a0ea699612863d745");
    i.putExtra("emotx", happyState.xVal);
    double randomTechpointLat = 
    	33.78156339080061 - Math.random() * 0.010487523427205;
    double randomTechpointLon =
    -84.38984870910645 - Math.random() * 0.01780986785888;
    i.putExtra("lat", randomTechpointLat);
    i.putExtra("lon", randomTechpointLon);
    //i.putExtra("lat", location.getLatitude());
    //i.putExtra("lon", location.getLongitude());
    Log.v("Auramap", "Lat/Lon: " + location.getLatitude() + ", " + location.getLongitude());
    startActivity(i);
    finish();
    }

 }





