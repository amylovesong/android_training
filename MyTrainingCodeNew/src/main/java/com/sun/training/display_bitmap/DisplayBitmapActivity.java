package com.sun.training.display_bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.sun.training.R;

public class DisplayBitmapActivity extends Activity {
	private Bitmap mPlaceHolderBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);

		ImageView imageView = (ImageView) findViewById(R.id.img_common);
		
		readDimensionsAndType();

		// -----Loading Large Bitmaps Efficiently-----
		// imageView.setImageBitmap(DisplayBitmapUtils
		// .decodeSampledBitmapFromResource(getResources(),
		// R.drawable.img_zoro, 100, 100));

		// -----Processing Bitmaps Off the UI Thread-----
		mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		loadBitmap(R.drawable.img_flower, imageView);
	}

	private void readDimensionsAndType() {
		// Read dimensions and type of the bitmap data prior to construction(and
		// memory allocation) of the bitmap.
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher,
				options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		String imageType = options.outMimeType;
		Log.d("sun", "height="+imageHeight+" width="+imageWidth+" type="+imageType);
	}

	private void loadBitmap(int resId, ImageView imageView) {
		if (BitmapWorkerTask.cancelPotentialWork(resId, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					getResources(), mPlaceHolderBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(resId);
		}
	}
}
