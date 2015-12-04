package com.sun.training.connectivity;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.sun.training.R;

public class VolleyActivity extends Activity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        textView = (TextView) findViewById(R.id.text);
    }

    private void request(){
    }

}
