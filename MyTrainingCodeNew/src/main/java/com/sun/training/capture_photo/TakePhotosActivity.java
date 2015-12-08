package com.sun.training.capture_photo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.sun.training.R;

public class TakePhotosActivity extends Activity implements OnClickListener {
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_VIDEO_CAPTURE = 2;
	final String txtCapturePhoto = "Capture photo";
	final String txtScanAndDisplay = "Scan and Display";
	final String txtRecordVideo = "Record video";
	final String txtPlay = "Play";
	
	ImageView mImageView;
	Button btn;
	VideoView mVideoView;
	
	String mCurrentPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);

		btn = (Button) findViewById(R.id.btn_common);
		btn.setText(txtCapturePhoto);
		mImageView = (ImageView) findViewById(R.id.img_common);
		btn.setOnClickListener(this);

		// Record videos
		btn.setText(txtRecordVideo);
		addVideoView();
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// --Get the thumbnail
			// startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

			// --Create the file where the photo should go
			File photoFile=null;
			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "sun_JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);
		// Save a file:path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();

		return image;
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		mediaScanIntent.setData(Uri.fromFile(f));
		this.sendBroadcast(mediaScanIntent);
	}

	private void setPic() {
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		mImageView.setImageBitmap(bitmap);
	}

	private void dispatchTakeVideoIntent() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
		}
	}

	private void addVideoView() {
		mVideoView = new VideoView(getApplicationContext());
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		((ViewGroup) findViewById(R.id.layout_common)).addView(mVideoView,
				params2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			if (data != null) {
				// --Get the thumbnail
				Bundle extras = data.getExtras();
				Bitmap imageBitmap = (Bitmap) extras.get("data");
				mImageView.setImageBitmap(imageBitmap);
			} else {// --When set output Uri in Intent, there is no Intent
					// result
				btn.setText(txtScanAndDisplay);
			}
		}

		if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
			if (data != null) {
				Uri videoUri = data.getData();
				mVideoView.setVideoURI(videoUri);
				mVideoView.start();
				btn.setText(txtPlay);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof Button) {
			String txt = ((Button) v).getText().toString();
			if (txt.equals(txtCapturePhoto)) {
				dispatchTakePictureIntent();
			} else if (txt.equals(txtScanAndDisplay)) {
				// --Add the photo to a gallery
				galleryAddPic();
				// --Decode a scaled image
				setPic();
			} else if (txt.equals(txtRecordVideo)) {
				dispatchTakeVideoIntent();
			} else if (txt.equals(txtPlay)) {
				if (!mVideoView.isPlaying()) {
					mVideoView.start();
				}
			}
		}
	}
}
