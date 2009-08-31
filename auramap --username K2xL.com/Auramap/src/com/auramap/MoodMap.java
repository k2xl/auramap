package com.auramap;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
	ProgressDialog pd ;
    BitmapDrawable bmp; 
    Bitmap happyMap;
     LinearLayout linearLayout;
     GeoPoint curPoint;
     MapView mapView;
     ZoomControls mapZoom;
     MapController mc;
     OverlayItem[] items;
	LocationManager manager;
	Location location; //location
	LocationListener locationListener;
	private final int GET_AURAPOINTS = 32890;
	private final int GET_HAPPYMAP = 23489;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        NavBar.adaptNav(this);
        
        mapView = (MapView) findViewById(R.id.thismap);
        mc = mapView.getController();
        mc.animateTo(new GeoPoint(33778268, -84399182));
        mc.zoomToSpan(10487, 17809);
        mapView.setBuiltInZoomControls(true);
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        location = manager.getLastKnownLocation( "gps" );
        locationListener = new MyLocationListener();
        int latData = (int)(1000000*location.getLatitude());
        int lonData = (int)(1000000*location.getLongitude());
    	curPoint = new GeoPoint(latData,lonData);
    	
    	
    	/*
    	  CurrentPointOverlay cpOverlay = new CurrentPointOverlay();
          List<Overlay> listOfOverlays = mapView.getOverlays();
          listOfOverlays.clear();
          listOfOverlays.add(cpOverlay); 
          mapView.invalidate();
    	 */

        getImage();
    }
    
    private void drawPoints() {
    	pd.dismiss();
        //Drawable drawable = this.getResources().getDrawable(R.drawable.blank2);
    	BitmapDrawable drawable = new BitmapDrawable(happyMap);
        /*ItemizedAuraPoints circ = new ItemizedAuraPoints(drawable);
        
        int tempS = items.length;
        for (int i = 0 ; i < tempS ; i++)
        {
        	circ.addOverlay(items[i]);
        }
        //circ.addOverlay(curPoint);
        
        circ.callPopulate();*/
        
        
        mapView.setAlwaysDrawnWithCacheEnabled(true);
        mapView.setDrawingCacheEnabled(true);     

    	CurrentPointOverlay cpo = new CurrentPointOverlay();
        GraphicOverlay bmpOverlay = new GraphicOverlay(drawable, curPoint);
        mapView.getOverlays().add(bmpOverlay);
        mapView.getOverlays().add(cpo);
        //mapView.getOverlays().add(circ);
        
        
    }
    private void getImage() {    	
		pd = ProgressDialog.show(this, "Please wait", "Please wait while we get the HappyMap from the server.");	
		String servMessage = "username=" + Data.pNumber + "&password="
		+ Data.pKey + "&";
		String url = "http://www.k2xl.info/auramap/server/getMap.php";        
		happyMap = contactServer(url, servMessage, "Retrieving HappyMap");
		drawPoints();
    }
    
    private void getPoints() {    	
		pd = ProgressDialog.show(this, "Getting Aurapoints...", "Please wait while we get the Aurapoints from the server");
        Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/getcoords.php");
        intent.putExtra("loadMessage","Retrieving Aurapoints");
        intent.putExtra("servMessage","");
        startActivityForResult(intent, GET_AURAPOINTS);
    }
    
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
    
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
    	pd.dismiss();
    	
    	if(requestCode==GET_HAPPYMAP) {
    	}else if (requestCode==GET_AURAPOINTS) {
    	Log.v("Auramap", "Server PointResponse: " + data.getExtras().getString("webResponse"));
    	String[] sploded = data.getExtras().getString("webResponse").split("#");
    	if(sploded[0].equals("SUCCESS") == false) {Log.v("Auramap", "ERROR ERROR ERROR=" + sploded[0]); }
    	int tempS = sploded.length;    	
    	items = new OverlayItem[tempS-1];
    	for(int i =1; i<tempS; i++) {
    		String curString = sploded[i];
    		String[] auraPointData = curString.split(",");
    		int latData = (int)(1000000*Double.parseDouble(auraPointData[0]));
    		int lonData = (int)(1000000*Double.parseDouble(auraPointData[1]));
    		int emotX = Integer.parseInt(auraPointData[2]);
    		GeoPoint geopt = new GeoPoint(latData,lonData);
    		items[i-1] = new OverlayItem(geopt,"",""+emotX);
    	}}
    	
    	
    	drawPoints();

    }
    
    

     @Override
     protected boolean isRouteDisplayed() {
          // TODO Auto-generated method stub
          return false;
     }
     class CurrentPointOverlay extends com.google.android.maps.Overlay
     {
         @Override
         public boolean draw(Canvas canvas, MapView mapView, 
         boolean shadow, long when) 
         {
             super.draw(canvas, mapView, shadow);                   
  
             //---translate the GeoPoint to screen pixels---
             Point screenPts = new Point();
             mapView.getProjection().toPixels(curPoint, screenPts);
  
             //---add the marker---
             Bitmap bmp = BitmapFactory.decodeResource( getResources(), R.drawable.happy);            
             canvas.drawBitmap(bmp, screenPts.x, screenPts.y, null);
             return true;
         }
     } 
     
 	private Bitmap contactServer(String url, String servMessage,
			String loadMessage) {

		String str;

		int BUFFER_SIZE = 2000;
		InputStream in = null;

		try {
			HttpURLConnection con = (HttpURLConnection) (new URL(url))
					.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("METHOD", "POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			Log.v("Auramap", "Sending message: " + servMessage);
			// add url form parameters
			DataOutputStream ostream = null;
			try {
				ostream = new DataOutputStream(con.getOutputStream());
				ostream.writeBytes(servMessage);
			} finally {
				if (ostream != null) {
					ostream.flush();
					ostream.close();
				}
			}

			in = con.getInputStream();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			str = e1.toString();
		}
		if (in == null) {
			return null;}

		BitmapFactory bmpFact = new BitmapFactory();
		Bitmap bmp = bmpFact.decodeStream(in);

		return bmp;
	}

     
}
