package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetBuddy extends Activity {
	private TextView lastupdated, lasttags;
	private String number;
	final int LOAD_BUDDY_INFORMATION = 111;
	final int NUDGE_BUDDY = 222;

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

		final Button backButton = (Button) findViewById(R.id.BackButton);
		backButton.setOnClickListener(backListener);
		final Button nudgeButton = (Button) findViewById(R.id.SendNudgeButton);
		nudgeButton.setOnClickListener(nListener);
		startActivityForResult(i, LOAD_BUDDY_INFORMATION);
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String fromServer = data.getExtras().getString("webResponse");
		Log.v("Auramap", "Get Buddy Server Result: " + fromServer);
		
		if(requestCode == LOAD_BUDDY_INFORMATION) {
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
			
			String[] strdata = fromServer.split(",;,");
			String secondsago = Data.distanceTo(Long.parseLong(strdata[0]), true);
			double rawval = Double.parseDouble(strdata[1]);
			int val = 0;
			if (rawval != -1) {
				val = 1 + (int) (4 * rawval);
			}
			ImageView status = (ImageView) findViewById(R.id.Buddystatus);
			status.setImageDrawable(getResources().getDrawable(imgStates[val]));
			
			lastupdated.setText(secondsago);
			lasttags.setText("Tags: "+strdata[2]);
			
			if (strdata[3].equals("0"))
			{
				Button nudgeB = (Button) findViewById(R.id.SendNudgeButton);
				nudgeB.setEnabled(false);
			}
			
			Log.v("Auramap", "Server Result: " + fromServer);
			
		} else if (requestCode == NUDGE_BUDDY) {

			final Button nudgeButton = (Button) findViewById(R.id.SendNudgeButton);
			if(fromServer.contains("SUCCESS")==false) {
				nudgeButton.setEnabled(true);				
			}
		}
	}
	
	private OnClickListener nListener = new OnClickListener() {
		public void onClick(View v) {

			final Button nudgeButton = (Button) findViewById(R.id.SendNudgeButton);
			nudgeButton.setEnabled(false);
			nudgeBuddy();
		}
	};
	private OnClickListener backListener = new OnClickListener() {
		public void onClick(View v) {
			exit();
		}
	};
	
	private void exit() {
		finish();
	}
	
	private void nudgeBuddy() {
		Intent i = new Intent(this.getBaseContext(), TextURL.class);
		i.putExtra("URL", "http://www.k2xl.info/auramap/server/sendNudge.php");
		i.putExtra("servMessage", "target="+number);
		i.putExtra("loadMessage", "Nudging Buddy...");
		startActivityForResult(i, NUDGE_BUDDY);
	}
	
	
}
