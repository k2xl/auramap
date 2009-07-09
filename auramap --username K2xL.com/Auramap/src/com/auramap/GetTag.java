package com.auramap;

import java.net.*;
import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetTag extends Activity {
    public EditText tag;
    
    private OnClickListener sListener =new OnClickListener() {
    	
    	public void onClick(View v) {                
            String t= tag.getText().toString();
            Bundle b = new Bundle();
            b.putString("tag", t);

            setResult(RESULT_OK, null, b);

            // equivalent of 'return'
            finish();            
        }
    };
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);          

        tag = (EditText) findViewById(R.id.tag);
  
    
    final Button submit = (Button) findViewById(R.id.submit);
    
    submit.setOnClickListener(sListener);
        
    }
    
    
 }
    


    

    
