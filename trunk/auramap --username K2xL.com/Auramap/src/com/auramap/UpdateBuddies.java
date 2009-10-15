package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;


public class UpdateBuddies extends Activity {
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Log.v("Auramap", "Update Buddies...");

		Cursor C = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
		startManagingCursor(C);
		if (C.moveToFirst() == false) { 		
			//setResult(RESULT_OK, null); 
			//Log.v("Auramap","No friends?"); 
			//finish(); return; 
		}
		String contacts = "";
		int count = 0;
		do 
		{
			int index = C.getColumnIndexOrThrow(People.NUMBER_KEY);
			count++;
			//Log.v("Auramap",C+" ..C count = "+index+"..,"+C.getString(C.getColumnIndexOrThrow(People.NUMBER_KEY)));
			String num = null;
			try{
			num = C.getString(index);
			}catch (CursorIndexOutOfBoundsException e)
			{
				continue;
			}
			if (num == null) { continue; }
			int size = num.length();
			char[] temp = new char[size];
			for(int h =0; h<size; h++) {				
				temp[h] = num.charAt(size-1-h);
			}
			num = new String(temp);
			//Log.v("Auramap", "Number: " + num);
			contacts+=num+",0#";
		}while (C.moveToNext());
		if (contacts.length() > 0){
			contacts = contacts.substring(0,contacts.length()-1);
		}
		else
		{
			//Log.v("Auramap","No friends... weird... There should be "+count);
			//setResult(RESULT_OK, null);
			//finish();
			//return;
		}
		
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


