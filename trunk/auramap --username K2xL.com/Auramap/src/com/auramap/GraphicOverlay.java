package com.auramap;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class GraphicOverlay extends Overlay {
        BitmapDrawable bmp;
        static int w, h;
        GeoPoint p;

        public GraphicOverlay(Drawable d, GeoPoint p) {
                bmp = (BitmapDrawable) d;
                this.p = p;
                w = bmp.getIntrinsicWidth();
                h = bmp.getIntrinsicHeight();
        }

        public void draw(Canvas canvas, MapView mapView, boolean
shadow) {
        	
                /*calculator.getPointXY(p, sXYCoords);
                bmp.setBounds(sXYCoords[0] - w / 2, sXYCoords[1] - h,
                                sXYCoords[0] + w / 2, sXYCoords[1]);*/
        		Projection proj = mapView.getProjection();
        		int zoomLevel = mapView.getZoomLevel();
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
                Point projP = proj.toPixels(p,null);
                canvas.drawBitmap(bmp.getBitmap(), projP.x,projP.y, paint);
        }

        public GeoPoint getCenter() {
                return p;
        }

        public boolean dispatchMotionEvent(MotionEvent ev) {
                return false;
        }

} 