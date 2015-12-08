package com.sun.training.nfc;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

public class ViewActivity extends Activity {

	private static final String TAG = ViewActivity.class.getSimpleName();

	private File mParentPath;

	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		handleViewIntent();
	}

	private void handleViewIntent() {
		mIntent = getIntent();
		String action = mIntent.getAction();

		if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
			Uri beamUri = mIntent.getData();
			Log.d(TAG, "handleViewIntent beamUri:" + beamUri);
			// test for the type of URI, by getting its scheme value
			if (TextUtils.equals(beamUri.getScheme(), "file")) {
				mParentPath = handleFileUri(beamUri);
				Log.d(TAG, "handleViewIntent mParentPath:" + mParentPath);
			} else if (TextUtils.equals(beamUri.getScheme(), "content")) {
				mParentPath = handleContentUri(beamUri);
			}
		}
	}

	private File handleFileUri(Uri beamUri) {
		// get the path part of the URI
		String fileName = beamUri.getPath();
		Log.d(TAG, "handleFileUri path:" + fileName);
		// create a File object for this filename
		File copiedFile = new File(fileName);
		// get a string containing the file's parent directory
		return copiedFile.getParentFile();
	}

	private File handleContentUri(Uri beamUri) {
		int filenameIndex;
		File copiedFile;
		String fileName;

		// test the authority of the URI
		if (!TextUtils.equals(beamUri.getAuthority(), MediaStore.AUTHORITY)) {
			// handle content URIs for other content providers

			return null;
		} else {// for a MediaStore content URI
			String[] projection = { MediaStore.MediaColumns.DATA };
			Cursor pathCursor = getContentResolver().query(beamUri, projection,
					null, null, null);
			// check for a valid cursor
			if (pathCursor != null && pathCursor.moveToFirst()) {
				filenameIndex = pathCursor
						.getColumnIndex(MediaStore.MediaColumns.DATA);
				fileName=pathCursor.getString(filenameIndex);
				Log.d(TAG, "handleContentUri fileName=" + fileName);
				copiedFile=new File(fileName);
				return new File(copiedFile.getParent());
			} else {
				return null;
			}
		}
	}

}
