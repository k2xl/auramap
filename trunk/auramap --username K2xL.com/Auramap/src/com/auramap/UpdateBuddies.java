package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.util.Log;

public class UpdateBuddies extends Activity {
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Log.v("Auramap", "Update Buddies...");

		Cursor C = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
		startManagingCursor(C);
		if (C.moveToFirst() == false) { Log.v("Auramap","No friends?"); }
		String contacts = "";
		do 
		{
			int index = C.getColumnIndexOrThrow(People.NUMBER_KEY);
			String num = C.getString(index);
			int size = num.length();
			char[] temp = new char[size];
			for(int h =0; h<size; h++) {				
				temp[h] = num.charAt(size-1-h);
			}
			num = new String(temp);
			Log.v("Auramap", "Number: " + num);
			contacts+=num+",0#";
		}while (C.moveToNext());
		contacts = contacts.substring(0,contacts.length()-1);
		Log.v("Auramap","Sending contacts..."+contacts);
		

		String toServer = "data="+contacts;
		
		
		Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/updateBuddyList.php");
        intent.putExtra("loadMessage","Updating Buddies");
        intent.putExtra("servMessage",toServer);
    	
        startActivityForResult(intent,0);
	}
	
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		setResult(resultCode, data);
		if (resultCode == RESULT_CANCELED ){finish(); return; }
    	String fromServer = data.getExtras().getString("webResponse");
    	Log.v("Auramap", "Server Result: " + fromServer);
  	    	
        
    	finish();

	}
}


