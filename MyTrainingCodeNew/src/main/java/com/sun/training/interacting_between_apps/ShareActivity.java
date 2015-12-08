package com.sun.training.interacting_between_apps;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun.training.R;

public class ShareActivity extends Activity {
	private static final String TAG = ShareActivity.class.getSimpleName();
	private ImageView imgShare;
	private TextView txvShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		imgShare = (ImageView) findViewById(R.id.img_share);
		txvShare = (TextView) findViewById(R.id.txv_share);

		Intent intent = getIntent();
		Log.d(TAG, "intent:" + intent);

		if (intent.getType().indexOf("image/") != -1) {
			// Handle intents with image data
			handleSendImage(intent);
		} else if (intent.getType().equals("text/plain")) {
			// Handle intents with text
			handleSendText(intent);
		}
	}

	private void handleSendImage(Intent intent) {
		Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
		Log.d(TAG, "imageUri:" + imageUri);
		try {
			InputStream is = getContentResolver().openInputStream(imageUri);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			imgShare.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		txvShare.setText(sharedText);
	}

	private void returnResult() {
		Intent result = new Intent("com.sun.RESULT_ACTION",
				Uri.parse("content://result_uri"));
		setResult(RESULT_OK, result);
		finish();
	}
}
