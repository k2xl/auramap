package com.auramap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

public class ImageURL extends Activity {

	private ProgressDialog pd;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle b = this.getIntent().getExtras();

		String url = b.getString("URL");
		String loadMessage = b.getString("loadMessage");

		String servMessage = "username=" + Data.pNumber + "&password="
				+ Data.pKey + "&" + b.getString("servMessage");
		Bitmap response = contactServer(url, servMessage, loadMessage);

		Intent i = new Intent();
		i.putExtra("webResponse", response);
		setResult(RESULT_OK, i);
		finish();
	}

	private Bitmap contactServer(String url, String servMessage,
			String loadMessage) {

		String str;

		int BUFFER_SIZE = 2000;
		InputStream in = null;

		try {
			HttpURLConnection con = (HttpURLConnection) (new URL(url))
					.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("METHOD", "POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			Log.v("Auramap", "Sending message: " + servMessage);
			// add url form parameters
			DataOutputStream ostream = null;
			try {
				ostream = new DataOutputStream(con.getOutputStream());
				ostream.writeBytes(servMessage);
			} finally {
				if (ostream != null) {
					ostream.flush();
					ostream.close();
				}
			}

			in = con.getInputStream();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			str = e1.toString();
		}
		if (in == null) {
			return null;}

		BitmapFactory bmpFact = new BitmapFactory();
		Bitmap bmp = bmpFact.decodeStream(in);

		return bmp;
	}

}