package com.auramap;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

//Slightly modified version of the code provided by Matt Drake

public class ConnectionResource extends Activity {
    /** Called when the activity is first created. */
        public final static String queryBase="http://ngp.lcc.gatech.edu/php_scripts/droid_root.php?";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.state_disp);
              
        
        Bundle extras = getIntent().getExtras();
        
        String q="overwrite_me";
        if(extras !=null)
        {
                q = extras.getString("entry");
        }
        
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
    	    	
        Intent intent = new Intent();
        String val = textURL(q);
        intent.putExtra("webResponse",val);
        setResult(RESULT_OK, intent);
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
        return str;        
    }
}