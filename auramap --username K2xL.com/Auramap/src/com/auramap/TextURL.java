package com.auramap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class TextURL extends Activity {
	private ProgressDialog pd;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        Bundle b = this.getIntent().getExtras();
        
        String url = b.getString("URL");
        String loadMessage = b.getString("loadMessage");

        String servMessage = b.getString("servMessage");
        String response = contactServer(url, servMessage, loadMessage);
        
        
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
            Log.v("Auramap", "Sending message: " + servMessage);
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

