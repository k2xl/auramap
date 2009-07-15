package com.auramap;

import java.net.*;
import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
  
    
    final Button next = (Button) findViewById(R.id.submit);    
    next.setOnClickListener(sListener);
        
    }
    
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
    


    

    
