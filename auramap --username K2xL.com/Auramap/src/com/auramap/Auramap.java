package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Auramap extends Activity {
	/** Called when the activity is first created. */
	
	public static Activity Instance;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.loading);

		Intent i = new Intent(this.getBaseContext(), RegisterPhone.class);
		startActivityForResult(i, 10);
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED){ finish(); return; }
		Log.v("Auramap", "RequestCode: " + requestCode);
		//String res = data.getExtras().getString("Result");
		if (requestCode == 10) {
			Intent i = new Intent(this.getBaseContext(), UpdateBuddies.class);
			startActivityForResult(i, 11);

		} else if (requestCode == 11) {
			Intent i = new Intent( this, NudgeService.class );
			
			startService(i);
			//Toast t = Toast.makeText(this.getApplicationContext(), "Starting service...",1000);
	        //t.show();
			String GoToScreen = "";
			try
			{
				GoToScreen = this.getIntent().getExtras().getString("GoToScreen");
			}catch(NullPointerException e){
				GoToScreen = "";
			}
			if (GoToScreen.equals("")){
				Intent o = new Intent(this.getBaseContext(), QueryScreen.class);
				startActivity(o);
			}
			else
			{
				String[] splode = GoToScreen.split(",;,");
				GoToScreen = "";
				Intent intent = new Intent(this.getBaseContext(), GetBuddy.class);
				intent.putExtra("name", Data.getContactNameFromNumber(splode[1],getBaseContext().getContentResolver()));
				intent.putExtra("phone", splode[1]);
				startActivity(intent);
			}
			finish();
		}
	}

}
