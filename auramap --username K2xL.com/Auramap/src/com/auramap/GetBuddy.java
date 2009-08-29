package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class GetBuddy extends Activity {
	private TextView lastupdated, lasttags;
	private String number;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddyview);
		Bundle extras = getIntent().getExtras();
		String name = extras.getString("name");
		number = extras.getString("phone");
		TextView buddyname = (TextView) findViewById(R.id.Buddyname);
		buddyname.setText(name);

		lastupdated = (TextView) findViewById(R.id.Profilelastupdated);
		lastupdated.setText("Loading...");

		lasttags = (TextView) findViewById(R.id.Profiletags);
		lasttags.setText("Loading tags...");

		Intent i = new Intent(this.getBaseContext(), TextURL.class);
		i.putExtra("URL", "http://www.k2xl.info/auramap/server/getBuddy.php");
		i.putExtra("servMessage", "buddynumber=" + number);
		startActivityForResult(i, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String fromServer = data.getExtras().getString("webResponse");
		int[] imgStates = new int[6];
		imgStates[0] = R.drawable.blank;
		imgStates[1] = R.drawable.sad2;
		imgStates[2] = R.drawable.lesssad2;
		imgStates[3] = R.drawable.neutral2;
		imgStates[4] = R.drawable.lesshappy2;
		imgStates[5] = R.drawable.happy2;
		if (fromServer.indexOf("UNKNOWN_PHONE") >= 0) {
			lastupdated.setText("No data found for phone");
			ImageView status = (ImageView) findViewById(R.id.Buddystatus);
			status.setImageDrawable(getResources().getDrawable(R.drawable.mapicon));
			lasttags.setText(number);
			return;
		}
		
		String[] strdata = fromServer.split("#");
		String secondsago = Long.parseLong(strdata[0])+" seconds ago.";
		double rawval = Double.parseDouble(strdata[1]);
		int val = 0;
		if (rawval != -1) {
			val = 1 + (int) (4 * rawval);
		}
		ImageView status = (ImageView) findViewById(R.id.Buddystatus);
		status.setImageDrawable(getResources().getDrawable(imgStates[val]));
		
		lastupdated.setText(secondsago);
		lasttags.setText("Tags: "+strdata[2]);
		Log.v("Auramap", "Server Result: " + fromServer);
	}
}
