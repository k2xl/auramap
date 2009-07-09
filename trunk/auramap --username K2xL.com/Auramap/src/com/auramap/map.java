
package com.auramap;

import android.os.Bundle;
import com.google.android.maps.MapActivity;

public class map extends MapActivity {
	
	public void OnCreate(Bundle b){
		setContentView(R.layout.map);		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}