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

        Data.pNumber = MyPhoneNumber;
        Data.pKey = passkey;
        
        // use textURL or something to call the server (registerphone.php)
		String toServer = "";
		toServer+="username=" + MyPhoneNumber + "&password=" + passkey;
		Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/registerphone.php");
        intent.putExtra("loadMessage","Retrieving Tags");
        intent.putExtra("servMessage",toServer);
    	
        startActivityForResult(intent,0);
	}
	
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
			
		
    	String fromServer = data.getExtras().getString("webResponse");
    	Log.v("sss0", "Server Result: " + fromServer);
    	

    	if (fromServer.equals("SUCCESS")) {
    		
    		finish();
    		return;
    	}
    	else if (fromServer.contains("_ERROR"))
    	{
    		setContentView(R.layout.default_error);
    		Log.v("sss","Error = "+fromServer);
    		return;
    	}
    	else
    	{
    		Log.v("sss","I got the passkey! It is "+fromServer);
        	SharedPreferences.Editor editor = settings.edit();
        	editor.putString("MyPassKey", fromServer);
        	Data.pKey = fromServer;
            editor.commit();	
        	finish();

    	}
	}
}
