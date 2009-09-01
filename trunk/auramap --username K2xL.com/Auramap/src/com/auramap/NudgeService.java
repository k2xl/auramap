package com.auramap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NudgeService extends Service {

	int count;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public static final String URL = "http://www.k2xl.info/auramap/server/ping.php";
	private static Timer timer = new Timer();
	private static final int INTERVAL = 5000;
	private final int PING_SERVER = 111;
	NotificationManager mNM;

	public void onCreate() {
		super.onCreate();

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Toast t = Toast.makeText(this, "Aaaaaaaaaaaaaaaaaaaaaaaaaaah", 1000);
		// t.show();
		// startservice();
		count = 0;
		Thread td = new Thread(null, pinger, "NudgeService");
		td.start();

	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Log.v("NudgeService", "Begin NudgeListener");

	}

	Runnable pinger = new Runnable() {
		public void run() {

			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					count++;
					Log.v("NudgeService", "NudgeService is listening..." + count);
					callServer();
				}
			}, 0, INTERVAL);
		}
	};

	private void showRecieveNudgeNotification(String people) {
		CharSequence text = "" + people
				+ " wants to know how you are feeling right now.";
		if (people.indexOf(" and ") > 0) {
			text = "" + people
					+ " want to know how you are feeling right now.!";
		}
		Notification notification = new Notification(R.drawable.happy,
				"Someone wants to know how you feel!", System
						.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, HappyMap.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, "You've been nudged!", text,
				contentIntent);

		mNM.notify(count, notification);

	}

	private void showReturnNudgeNotification(String fromServer) {
		String[] sploded = fromServer.split(",;,");

		String name = Data.getContactNameFromNumber(sploded[0], this
				.getContentResolver());
		double state = Double.parseDouble(sploded[2]);
		String tags = sploded[3];

		String[] strStates = new String[5];
		strStates[0] = "sad";
		strStates[1] = "kind of sad";
		strStates[2] = "neutral";
		strStates[3] = "kind of happy";
		strStates[4] = "happy";

		int[] imgStates = new int[5];
		imgStates[0] = R.drawable.sad;
		imgStates[1] = R.drawable.lesssad;
		imgStates[2] = R.drawable.neutral;
		imgStates[3] = R.drawable.lesshappy;
		imgStates[4] = R.drawable.happy;
		int val = (int) (state * 4);

		CharSequence text = name + " feels " + strStates[val] + " about "
				+ tags;
		Notification notification = new Notification(imgStates[val],
				"Someone has responded to your nudge!", System
						.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		Intent i = new Intent(this, HappyMap.class);
		i.putExtra("GoToScreen", "Buddy,;,"+sploded[0]);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, name
				+ " has responded to your nudge.", text, contentIntent);

		mNM.notify(count, notification);

	}

	private void startservice() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				count++;
				Log.v("NudgeService", "NudgeService is listening..." + count);
				callServer();
			}
		}, 0, INTERVAL);
	}

	private void callServer() {
		String servStr = contactServer(URL, "username=" + Data.pNumber
				+ "&password=" + Data.pKey, "Checking nudges");
		Log.v("NudgeService", "NudgeService Response: " + servStr);
		String[] response = servStr.split("#");
		if (response[0].indexOf("0") == 0) {
			Log.v("NudgeService", "No nudges .... :-( ");
		} else if (response[0].indexOf("1") == 0) {
			String[] explode = response[0].split(",;,");
			if (explode[0].equals("1")) {
				String people = "";
				int tempS = explode.length;
				for (int i = 1; i < tempS; i++) {
					String person = Data.getContactNameFromNumber(explode[i],
							this.getContentResolver());
					people += person;
					if (i == tempS - 2 && tempS >= 2) {
						people += " and ";
					} else if (i < tempS - 2) {
						people += ",";
					}
				}
				Log.v("NudgeService", "You just got nudged by " + people);
				servStr = contactServer(URL, "username=" + Data.pNumber
						+ "&password=" + Data.pKey + "&clearnudges=1",
						"Clearing nudges");
				Log.v("NudgeService", "Clear Nudges Response: " + servStr);
				if (servStr.equals("SUCCESS")) {
					showRecieveNudgeNotification(people);
				} else if (servStr.contains("ERROR")) {
					this.stopSelf();
				}
			}

		}
		Log.v("NudgeService", "Response 1: " + response[1]);
		if (response[1].equals("0")==false) {
			servStr = contactServer(URL, "username=" + Data.pNumber
					+ "&password=" + Data.pKey + "&clearnudges=2",
					"Clearing nudges");			
			Log.v("NudgeService", "Clear Nudges Response: " + servStr);
			String sploded[] = response[1].split(";;;");
			
			int tempS = sploded.length;
			for (int i = 0; i < tempS; i++) {
				showReturnNudgeNotification(sploded[i]);
			}
		}
	}

	private String contactServer(String url, String servMessage,
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
			Log.v("Server", "Sending message: " + servMessage);
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
			return "[404]";
		}
		InputStreamReader isr = new InputStreamReader(in);
		int charRead;
		str = "";
		char[] inputBuffer = new char[BUFFER_SIZE];
		try {
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// ---convert the chars to a String---
				String readString = String
						.copyValueOf(inputBuffer, 0, charRead);
				str += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			str = "FAILED";
		}

		return str;
	}

	private void stopservice() {

		if (timer != null) {

			timer.cancel();

		}

	}
}
