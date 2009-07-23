package com.auramap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
    ProgressDialog pd;
    boolean localTagsRetrieved=false;
    boolean globalTagsRetrieved=false;
    
    private int[] colorsHEX = { 0x33CCCC, 0x66CC99,  0x99CC66, 0xCC9933, 0xFF9900 };
    private final int numTags = 3;
    
    String[] localTagNames;
    int[] localTagColors;
    String[] globalTagNames;
    int[] globalTagColors;
    
    
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
    	int[] c = new int[3];
    	c[0] = rgb>>16&0xFF;
    	c[1] = rgb>>8&0xFF;
    	c[2] = rgb&0xFF;
    	return c;
	}
	private int getColorFromGradient(int lowC, int highC,double perc)
	{
		Log.v("Aa","Low C = "+lowC+",highC"+highC+",perc="+perc);
		return averageColors(lowC,highC,1-perc,perc);
	}
    private void setupLocalTags(String toServer) {
    	toServer += "&radius=100&numresults=" + numTags;
    	//pd = ProgressDialog.show(this, "Loading...", "Please wait while we get the local tags from the server");	
    	
    	Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/gettags.php");
        intent.putExtra("loadMessage","Retrieving Tags");
        intent.putExtra("servMessage",toServer);
        startActivityForResult(intent, 0);
    }
    
    private void setupGlobalTags(String toServer) {

    	toServer += "&radius=5000000&numresults=" + numTags;
    	//pd = ProgressDialog.show(this, "Loading...", "Please wait while we get the global tags from the server");	
    	
    	Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/gettags.php");
        intent.putExtra("loadMessage","Retrieving Tags");
        intent.putExtra("servMessage",toServer);
        startActivityForResult(intent, 1);
    	
    }
    
    public void onActivityResult (int requestCode, int resultCode, Intent data) {

    	//pd.dismiss();
    	String fromServer = data.getExtras().getString("webResponse");
    	Log.v("ddd", "CR: Code: " + requestCode + " Response: " + fromServer);
    	
  
    	if(requestCode == 0) {
        	
        	localTagNames[0] = "[no data]";
        	localTagNames[1] = "[no data]";
        	localTagNames[2] = "[no data]";

        	localTagColors[0] = 0x999999;
        	localTagColors[1] = 0x999999;
        	localTagColors[2] = 0x999999;

            if (fromServer.equals("EMPTY_RESULT") == true) {
            	return;
            }
            
        	String[] sploded = fromServer.split("#");
        	int tempS = sploded.length;
        	for(int i=0; i<tempS; i++) {
        		String[] anotherTemp = sploded[i].split(",");
        		localTagNames[i]=anotherTemp[0];   
        		double d = Double.parseDouble(anotherTemp[2]);
        		
        		double realVal = (d*((colorsHEX.length)-1));
        		int lowC = (int)Math.floor(realVal);
        		int highC = (int)Math.ceil(realVal);
        		

        		double percAcross = (realVal-lowC);
        		
        		
        		localTagColors[i]= getColorFromGradient(colorsHEX[lowC],colorsHEX[highC],percAcross);
        		}
            localTagsRetrieved=true;
    	
    	} else if(requestCode==1) {

        	globalTagNames[0] = "[no data]";
    		globalTagNames[1] = "[no data]";
    		globalTagNames[2] = "[no data]";

    		globalTagColors[0] = 0x999999;
        	globalTagColors[1] = 0x999999;
        	globalTagColors[2] = 0x999999;

        	if (fromServer.equals("EMPTY_RESULT") == true) {
            	return;
            }
        	String[] sploded = fromServer.split("#");
        	int tempS = sploded.length;
        	for(int i=0; i<tempS; i++) {
        		String[] anotherTemp = sploded[i].split(",");
        		globalTagNames[i]=anotherTemp[0];   
        		double d = Double.parseDouble(anotherTemp[2]);
        	
        		
        		double realVal = (d*((colorsHEX.length)-1));
        		int lowC = (int)Math.floor(realVal);
        		int highC = (int)Math.ceil(realVal);
        		

        		double percAcross = (realVal-lowC);
        		
        		
        		globalTagColors[i]= getColorFromGradient(colorsHEX[lowC],colorsHEX[highC],percAcross);
        		}

            globalTagsRetrieved=true;
    	}
    	if(localTagsRetrieved && globalTagsRetrieved) setupButtons();
    }
    
    public void setupButtons() {
        //parseResponse();
    	Button tagButton = (Button) findViewById(R.id.globalTag01 );
        tagButton.setText(globalTagNames[0]);
        tagButton.setBackgroundColor((globalTagColors[0]));
    	tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.globalTag02 );
        tagButton.setText(globalTagNames[1]);
        tagButton.setBackgroundColor((globalTagColors[1]));
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.globalTag03 );
        tagButton.setText(globalTagNames[2]);
        tagButton.setBackgroundColor((globalTagColors[2]));
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag01 );
        tagButton.setText(localTagNames[0]);
        tagButton.setBackgroundColor((localTagColors[0]));
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag02 );
        tagButton.setText(localTagNames[1]);
        tagButton.setBackgroundColor((localTagColors[1]));
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag03 );
        tagButton.setText(localTagNames[2]);
        tagButton.setBackgroundColor((localTagColors[2]));
        tagButton.setOnClickListener(tagListener);
  
       
    
    final ImageButton next = (ImageButton) findViewById(R.id.submit);    
    next.setOnClickListener(sListener);
    	
    }

    
    

    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);
        
        localTagNames = new String[5];
        localTagColors = new int[5];
        globalTagNames = new String[5];
        globalTagColors = new int[5];

        tag = (EditText) findViewById(R.id.tag);
        
        String toServer = "username=" + this.getIntent().getExtras().getString("username") +
        				  "&password=" + this.getIntent().getExtras().getString("password") + 
        				  "&lat=" + this.getIntent().getExtras().getDouble("lat") +  
        				  "&lon=" + this.getIntent().getExtras().getDouble("lon");
        Log.v("Auramap", "Tags To Server: " + toServer);
        setupLocalTags(toServer);
        setupGlobalTags(toServer);

    
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
    	super.onWindowFocusChanged(hasFocus);

         
         ImageButton happymeter = (ImageButton)findViewById(R.id.happymeter);
         double emotval = this.getIntent().getExtras().getDouble("emotx");
         
         int padding = (int)(emotval*happymeter.getWidth()-happymeter.getDrawable().getIntrinsicWidth()/2);
         if (emotval == 0) { padding = -200; happymeter.setImageDrawable( (Drawable)getResources().getDrawable(R.drawable.sad));}
         else if (emotval == .25) { padding = -100;happymeter.setImageDrawable( (Drawable)getResources().getDrawable(R.drawable.lesssad)); }
         else if (emotval == .5) { padding = 0;happymeter.setImageDrawable( (Drawable)getResources().getDrawable(R.drawable.neutral)); }
         else if (emotval == .75) { padding = 100;happymeter.setImageDrawable( (Drawable)getResources().getDrawable(R.drawable.lesshappy)); }
         else if (emotval == 1) { padding = 200;happymeter.setImageDrawable( (Drawable)getResources().getDrawable(R.drawable.happy)); }
         if (padding > 0){padding+=17;
         happymeter.setPadding(padding, 0, 0, 0); // 17 is width of that left silver bar thing on scroll
         }
         if (padding < 0)
         {
        	 padding-=17;
        	 happymeter.setPadding(0, 0, -(padding), 0); // 17 is width of that left silver bar thing on scroll	 
         }
         
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
    			tag.setSelection(tag.length());
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
    
 }
    


    

    
