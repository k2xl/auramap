package com.auramap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
	public final static String queryBase = "http://ngp.lcc.gatech.edu/php_scripts/droid_root.php?";
	String[] randTags = { "Traffic", "Whales", "Pizza", "Android", "Jackson",
			"Sunshine", "Paper Cups", "Weather", "Tulips", "Beer", "Flowers",
			"Rainbows", "Computer", "Android", "Girlfriend", "Boyfriend", "Kids",
			"Spaceships", "Dinosaurs", "Bears", "Pizza", "Fire",
			"Bruce Willis", "M Jackson", "Doughnuts", "Lightning", "Life",
			"Plague", "Baseball", "Hockey", "Money", "Economy", "Obama" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.v("Auramap", "1");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.state_disp);

		Bundle extras = getIntent().getExtras();

		String q = "overwrite_me";
		if (extras != null) {
			q = extras.getString("entry");
		}

		// Puts bundle in q
		q = "username=test&" + "password=test&" + "emotx="
				+ extras.getDouble("emotx") + "&" + "lat="
				+ extras.getDouble("lat") + "&" + "lon="
				+ extras.getDouble("lon") + "&" + "tag="
				+ extras.getString("tag");

		// Creates an optional view showing all the data it's sending off

		TextView vText = (TextView) findViewById(R.id.emote);
		vText.setText(extras.get("emote").toString());

		vText = (TextView) findViewById(R.id.happyX);
		vText.setText(extras.get("emotx").toString());

		vText = (TextView) findViewById(R.id.loc);
		vText.setText("Long: " + extras.get("lon").toString() + " Lat: "
				+ extras.get("lat").toString());
		final Button mapButton = (Button) findViewById(R.id.MapButton);
		mapButton.setOnClickListener(mListener);

		final Button genButton = (Button) findViewById(R.id.point_gen);
		genButton.setOnClickListener(gListener);

		Intent intent = new Intent(this.getBaseContext(), TextURL.class);
		intent.putExtra("URL",
				"http://www.k2xl.info/auramap/server/insertaura.php");
		intent.putExtra("loadMessage", "Sending Aurapoint");
		intent.putExtra("servMessage", q);
		startActivityForResult(intent, 0);
	}

	private OnClickListener gListener = new OnClickListener() {
		public void onClick(View v) {
			for (int i = 0; i < 100; i++) {
				String s = "";
				double eX = ((Math.round(Math.random() * 4))) / 4.0;
				// Log.v()
				double randomTechpointLat = 33.78156339080061 - Math.random() * 0.010487523427205;
				double randomTechpointLon = -84.38984870910645 - Math.random()
						* 0.01780986785888;

				int numTags = (int) (1 + Math.random() * 4);
				int startTag = (int) (Math.random() * (randTags.length - numTags));
				String tags = "&tag=";
				for (int y = 0; y < numTags; y++) {
					tags += randTags[y + startTag] + ",";
				}
				Log.v("...", tags);

				s += "username=test&password=test&emotx=" + eX + "&lat="
						+ randomTechpointLat + "&lon=" + randomTechpointLon
						+ tags;
				textURL(s);
			}
		}
	};
	private void startIntent(String q)
	{
        Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/insertaura.php");
        intent.putExtra("loadMessage","Sending Aurapoint");
        intent.putExtra("servMessage",q);
        startActivityForResult(intent, 0);
	}
	private OnClickListener mListener = new OnClickListener() {

		public void onClick(View v) {
			goToMap();
		}
	};

	private void goToMap() {

		Intent i = new Intent(this.getBaseContext(), MoodMap.class);
		startActivity(i);
		finish();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

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

}
