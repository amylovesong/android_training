package com.sun.training.service;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sunxiaoling on 2015/11/11.
 */
public class HelloIntentService extends IntentService{

    /**
     * A constructor is required, and must call super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public HelloIntentService(){
        super("HelloIntentService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        long endTime = System.currentTimeMillis() + 5*1000;
        while(System.currentTimeMillis() < endTime){
            synchronized (this){
                try {
                    wait(endTime - System.currentTimeMillis());
                }catch (Exception e){

                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        // here must return the default implementation(which is how the intent gets delivered to onHandleIntent()).
        return super.onStartCommand(intent, flags, startId);
    }
}
