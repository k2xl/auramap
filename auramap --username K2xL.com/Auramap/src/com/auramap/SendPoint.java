package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SendPoint extends Activity {

	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.v("Auramap", "1");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		Bundle extras = getIntent().getExtras();

		String q = "";
		if (extras != null) {
			q = extras.getString("entry");
		}
		
		// Puts bundle in q
		q = "emotx="
				+ extras.getDouble("emotx") + "&" + "lat="
				+ extras.getDouble("lat") + "&" + "lon="
				+ extras.getDouble("lon") + "&" + "tag="
				+ extras.getString("tag");
		// Force
		q = "emotx="
			+ extras.getDouble("emotx") + "&" + "lat="
			+ Data.FAKELAT/1000000.0 + "&" + "lon="
			+ Data.FAKELON/1000000.0 + "&" + "tag="
			+ extras.getString("tag");
		Intent intent = new Intent(this.getBaseContext(), TextURL.class);
		intent.putExtra("URL",
				"http://www.k2xl.info/auramap/server/insertaura.php");
		intent.putExtra("loadMessage", "Sending Aurapoint");
		intent.putExtra("servMessage", q);
		startActivityForResult(intent, 0);
	}
	

	private void goToMap() {
		Intent i = new Intent(this.getBaseContext(), MoodMap.class);
		startActivity(i);
		finish();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		goToMap();
	}
}