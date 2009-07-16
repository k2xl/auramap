package com.auramap;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MoodMap extends MapActivity {

     LinearLayout linearLayout;
     MapView mapView;
     ZoomControls mapZoom;
     MapController mc;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
       
        mapView = (MapView) findViewById(R.id.thismap);
        mc = mapView.getController();
        mc.animateTo(new GeoPoint(33778268, -84399182));
        mc.zoomToSpan(10487, 17809);
        mapView.setBuiltInZoomControls(true);
        OverlayItem[] items = getPoints();
        Log.v("Auramap","AAA");
        Drawable drawable = this.getResources().getDrawable(R.drawable.blank2);
        Log.v("Auramap","BBB");
        ItemizedAuraPoints circ = new ItemizedAuraPoints(drawable);
        
        int tempS = items.length;
        Log.v("Auramap","CCC = "+tempS);
        for (int i = 0 ; i < tempS ; i++)
        {
        	circ.addOverlay(items[i]);
        }
        
        Log.v("Auramap","DDD");
        circ.callPopulate();
        mapView.setAlwaysDrawnWithCacheEnabled(true);
        mapView.setDrawingCacheEnabled(true);
        
        
        mapView.getOverlays().add(circ);
        Log.v("Auramap","EEE");
    }
    
    private OverlayItem[] getPoints() {    	
    	
    	String serverResult = textURL("username=test&password=test");
    	
    	String[] sploded = serverResult.split("#");

    	if(sploded[0].equals("SUCCESS") == false) {Log.v("Auramap", "ERROR ERROR ERROR=" + sploded[0]); return null;}

    	int tempS = sploded.length;
    	
    	OverlayItem[] items = new OverlayItem[tempS-1];
    	
    	for(int i =1; i<tempS; i++) {
    		String curString = sploded[i];
    		String[] auraPointData = curString.split(",");
    		int latData = (int)(1000000*Double.parseDouble(auraPointData[0]));
    		int lonData = (int)(1000000*Double.parseDouble(auraPointData[1]));
    		int emotX = Integer.parseInt(auraPointData[2]);
    		GeoPoint geopt = new GeoPoint(latData,lonData);
    		items[i-1] = new OverlayItem(geopt,"",""+emotX);
    	}


    	return items;
    }
    
    

     @Override
     protected boolean isRouteDisplayed() {
          // TODO Auto-generated method stub
          return false;
     }
     
     public String textURL(String vars)
     {
     	int BUFFER_SIZE = 2000;
         InputStream in = null;

         try {
             HttpURLConnection con = (HttpURLConnection)(new URL("http://www.k2xl.info/auramap/server/getcoords.php")).openConnection();
             
             con.setRequestMethod( "POST" );
             con.setRequestProperty("METHOD", "POST");
             con.setDoInput( true );
             con.setDoOutput( true );
             Log.v("Auramap", "Sending message: " + vars);
            // add url form parameters
             DataOutputStream ostream = null;
             try {
                 ostream = new DataOutputStream( con.getOutputStream() );
                 ostream.writeBytes( vars );
             }finally {
                 if( ostream != null ) {
                     ostream.flush();
                     ostream.close();
                   }
                 }
             
             in = con.getInputStream();

             
         } catch (IOException e1) {
             // TODO Auto-generated catch block
             e1.printStackTrace();
             return e1.toString();
         }
         
         InputStreamReader isr = new InputStreamReader(in);
         int charRead;
           String str = "";
           char[] inputBuffer = new char[BUFFER_SIZE];          
         try {
             while ((charRead = isr.read(inputBuffer))>0)
             {                    
                 //---convert the chars to a String---
                 String readString =
                     String.copyValueOf(inputBuffer, 0, charRead);                    
                 str += readString;
                 inputBuffer = new char[BUFFER_SIZE];
             }
             in.close();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             return "FAILED";
         } 

         Log.v("Auramap", "Coords: " + str);
         return str;        
     }
}
