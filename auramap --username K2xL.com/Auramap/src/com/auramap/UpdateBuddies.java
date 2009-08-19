package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;

public class UpdateBuddies extends Activity {
	//public static HashMap<String,String> Contacts = new HashMap<String,String>();
	public void onCreate(Bundle b) {
		super.onCreate(b);
/*
		Cursor C = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
		startManagingCursor(C);
		
		int count = C.getCount();
		for (int i = 0; i < count; i++)
		{
			String name = C.getColumnIndex(People.NAME);
			Contacts.put(num, name)
		}
		*/
		//Contacts.People;
		//Log.v("Auramap", "Count:" + count);
		

		String toServer = "username=" + Data.pNumber + "&password=" + Data.pKey +"&contacts=";		
		Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/updatebuddies.php");
        intent.putExtra("loadMessage","Updating Buddies");
        intent.putExtra("servMessage",toServer);
    	
        startActivityForResult(intent,0);
	}
	
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
			
    	String fromServer = data.getExtras().getString("webResponse");
    	Log.v("Auramap", "Server Result: " + fromServer);
  	    	
        setResult(RESULT_OK, data);
    	finish();

	}
}


