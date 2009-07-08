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
    /** Called when the activity is first created. */
        public EditText tag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);          

        tag = (EditText) findViewById(R.id.tag);
    
    final Button submit = (Button) findViewById(R.id.submit);
    submit.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {                
            String t= tag.getText().toString();
            Intent nIntent = new Intent(v.getContext(), ConnectionResource.class);
            nIntent.putExtra("tag",t);
            startActivityForResult(nIntent, 0);
        }
    });
    }
    public void onActivityResult  (int requestCode, int resultCode, Intent data){
            Intent intent = new Intent();
        intent.putExtra("webResponse",data.getExtras().getString("webResponse"));
        setResult(RESULT_OK, intent);
        finish();
            
    }
    
    
}