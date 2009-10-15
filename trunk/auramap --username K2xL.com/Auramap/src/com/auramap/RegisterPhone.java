package com.auramap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterPhone extends Activity {
	private SharedPreferences settings;
	public void onCreate(Bundle b) {
		super.onCreate(b);
	
		settings = getSharedPreferences("HappyMapData", 0);

	    String passkey = settings.getString("MyPassKey", "");
	    TelephonyManager telephony = (TelephonyManager)
        
        getSystemService(TELEPHONY_SERVICE);
        String MyPhoneNumber = telephony.getLine1Number();
        if (MyPhoneNumber == null || MyPhoneNumber.equals(""))
        {
        	MyPhoneNumber = "3333333338";
        }
        Data.pNumber = MyPhoneNumber;
        //Data.pNumber = "3333333333";
        //Data.pNumber ="2222222220";//"17231165314"; "16555555556"; MyPhoneNumber;
        Data.pKey = passkey;
        //Data.pKey = "12345"; // debuggin
        
        // use textURL or something to call the server (registerphone.php)
		String toServer = "";
		//toServer+="username=" + Data.pNumber + "&password=" + Data.pKey;
		Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/registerphone.php");
        intent.putExtra("loadMessage","Retrieving Tags");
        intent.putExtra("servMessage",toServer);

        // california
		//Data.FAKELAT = (int)((37.748457761603355-Math.random()* -0.044235951909101)*1000000);
		//Data.FAKELON = (int)((-122.4481201171875-Math.random()* -0.05544662475586)*1000000);
        //gtech 33.781126,-84.407272
        double randomTechpointLat = 33.78156339080061 - Math.random() * 0.010487523427205;
		double randomTechpointLon = -84.38984870910645 - Math.random()* 0.01780986785888;
        Data.FAKELAT = (int)(randomTechpointLat*1000000); //(int)((33.78156339080061-Math.random()*-0.009846)*1000000);
        Data.FAKELON = (int)(randomTechpointLon*1000000); //(int)((-84.407272-Math.random()*-0.016479)*1000000);;
        // 
        //Data.FAKELAT = (int)((37.748457761603355-Math.random()* -0.044235951909101)*1000000);
		//Data.FAKELON = (int)((-122.4481201171875-Math.random()* -0.05544662475586)*1000000);
        
        startActivityForResult(intent,0);
	}
	
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
			
		
    	String fromServer = data.getExtras().getString("webResponse");
    	Log.v("Auramap", "Server Result: " + fromServer);
    	

    	if (fromServer.equals("SUCCESS")) {
    		data.putExtra("Result", "OK");
    	}
    	else if (fromServer.indexOf("FIRST")==0)
    	{
    		data.putExtra("Result", "FIRST");
    		String[] message = fromServer.split(",");
    		Log.v("Auramap","I got the passkey! It is "+fromServer);
        	SharedPreferences.Editor editor = settings.edit();
        	editor.putString("MyPassKey", message[1]);
        	Data.pKey = message[1];
            editor.commit();	
    	} 	
    	else //if (fromServer.contains("_ERROR"))
    	{
    		data.putExtra("Result", "ERROR");
    		Log.v("Auramap","Error = "+fromServer);
    	}
    	
        setResult(RESULT_OK, data);

    	finish();

	}
}
