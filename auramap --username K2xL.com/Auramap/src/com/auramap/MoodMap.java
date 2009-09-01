package com.auramap;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class MoodMap extends MapActivity implements OnTouchListener {
	ProgressDialog pd;
	BitmapDrawable bmp;
	static Bitmap happyMap;
	Bitmap happyGrid[][];
	LinearLayout linearLayout;
	GeoPoint curPoint;
	GeoPoint topLeft;
	GeoPoint bottomRight;
	int mHeight, mWidth;
	final int SPAN_X = 320;
	final int SPAN_Y = 480;
	MapView mapView;
	// MapListener mListener;
	ZoomControls mapZoom;
	MapController mc;
	OverlayItem[] items;
	LocationManager manager;
	Location location; // location
	LocationListener locationListener;
	private final int GET_AURAPOINTS = 32890;
	private final int GET_HAPPYMAP = 23489;
	private static Timer timer = new Timer();
	private static final int INTERVAL = 1000;
	boolean isUpdating = true;
	private int zoomLevel;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		NavBar.adaptNav(this);

		mapView = (MapView) findViewById(R.id.thismap);
		mapView.setBuiltInZoomControls(true);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;
		;
		mHeight = dm.heightPixels;
		;
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = manager.getLastKnownLocation("gps");
		locationListener = new MyLocationListener();
		int latData = (int) (1000000 * location.getLatitude());
		int lonData = (int) (1000000 * location.getLongitude());
		curPoint = new GeoPoint(latData, lonData);
		happyGrid = new Bitmap[3][3];
		animateToCurrentPoint();
		getImage();
		zoomLevel = 14;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		onTouch(mapView, ev);
		super.dispatchTouchEvent(ev);

		return true; // super.dispatchTouchEvent(ev);
	}

	public boolean onTouch(View v, MotionEvent e) {
		
		Log.v("Auramap", "" + e.getAction() + "..." + e.ACTION_UP);
		if (happyMap != null)
		{
			mapView.getOverlays().clear();
			happyMap.recycle();
		}
		if (zoomLevel != mapView.getZoomLevel())
		{
			Log.v("Test",zoomLevel+" change");
			zoomLevel = mapView.getZoomLevel();
			getImage();
		}
		if (e.getAction() == (e.ACTION_UP) == false) {
			return super.onTouchEvent(e);
		}
		
		Projection p = mapView.getProjection();
		Point cp = new Point();
		p.toPixels(mapView.getMapCenter(), cp);
		Point edge = new Point();
		p.toPixels(topLeft, edge);
		int xBound = Math.abs(Math.abs(cp.x) - Math.abs(edge.x));
		int yBound = Math.abs(Math.abs(edge.y) - Math.abs(cp.y));

		Log.v("Auramap", "Distance x | y: " + xBound + " | " + yBound);
		Log.v("Auramap", "Bounds X: " + (mWidth / 2) + " | "
				+ (SPAN_X - mWidth / 2));
		Log.v("Auramap", "Bounds Y: " + (mHeight / 2) + " | "
				+ (SPAN_Y - mHeight / 2));
		if (xBound < mWidth / 2 || xBound > SPAN_X - mWidth / 2
				|| yBound < mHeight / 2 || yBound > SPAN_Y - mHeight / 2) {
			isUpdating = true;
			getImage();
		}

		return true;
	}

	private void animateToCurrentPoint() {

		mapView = (MapView) findViewById(R.id.thismap);
		mc = mapView.getController();
		mc.setCenter(curPoint);
		mc.setZoom(14);

	}

	private void drawPoints() {
		BitmapDrawable drawable = new BitmapDrawable(happyMap);

		mapView.setAlwaysDrawnWithCacheEnabled(true);
		mapView.setDrawingCacheEnabled(true);

		CurrentPointOverlay cpo = new CurrentPointOverlay();
		GraphicOverlay bmpOverlay = new GraphicOverlay(drawable, topLeft);

		mapView.getOverlays().add(bmpOverlay);
		mapView.getOverlays().add(cpo);
		mapView.invalidate();
		isUpdating = false;
		// pd.dismiss();
	}

	private void drawGrid() {
		BitmapDrawable drawable = new BitmapDrawable(happyMap);

		mapView.setAlwaysDrawnWithCacheEnabled(true);
		mapView.setDrawingCacheEnabled(true);

		CurrentPointOverlay cpo = new CurrentPointOverlay();
		GraphicOverlay bmpOverlay = new GraphicOverlay(drawable, topLeft);
		mapView.getOverlays().clear();
		mapView.getOverlays().add(bmpOverlay);
		mapView.getOverlays().add(cpo);
		mapView.invalidate();
		isUpdating = false;
		// pd.dismiss();
	}

	private void getImage() {
		if (isUpdating) {
			Toast.makeText(this, "Updating Map", 1000).show();
			
			mapView.getOverlays().clear();
			Projection p = mapView.getProjection();
			int screenWidth = mWidth;
			int screenHeight = mHeight;
			Log.v("Test", "" + (mWidth) + "," + mHeight + " should be 320,480");
			// curPoint = p.fromPixels(0, 0);
			curPoint = mapView.getMapCenter();
			topLeft = p.fromPixels(SPAN_X / -2 + screenWidth / 2, SPAN_Y / -2
					+ screenHeight / 2);

			bottomRight = p.fromPixels(SPAN_X / 2 + screenWidth / 2, SPAN_Y / 2
					+ screenHeight / 2);

			double latInit = topLeft.getLatitudeE6() / 1000000.0;
			double lonInit = topLeft.getLongitudeE6() / 1000000.0;
			double latFin = bottomRight.getLatitudeE6() / 1000000.0;
			double lonFin = bottomRight.getLongitudeE6() / 1000000.0;
			// Log.v("Test",latInit+","+lonInit+"latFin"+lonFin);
			Log.v("Auramap", "Zoom Pixel= " + 30
					* Math.pow(2, 14 - mapView.getZoomLevel()));
			String servMessage = "username=" + Data.pNumber + "&password="
					+ Data.pKey + "&ilat=" + latInit + "&flat=" + latFin
					+ "&ilon=" + lonInit + "&flon=" + lonFin + "&w=" + SPAN_X
					+ "&h=" + SPAN_Y + "&radius=" + 30
					* Math.pow(2, mapView.getZoomLevel() - 14);
			String url = "http://www.k2xl.info/auramap/server/getMap.php";
			Log.v("Server", "Image Server Message: " + servMessage);
			if (happyMap != null) { mapView.getOverlays().clear();
			happyMap.recycle(); }
			Intent i = new Intent(this.getBaseContext(), ImageURL.class);
			i.putExtra("servMessage", servMessage);
			i.putExtra("URL", url);
			i.putExtra("loadMessage", "Loading from server");
			startActivityForResult(i, 123);
			// happyMap = contactServer(url, servMessage,
			// "Retrieving HappyMap");
			// drawPoints();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*String response = data.getExtras().getString("webResponse");
		happyMap = BitmapFactory.decodeByteArray(response.getBytes(), 0, response.getBytes().length);
		//Log.v("Test",happyMap.getWidth()+","+happyMap.getHeight());
		//happyMap = getBitMapFromString(ba);
		Log.v("Test",happyMap+", "+response.length()+" vs "+response.getBytes().length);*/
		 drawPoints();
	}

	public static Bitmap getBitMapFromString(String src) {
		Log.i("b=", "" + src.getBytes().length);
		return BitmapFactory.decodeByteArray(src.getBytes(), 0,
				src.getBytes().length);
	}

	private void getImageGrid() {

		// pd = ProgressDialog.show(this.getBaseContext(), "Please wait",
		// "Please wait while we get the HappyMap from the server.");

		if (isUpdating) {
			Toast.makeText(this, "Updating Map", 1000).show();
			Projection p = mapView.getProjection();

			int screenWidth = mWidth;
			int screenHeight = mHeight;
			curPoint = mapView.getMapCenter();

			int xItr = mWidth / 2 - SPAN_X - SPAN_X / 2;
			int yItr = mHeight / 2 - SPAN_Y - SPAN_Y / 2;

			String url = "http://www.k2xl.info/auramap/server/getMap.php";
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					topLeft = p.fromPixels(xItr, yItr);
					bottomRight = p.fromPixels(xItr + SPAN_X, yItr + SPAN_Y);
					double latInit = topLeft.getLatitudeE6() / 1000000.0;
					double lonInit = topLeft.getLongitudeE6() / 1000000.0;
					double latFin = bottomRight.getLatitudeE6() / 1000000.0;
					double lonFin = bottomRight.getLongitudeE6() / 1000000.0;

					String servMessage = "username=" + Data.pNumber
							+ "&password=" + Data.pKey + "&ilat=" + latInit
							+ "&flat=" + latFin + "&ilon=" + lonInit + "&flon="
							+ lonFin + "&w=" + SPAN_X + "&h=" + SPAN_Y
							+ "&radius=" + 30;
					Log.v("Server", "Image Server Message: " + servMessage);
					happyGrid[i][j] = contactServer(url, servMessage,
							"Retrieving HappyMap");

					xItr += SPAN_X;
				}
				yItr += SPAN_Y;
				xItr = mWidth / 2 - SPAN_X - SPAN_X / 2;
			}

			drawGrid();
		}
	}

	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location newLoc) {
			if (newLoc != null) {
				location = newLoc;
			}

		}

		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class CurrentPointOverlay extends com.google.android.maps.Overlay {
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(curPoint, screenPts);

			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					android.R.drawable.ic_menu_mylocation);
			canvas.drawBitmap(bmp, screenPts.x - bmp.getWidth() / 2,
					screenPts.y - bmp.getHeight() / 2, null);
			return true;
		}
	}

	private Bitmap contactServer(String url, String servMessage,
			String loadMessage) {

		String str;
		Log.v("Auramap", "Map Server Message: " + servMessage);

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
			return null;
		}

		BitmapFactory bmpFact = new BitmapFactory();
		Bitmap bmp = bmpFact.decodeStream(in);

		return bmp;
	}

}
