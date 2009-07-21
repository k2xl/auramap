package com.auramap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class GetTag extends Activity {
    public EditText tag;
    private String data;
    private String[] colors = { "#33CCCC", "#66CC99",  "#99CC66", "#CC9933", "#FF9900" };
    
    private final int numTags = 3;
    
    String[] localTagNames;
    String[] localTagColors;
    String[] globalTagNames;
    String[] globalTagColors;
    
    
    private OnClickListener sListener =new OnClickListener() {
    	
    	public void onClick(View v) {                
            sendAuraPoint();                          
        }
    };
    
    private void setupLocalTags(String toServer) {
    	String fromServer = textURL(toServer + "&radius=100&numresults=" + numTags);
    	String[] sploded = fromServer.split("#");
    	int tempS = sploded.length;
    	for(int i=1; i<tempS; i++) {
    		String[] anotherTemp = sploded[i].split(",");
    		localTagNames[i-1]=anotherTemp[0];   
    		Double d = Double.parseDouble(anotherTemp[2]);
    		int n = (int)Math.round(d*4);
    		localTagColors[i-1]= colors[n];
    		}
    }
    
    private void setupGlobalTags(String toServer) {

    	String fromServer = textURL(toServer + "&radius=5000&numresults=" + numTags);
    	String[] sploded = fromServer.split("#");
    	int tempS = sploded.length;
    	for(int i=0; i<tempS; i++) {
    		String[] anotherTemp = sploded[i].split(",");
    		globalTagNames[i]=anotherTemp[0];   
    		Double d = Double.parseDouble(anotherTemp[2]);
    		int n = (int)Math.round(d*4);
    		globalTagColors[i]= colors[n];
    		}
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);
        
        localTagNames = new String[5];
        localTagColors = new String[5];
        globalTagNames = new String[5];
        globalTagColors = new String[5];

        //data = this.getIntent().getExtras().getString("data");
        tag = (EditText) findViewById(R.id.tag);
        //createTagButtons();
        
        String toServer = "username=" + this.getIntent().getExtras().getString("username") +
        				  "&password=" + this.getIntent().getExtras().getString("password") + 
        				  "&lat=" + this.getIntent().getExtras().getDouble("lat") +  
        				  "&lon=" + this.getIntent().getExtras().getDouble("lon");
        setupLocalTags(toServer);
        setupGlobalTags(toServer);
        //parseResponse();
    	Button tagButton = (Button) findViewById(R.id.globalTag01 );
        tagButton.setText(globalTagNames[0]);
        tagButton.setBackgroundColor(Color.parseColor(globalTagColors[0]));
    	tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.globalTag02 );
        tagButton.setText(globalTagNames[1]);
        tagButton.setBackgroundColor(Color.parseColor(globalTagColors[1]));
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.globalTag03 );
        tagButton.setText(globalTagNames[2]);
        tagButton.setBackgroundColor(Color.parseColor(globalTagColors[2]));
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag01 );
        tagButton.setText(localTagNames[0]);
        tagButton.setBackgroundColor(Color.parseColor(localTagColors[0]));
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag02 );
        tagButton.setText(localTagNames[1]);
        tagButton.setBackgroundColor(Color.parseColor(localTagColors[1]));
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag03 );
        tagButton.setText(localTagNames[2]);
        tagButton.setBackgroundColor(Color.parseColor(localTagColors[2]));
        tagButton.setOnClickListener(tagListener);
  
    
    final ImageButton next = (ImageButton) findViewById(R.id.submit);    
    next.setOnClickListener(sListener);
        
    }
    
    private OnClickListener tagListener =new OnClickListener() {
    	public void onClick(View v) {
    		String str = tag.getText().toString();
    		String newTag = ((Button) findViewById(v.getId())).getText().toString();
    		if(str.equalsIgnoreCase("") ) str = newTag;
    		else if(!str.contains(", " + newTag)) str += ", " + newTag;
    		tag.setText(str);
    		    		
    	}
    };
    
    private void sendAuraPoint() {
        
        String t= tag.getText().toString();
    	Intent i = new Intent(this.getBaseContext(), ConnectionResource.class);				
		i.putExtras(this.getIntent().getExtras());
		i.putExtra("tag", t);
		startActivity(i);
		finish();
		
    }
    
    public String textURL(String vars)
    {
    	int BUFFER_SIZE = 2000;
        InputStream in = null;

        try {
            HttpURLConnection con = (HttpURLConnection)(new URL("http://www.k2xl.info/auramap/server/gettags.php")).openConnection();
            
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

    
    
    
    /*
    public void onActivityResult  (int requestCode, int resultCode, Intent data){

    	Intent intent = new Intent();
    	intent.putExtra("webResponse",data.getExtras().getString("webResponse"));
    	//setResult(RESULT_OK, intent);
    	Log.v("Auramap", "Returned");
        Log.v("Auramap", intent.getExtras().getString("webresponse"));
    	finish();        
    } */   
    
 }
    


    

    
