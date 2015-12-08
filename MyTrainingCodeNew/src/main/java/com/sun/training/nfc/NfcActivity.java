package com.sun.training.nfc;

import java.io.File;

import com.sun.training.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

/**
 * NFC: Near Field Communication
 * 
 * @author sxl
 *
 */
public class NfcActivity extends Activity {

	private static final String TAG = NfcActivity.class.getSimpleName();

	NfcAdapter mNfcAdapter;
	// Flag to indicate that Android Beam is available
	boolean mAndroidBeamAvailable = false;
	// List of URIs to provide to Android Beam
	private Uri[] mFileUris = new Uri[10];

	private FileUriCallback mFileUriCallback;

	private TextView txvCheckResult;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		findView();

		String checkResult;
		// NFC isn't available on the device
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
			checkResult = "NFC isn't available on the device";
			Log.e(TAG, checkResult);
		} // Android Beam file transfer isn't supported
		else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			mAndroidBeamAvailable = false;
			checkResult = "Android Beam file transfer isn't supported";
			Log.e(TAG, checkResult);
		} else {// Android Beam file transfer is available, continue
			checkResult = "Android Beam file transfer is available";
			Log.d(TAG, checkResult);
			mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
			// mFileUriCallback = new FileUriCallback();
			// mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback, this);
			mNfcAdapter.setBeamPushUris(createFileUris(), this);
		}

		// show check result
		if (txvCheckResult != null) {
			txvCheckResult.setText(checkResult);
		}
	}

	private void findView() {
		txvCheckResult = (TextView) findViewById(R.id.txv_check_result);
	}

	/**
	 * Callback that Android Beam file transfer calls to get files to share
	 */
	@SuppressLint("NewApi")
	private class FileUriCallback implements CreateBeamUrisCallback {
		public FileUriCallback() {
			// TODO Auto-generated constructor stub
		}

		/**
		 * Create content URIs as needed to share with another device
		 */
		@Override
		public Uri[] createBeamUris(NfcEvent event) {
			Log.d(TAG, "createBeamUris event:" + event);
			return createFileUris();
		}

	}

	private Uri[] createFileUris() {
		Uri[] mFileUris = new Uri[1];
		String transferFile = "nfc_transfer_file.jpg";
		File file = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		Log.d(TAG, "Environment " + file);
		File extDir = getExternalFilesDir(null);
		File requestFile = new File(extDir, transferFile);
		requestFile.setReadable(true, false);
		Uri fileUri = Uri.fromFile(requestFile);
		if (fileUri != null) {
			Log.d(TAG, "fileUri " + fileUri);
			mFileUris[0] = fileUri;
		} else {
			Log.e(TAG, "No File URI available for file.");
		}

		return mFileUris;
	}

}
