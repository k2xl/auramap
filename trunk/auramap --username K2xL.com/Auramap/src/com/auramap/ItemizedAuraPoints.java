package com.auramap;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class ItemizedAuraPoints extends ItemizedOverlay
{
	private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
	GeoPoint point;
	Paint auraImage;
	private Projection proj;
	public ItemizedAuraPoints(Drawable defaultMarker) {
		//super(defaultMarker);
		super(boundCenterBottom(defaultMarker));
		auraImage = new Paint();
		//auraImage = new Paint();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		super.draw(canvas, mapView,false);
		proj = mapView.getProjection();
		int zoomLevel = mapView.getZoomLevel();
		int tempS = items.size();
		for (int i = 0; i < tempS; i++)
		{
			point = items.get(i).getPoint();
			//double emoteRatio = 256*Integer.parseInt(items.get(i).getSnippet())+100.0/200.0;
			//int color = (((int)emoteRatio)<<6)|0x000000;
			int color = Integer.parseInt(items.get(i).getSnippet());
			auraImage.setColor(color);
			auraImage.setAlpha(63);
			Point pixelPoint = proj.toPixels(point, null);
			canvas.drawCircle(pixelPoint.x,pixelPoint.y,Math.max(3,(int)(222.0/Math.pow(2,19-zoomLevel))),auraImage);
		}
	}
	@Override
	protected OverlayItem createItem(int i) {
		return items.get(i);
	}
	public void addOverlay(OverlayItem overlay)
	{
		items.add(overlay);
		populate();
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return items.size();
	}
	
}
