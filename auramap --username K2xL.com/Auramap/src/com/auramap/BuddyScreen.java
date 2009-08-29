package com.auramap;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BuddyScreen extends Activity {
	ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddyscreen);
		NavBar.adaptNav(this);
		getBuddies("");// "username="+Data.pNumber+"&password="+Data.pKey);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		pd.dismiss();
		String response = data.getExtras().getString("webResponse");
		String fromServer = "1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0";
		if (response.equals("[404]") == false) {
			fromServer = response;
		}

		// data.getExtras().getString("webResponse");
		Log.v("Auramap", "Buddy List Server Result: " + fromServer);
		String[][] buddyList;
		String[] sploded = fromServer.split("#");
		int tempS = sploded.length;
		buddyList = new String[tempS][4];
		for (int i = 0; i < tempS; i++) {
			buddyList[i] = sploded[i].split(",");
		}

		ListView list = (ListView) findViewById(R.id.BuddylistView);

		int[] imgStates = new int[6];
		imgStates[0] = R.drawable.blank;
		imgStates[1] = R.drawable.sad;
		imgStates[2] = R.drawable.lesssad;
		imgStates[3] = R.drawable.neutral;
		imgStates[4] = R.drawable.lesshappy;
		imgStates[5] = R.drawable.happy;

		ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map;
		for (int i = 0; i < tempS; i++) {
			map = new HashMap<String, Object>();
			map.put("phone", getContactNameFromNumber(Data
					.getNameFromNumber(buddyList[i][0])));
			map.put("rawphone", ""+buddyList[i][0]);
			double rawval = Double.parseDouble(buddyList[i][1]);
			int val = 0;
			if (rawval != -1) {
				val = 1 + (int) (4 * rawval);
			}
			map.put("state", imgStates[val]);
			if (buddyList[i][2].equals("0")) {
				map.put("update", "");
			} else {
				map.put("update", buddyList[i][2] + " seconds ago");
			}
			map.put("privacy", buddyList[i][3]);
			mylist.add(map);
		}

		SimpleAdapter mSchedule = new SimpleAdapter(this, mylist,
				R.layout.buddyrow, new String[] { "phone", "state", "update",
						"privacy","rawphone" }, new int[] { R.id.PHONE, R.id.STATE,
						R.id.UPDATE, R.id.PRIVACY });
		list.setAdapter(mSchedule);
		list.setClickable(true);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView arg0, View arg1, int arg2,
					long arg3) {
				Log.v("Auramap", "Item clicked "+arg2+","+arg3);
				
				int index = arg2;
				LinearLayout arg = (LinearLayout)arg1;
				TextView list = (TextView)arg.getChildAt(0);
				
				//TextView t = (TextView)arg0.getChildAt(1);
				HashMap<String, Object> map = (HashMap<String, Object>)arg0.getItemAtPosition(arg2);
				//Log.v("Auramap","Rawphone = "+map.get("rawphone"));
				
				seeBuddy(""+map.get("phone"), ""+map.get("rawphone"));
			}
		});
	}
	private void seeBuddy(String name, String phone)
	{
		//setContentView(R.layout.buddyview);
		Intent intent = new Intent(this.getBaseContext(), GetBuddy.class);
		intent.putExtra("name", name);
		intent.putExtra("phone", phone);
		startActivity(intent);
	}
	private void getBuddies(String toServer) {

		Intent intent = new Intent(this.getBaseContext(), TextURL.class);
		intent.putExtra("URL",
				"http://www.k2xl.info/auramap/server/getBuddyList.php");
		intent.putExtra("loadMessage", "Retrieving Tags");
		intent.putExtra("servMessage", toServer);
		pd = ProgressDialog.show(this, "Loading...",
				"Please wait while we get your buddies");

		startActivityForResult(intent, 0);
	}

	private String getContactNameFromNumber(String number) {
		// define the columns I want the query to return
		String[] projection = new String[] { Contacts.Phones.DISPLAY_NAME,
				Contacts.Phones.NUMBER };

		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(
				Contacts.Phones.CONTENT_FILTER_URL, Uri.encode(number));

		// query time
		Cursor c = getContentResolver().query(contactUri, projection, null,
				null, null);

		// if the query returns 1 or more results
		// return the first result
		if (c.moveToFirst()) {
			String name = c.getString(c
					.getColumnIndex(Contacts.Phones.DISPLAY_NAME));
			return name;
		}

		// return the original number if no match was found
		return number;
	}
}
