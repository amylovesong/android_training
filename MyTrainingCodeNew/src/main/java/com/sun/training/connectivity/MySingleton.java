package com.sun.training.connectivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by sunxiaoling on 2016/1/11.
 */
public class MySingleton {
    private static MySingleton mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private MySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getReqeustQueue();

//        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
//            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
//
//            @Override
//            public Bitmap getBitmap(String url) {
//                return cache.get(url);
//            }
//
//            @Override
//            public void putBitmap(String url, Bitmap bitmap) {
//                cache.put(url, bitmap);
//            }
//        });
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
                LruBitmapCache.getCacheSize(mCtx)));
    }

//    public static MySingleton getInstance(Context context) {
//        if (mInstance == null) {
//            synchronized (MySingleton.class) {
//                if (mInstance == null) {
//                    mInstance = new MySingleton(context);
//                }
//            }
//        }
//        return mInstance;
//    }

    public static MySingleton getInstance(Context context) {
        mCtx = context;
        return SingletonHolder.instance;
    }

    private static class SingletonHolder{
        private static MySingleton instance = new MySingleton(mCtx);
    }

    public RequestQueue getReqeustQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getReqeustQueue().add(req);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
}
