package com.auramap;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//Slightly modified version of the code provided by Matt Drake

public class ConnectionResource extends Activity {
    /** Called when the activity is first created. */
        public final static String queryBase="http://ngp.lcc.gatech.edu/php_scripts/droid_root.php?";
        
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.v("Auramap", "1");
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.state_disp);
              

        Bundle extras = getIntent().getExtras();
        
        String q="overwrite_me";
        if(extras !=null)
        {
                q = extras.getString("entry");
        }
        
        //Puts bundle in q
       q = "username=test&"  
       	+ "password=test&"
       	+ "emotx=" + extras.getInt("emotx") + "&"
       	+ "emoty=" + extras.getInt("emoty")+ "&"
       	+ "lat=" + extras.getDouble("lat")+ "&"
       	+ "lon=" + extras.getDouble("lon")+ "&"
       	+ "tag=" + extras.getString("tag");    	
       	        
        //Creates an optional view showing all the data it's sending off
    	
        TextView vText = (TextView)findViewById(R.id.emote);
    	vText.setText(extras.get("emote").toString());

    	vText = (TextView)findViewById(R.id.happyX);
    	vText.setText(extras.get("emotx").toString());

    	vText = (TextView)findViewById(R.id.happyY);
    	vText.setText(extras.get("emoty").toString());
    	
    	vText = (TextView)findViewById(R.id.loc);
    	vText.setText("Long: " +     			
    			extras.get("lon").toString() + " Lat: " +
    			extras.get("lat").toString());	
    	final Button mapButton = (Button) findViewById(R.id.MapButton);
        mapButton.setOnClickListener(mListener);
    	
        Log.v("Auramap", "2");
        Intent intent = new Intent();
        Log.v("Auramap", "3");
        String val = textURL(q);
        intent.putExtra("webResponse",val);
        setResult(RESULT_OK, intent);
        //finish();
    }
    private OnClickListener mListener =new OnClickListener() {
    	
    	public void onClick(View v) {                
    	    goToMap();                 
        }
    };
    
    private void goToMap() {
    	
        Intent i = new Intent(this.getBaseContext(), MoodMap.class);	  
        startActivity(i);
        finish();
    }  
    
    
    public String textURL(String vars)
    {
    	int BUFFER_SIZE = 2000;
        InputStream in = null;

        try {
            HttpURLConnection con = (HttpURLConnection)(new URL("http://www.k2xl.info/auramap/server/insertaura.php")).openConnection();
            
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

        Log.v("Auramap", "CR: " + str);
        return str;        
    }
    private void log(String s) {
    	Log.v("Auramap", s);    
    }
    }
