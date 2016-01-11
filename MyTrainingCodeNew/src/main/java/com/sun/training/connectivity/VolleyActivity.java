package com.sun.training.connectivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.util.LruCache;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sun.training.R;

public class VolleyActivity extends Activity {
    public static final String TAG = "MyTag";
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        textView = (TextView) findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(TAG);
        }
    }

    private void request(){
//        mRequestQueue = Volley.newRequestQueue(this);

//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);//1MB cap
//        Network network = new BasicNetwork(new HurlStack());
//        mRequestQueue = new RequestQueue(cache, network);
//        mRequestQueue.start();

        mRequestQueue = MySingleton.getInstance(getApplicationContext()).getReqeustQueue();

        String url = "http://www.google.com";

        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText("Response is:" + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });
        stringRequest.setTag(TAG);
//        mRequestQueue.add(stringRequest);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
