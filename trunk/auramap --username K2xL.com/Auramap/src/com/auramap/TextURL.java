package com.auramap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TextURL extends Activity {
	private ProgressDialog pd;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        Bundle b = this.getIntent().getExtras();
        
        String url = b.getString("URL");
        String loadMessage = b.getString("loadMessage");

        String servMessage = "username="+Data.pNumber+"&password="+Data.pKey+"&"+b.getString("servMessage");
        String response = contactServer(url, servMessage, loadMessage);
        
		if (response.indexOf("ERROR") >= 0 || response.indexOf("[404]")>=0) {
			Log.v("Server","Error returned from server: "+response);
			setContentView(R.layout.default_error);
			if (response.indexOf("[404]")>=0)
			{
				Toast.makeText(this, "Doesn't look like you have internet!", 1000).show();
			}
			else if (response.indexOf("LOGIN_ERROR")>=0)
			{
				Toast.makeText(this, "Error logging you in.", 1000).show();
			}
			else if (response.indexOf("PARAMETER_ERROR")>=0)
			{
				Toast.makeText(this, "Looks like some invalid data was sent to the server.", 1000).show();
			}
			else if (response.indexOf("SERVER_ERROR")>=0)
			{
				Toast.makeText(this, "An error occured on HappyMap's server.", 1000).show();
			}
			//finish();
			Intent i = new Intent();
	        i.putExtra("webResponse", response);
	        setResult(RESULT_CANCELED, i);
	        return;
			// finish();
		}         
        
        Intent i = new Intent();
        i.putExtra("webResponse", response);
        setResult(RESULT_OK, i);
        finish();
	}
    
	private String contactServer(String url, String servMessage, String loadMessage) {
		

		String str;
		
    	int BUFFER_SIZE = 2000;
        InputStream in = null;

        try {
            HttpURLConnection con = (HttpURLConnection)(new URL(url)).openConnection();
            
            con.setRequestMethod( "POST" );
            con.setRequestProperty("METHOD", "POST");
            con.setDoInput( true );
            con.setDoOutput( true );
            Log.v("Server", "Sending message: " + servMessage);
           // add url form parameters
            DataOutputStream ostream = null;
            try {
                ostream = new DataOutputStream( con.getOutputStream() );
                ostream.writeBytes( servMessage );
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
            str =  e1.toString();
        }
        if (in == null)
        {
        	return "[404]";
        }
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
          str = "";
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
            str = "FAILED";
        }
        
        return str;        
    }

 }

