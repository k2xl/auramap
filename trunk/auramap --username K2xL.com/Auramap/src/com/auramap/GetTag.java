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
    public static int averageColors(int col1,int col2,double weight1 ,double weight2)
	{
		int[] c1 = getRGB(col1);
		int[] c2 = getRGB(col2);
		int r = (int) ((c1[0]*weight1+c2[0]*weight2)/(weight1+weight2));
		int g = (int) ((c1[1]*weight1+c2[1]*weight2)/(weight1+weight2));
		int b = (int) ((c1[2]*weight1+c2[2]*weight2)/(weight1+weight2));
		return Color.rgb(r, g, b);
	}
    public static int[] getRGB(int rgb){
    	int[] c = new int[2];
    	c[0] = rgb>>16&0xFF;
    	c[1] = rgb>>8&0xFF;
    	c[2] = rgb&0xFF;
    	return c;
	}
	
    private void setupLocalTags(String toServer) {
    	String fromServer = textURL(toServer + "&radius=100&numresults=" + numTags);
    	localTagNames[0] = "[no data]";
    	localTagNames[1] = "[no data]";
    	localTagNames[2] = "[no data]";
    	localTagColors[0] = "#CCCCCC";
    	localTagColors[1] = "#CCCCCC";
    	localTagColors[2] = "#CCCCCC";
        if (fromServer.equals("EMPTY_RESULT") == true) {
        	return;
        }
        
    	String[] sploded = fromServer.split("#");
    	int tempS = sploded.length;
    	for(int i=0; i<tempS; i++) {
    		String[] anotherTemp = sploded[i].split(",");
    		localTagNames[i]=anotherTemp[0];   
    		Double d = Double.parseDouble(anotherTemp[2]);
    		int n = (int)Math.round(d*4);
    		localTagColors[i]= colors[n];
    		}
    }
    
    private void setupGlobalTags(String toServer) {

    	String fromServer = textURL(toServer + "&radius=5000&numresults=" + numTags);
    	globalTagNames[0] = "[no data]";
		globalTagNames[1] = "[no data]";
		globalTagNames[2] = "[no data]";
		globalTagColors[0] = "#CCCCCC";
    	globalTagColors[1] = "#CCCCCC";
    	globalTagColors[2] = "#CCCCCC";
    	if (fromServer.equals("EMPTY_RESULT") == true) {
        	return;
        }
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
        
        Log.v("aaa","local1 = "+localTagColors[0]);
        Log.v("aaa","local2 = "+localTagColors[1]);
        Log.v("aaa","local3 = "+localTagColors[2]);
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
    		String[] splitted = str.split(",");
    		boolean found = false;
    		int tempS = splitted.length;
    		for (int i = 0 ; i < tempS; i++)
    		{
    			if (splitted[i].equals(newTag))
    			{
    				found =true;
    				break;
    			}
    		}
    		
    		if (!found){
    			if (tempS == 0){ tag.setText(newTag+","); }
    			else{
    			tag.setText(str+""+newTag+",");
    			}
    		}
    		    		
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
    


    

    
