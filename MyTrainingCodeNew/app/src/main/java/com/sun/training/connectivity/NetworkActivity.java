package com.sun.training.connectivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by sunxiaoling on 15/8/27.
 */
public class NetworkActivity extends Activity {

    private static final String ANY = "Any";
    private static final String WIFI = "Wi-Fi";
    private static final String URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

    public static String sPref = null;

    public static boolean refreshDisplay = true;
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    private NetworkReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        sPref = sharedPrefs.getString("listPref", "Wi-Fi");

        updateConnectedFlags();

        if (refreshDisplay) {
            loadPage();
        }

    }

    public void updateConnectedFlags() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    private void loadPage() {
        if ((sPref.equals(ANY) && (wifiConnected || mobileConnected))
                || (sPref.equals(WIFI) && wifiConnected)) {
            new DownloadXmlTask().execute(URL);
        }else {
            showErrorPage();
        }
    }

    private void showErrorPage() {

    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    private class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if (WIFI.equals(sPref) && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                refreshDisplay = true;
                Toast.makeText(context, "wifi connected", Toast.LENGTH_SHORT).show();
            }else if (ANY.equals(sPref) && networkInfo !=null){
                refreshDisplay = true;

            }else{
                refreshDisplay=false;
                Toast.makeText(context, "lost connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
