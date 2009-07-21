package com.auramap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

public class GetTag extends Activity {
    public EditText tag;
    private String data;
    
    
    private OnClickListener sListener =new OnClickListener() {
    	
    	public void onClick(View v) {                
            sendAuraPoint();                          
        }
    };
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);          

        //data = this.getIntent().getExtras().getString("data");
        tag = (EditText) findViewById(R.id.tag);
        //createTagButtons();
        
    	Button tagButton = (Button) findViewById(R.id.globalTag01 );
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.globalTag02 );
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.globalTag03 );
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag01 );
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag02 );
        tagButton.setOnClickListener(tagListener);
        
        tagButton = (Button) findViewById(R.id.localTag03 );
        tagButton.setOnClickListener(tagListener);
  
    
    final ImageButton next = (ImageButton) findViewById(R.id.submit);    
    next.setOnClickListener(sListener);
        
    }
    
    private OnClickListener tagListener =new OnClickListener() {
    	public void onClick(View v) {
    		String str = tag.getText().toString();
    		Log.v("Auramap", "Old Tag: "+ str);
    		String newTag = ((Button) findViewById(v.getId())).getText().toString();
    		Log.v("Auramap", "Add Tag: "+ newTag);
    		if(str.equalsIgnoreCase("") ) str = newTag;
    		else if(!str.contains(", " + newTag)) str += ", " + newTag;
    		Log.v("Auramap", "New Tag: "+ str);
    		tag.setText(str);
    		    		
    	}
    };
    
    private void sendAuraPoint() {
        
        String t= tag.getText().toString();
    	Intent i = new Intent(this.getBaseContext(), ConnectionResource.class);				
		i.putExtras(this.getIntent().getExtras());
		i.putExtra("tag", t);
		startActivity(i);
		finish();
		
    }
    
    
    
    /*
    public void onActivityResult  (int requestCode, int resultCode, Intent data){

    	Intent intent = new Intent();
    	intent.putExtra("webResponse",data.getExtras().getString("webResponse"));
    	//setResult(RESULT_OK, intent);
    	Log.v("Auramap", "Returned");
        Log.v("Auramap", intent.getExtras().getString("webresponse"));
    	finish();        
    } */   
    
 }
    


    

    
