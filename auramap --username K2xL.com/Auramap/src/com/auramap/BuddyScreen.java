package com.auramap;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BuddyScreen extends Activity {	
	ProgressDialog pd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddyscreen);
        NavBar.adaptNav(this);
        getBuddies("username="+Data.pNumber+"&password="+Data.pKey);
    }
	
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
    	pd.dismiss();
    	String response = data.getExtras().getString("webResponse");
    	String fromServer = "1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0#1234576,2,43,0#4321,4,62,0";
    	if (response.equals("[404]") == false)
    	{
    		fromServer = response;
    	}
    	 
    		
    		//data.getExtras().getString("webResponse");
    	Log.v("sss0", "Server Result: " + fromServer);
    	String[][] buddyList;
    	String[] sploded = fromServer.split("#");
    	int tempS = sploded.length;
    	buddyList = new String[tempS][4];
    	for(int i=0; i<tempS; i++) {    		
    		buddyList[i] = sploded[i].split(",");
    	}    	    	
    	
    	
    	ListView list = (ListView) findViewById(R.id.BuddylistView);
        
    	int[] imgStates = new int[6];
    	imgStates[0] = R.drawable.blank;
    	imgStates[1] = R.drawable.sad;
    	imgStates[2] = R.drawable.lesssad;
    	imgStates[3] = R.drawable.neutral;
    	imgStates[4] = R.drawable.lesshappy;
    	imgStates[5] = R.drawable.happy;
    	
        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for(int i=0; i<tempS; i++) {    		
            map = new HashMap<String, Object>();            
        	map.put("phone", Data.getNameFromNumber(buddyList[i][0]));
        	map.put("state", imgStates[1 + Integer.parseInt(buddyList[i][1])]);
        	if (buddyList[i][2] == "0"){
        		map.put("update", "");
        	}else{
        		map.put("update", buddyList[i][2]+" seconds ago");
        	}
        	map.put("privacy", buddyList[i][3]);
        	mylist.add(map);
        }

        
        
        SimpleAdapter mSchedule = new SimpleAdapter(this, mylist, R.layout.buddyrow,
                    new String[] {"phone", "state", "update", "privacy"}, new int[] {R.id.PHONE, R.id.STATE, R.id.UPDATE, R.id.PRIVACY});
        list.setAdapter(mSchedule);		
    }
    
    private void getBuddies(String toServer) {
    	
    	Intent intent = new Intent(this.getBaseContext(), TextURL.class);
        intent.putExtra("URL","http://www.k2xl.info/auramap/server/getBuddyList.php");
        intent.putExtra("loadMessage","Retrieving Tags");
        intent.putExtra("servMessage",toServer);
        pd = ProgressDialog.show(this, "Loading...", "Please wait while we get your buddies");	
    	
        startActivityForResult(intent, 0);
    }
}
