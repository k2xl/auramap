package com.auramap;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressScreen extends Activity {
	public void onCreate()
	{
		ProgressDialog.show(this, "Loading...", "Switching screens give me a second!");

	}
}
