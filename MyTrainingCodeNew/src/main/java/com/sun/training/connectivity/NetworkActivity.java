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
import android.webkit.WebView;
import android.widget.Toast;

import com.sun.training.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

        sPref = sharedPrefs.getString("listPref", WIFI);

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
        } else {
            showErrorPage();
        }
    }

    private void showErrorPage() {
        Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
    }

    // Uploads XML from stackoverflow.com, parse it, and combines it with
    // HTML markup. Return HTML string.
    private String loadXmlFromNetwork(String urlString) throws IOException, XmlPullParserException {
        InputStream stream = null;
        StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
        List<StackOverflowXmlParser.Entry> entries = null;

        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
//        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        boolean pref = false;//get from SharedPreferences

        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<h3>" + "page_title" + "</h3>");
        htmlString.append("<em>" + "updated " + formatter.format(
                rightNow.getTimeInMillis()) + "</em>");

        try {
            stream = downloadUrl(urlString);
            entries = stackOverflowXmlParser.parse(stream);
            // Make sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (StackOverflowXmlParser.Entry entry : entries) {
            htmlString.append("<p><a href='");
            htmlString.append(entry.link);
            htmlString.append("'>" + entry.title + "</a></p>");
            // If the user set the preference to include summary text,
            // add it to the display.
            if (pref) {
                htmlString.append(entry.summary);
            }
        }

        return htmlString.toString();
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        return conn.getInputStream();
    }

    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "connection error";
            } catch (XmlPullParserException e) {
                return "xml error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //set content view and display the HTML string in the UI via a WebView
            setContentView(R.layout.activity_network);
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadData(result, "text/html", null);
        }
    }

    private class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if (WIFI.equals(sPref) && (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                refreshDisplay = true;
                Toast.makeText(context, "wifi connected", Toast.LENGTH_SHORT).show();
            } else if (ANY.equals(sPref) && networkInfo != null) {
                refreshDisplay = true;

            } else {
                refreshDisplay = false;
                Toast.makeText(context, "lost connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
