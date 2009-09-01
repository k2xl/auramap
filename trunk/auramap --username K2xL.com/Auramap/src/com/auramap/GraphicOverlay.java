package com.auramap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class GraphicOverlay extends Overlay {
        BitmapDrawable bmp;
        static int w, h;
        GeoPoint p;
        int zoom;
        MoodMap myMoodMap;
        public GraphicOverlay(Drawable d, GeoPoint p, MoodMap m) {
                bmp = (BitmapDrawable) d;
                this.p = p;
                w = bmp.getIntrinsicWidth();
                h = bmp.getIntrinsicHeight();
                zoom=14;
                myMoodMap = m;
        }

        public void draw(Canvas canvas, MapView mapView, boolean
shadow) {
        	
        		
                /*calculator.getPointXY(p, sXYCoords);
                bmp.setBounds(sXYCoords[0] - w / 2, sXYCoords[1] - h,
                                sXYCoords[0] + w / 2, sXYCoords[1]);*/
        		Projection proj = mapView.getProjection();
        		
        		
                //bmp.setAlpha(30);
                //bmp.draw(canvas);

                /*
                RectF oval = new RectF(xyCoords[0], xyCoords[1],
                                xyCoords[0] + 5, xyCoords[1] + 5);

                Paint paint = new Paint();
                paint.setARGB(200, 255, 0, 0);
                canvas.drawOval(oval, paint);
                */
                Paint paint = new Paint();
                paint.setAlpha(60);
                Point projP = proj.toPixels(p,null);
                if (bmp.getBitmap() == null) { return; }
                canvas.drawBitmap(bmp.getBitmap(), projP.x,projP.y, paint);
        }

        public GeoPoint getCenter() {
                return p;
        }

        public boolean dispatchMotionEvent(MotionEvent ev) {
                return false;
        }

} 