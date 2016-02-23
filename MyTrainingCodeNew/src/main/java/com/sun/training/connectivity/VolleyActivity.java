package com.sun.training.connectivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.sun.training.R;

import org.json.JSONObject;

public class VolleyActivity extends Activity {
    public static final String TAG_LOG = VolleyActivity.class.getSimpleName();
    public static final String TAG_REQUEST = "MyTag";

    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    ImageLoader mImageLoader;

    TextView textView;
    ImageView imageView;
    NetworkImageView mNetworkImageView;
    TextView textJSON;

    String textUrl = "http://www.google.com";
    String imageUrl = "http://img3.100bt.com/upload/ttq/20130414/1365913496030.jpg";
    String imageUrl2 = "http://img3.100bt.com/upload/ttq/20130414/1365913555700.jpg";
    String jsonUrl = "http://test.tv.api.3g.youku.com/tv/pay/show/price/info?showid=cc15e98a962411de83b1&pid=69b81504767483cf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.networkImageView);
        textJSON = (TextView) findViewById(R.id.text_json);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        request();
//        loadImage();
        loadImageByImageLoader();
//        requestJSON();
        requestGson();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(TAG_REQUEST);
        }
    }

    private void request(){
//        mRequestQueue = Volley.newRequestQueue(this);

//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);//1MB cap
//        Network network = new BasicNetwork(new HurlStack());
//        mRequestQueue = new RequestQueue(cache, network);
//        mRequestQueue.start();

        mRequestQueue = MySingleton.getInstance(getApplicationContext()).getReqeustQueue();


        stringRequest = new StringRequest(Request.Method.GET, textUrl,
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
        stringRequest.setTag(TAG_REQUEST);
//        mRequestQueue.add(stringRequest);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void loadImage(){
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG_LOG, "loadImage onErrorResponse " + error.getMessage());
            }
        });
        imageRequest.setTag(TAG_REQUEST);

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    private void loadImageByImageLoader(){
        mImageLoader = MySingleton.getInstance(getApplicationContext()).getImageLoader();
        mImageLoader.get(imageUrl, ImageLoader.getImageListener(imageView, R.drawable.img_placeholder,
                R.drawable.img_placeholder));

        mNetworkImageView.setImageUrl(imageUrl2, mImageLoader);
    }

    private void requestJSON(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                jsonUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textJSON.setText("Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG_LOG, "requestJSON onErrorResponse: " + error);
            }
        });
        jsonObjectRequest.setTag(TAG_REQUEST);

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void requestGson(){
        GsonRequest<PriceInfo> gsonRequest = new GsonRequest<>(jsonUrl, PriceInfo.class,
                null, new Response.Listener<PriceInfo>() {
            @Override
            public void onResponse(PriceInfo response) {
                textJSON.setText("PriceInfo:\n" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG_LOG, "requestGson onErrorResponse: " + error);
            }
        });
        gsonRequest.setTag(TAG_REQUEST);

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
    }

    public static class PriceInfo{
        public String status;
        public Result results;

        public class Result{
            public String pk_odshow;
            public int try_type;
            public int pay_type;
            public int paid;
            public String try_time;
        }

        @Override
        public String toString() {
            return "status: " + status + "\ntry_type: " + results.try_type
                    + "\npay_type: " + results.pay_type;
        }
    }

}
