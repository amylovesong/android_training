package com.sun.training.display_bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DisplayBitmapUtils {
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int resWidth, int resHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > resHeight || width > resWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > resHeight
					&& (halfWidth / inSampleSize) > resWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int resWidth, int resHeight) {
		// check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, resWidth,
				resHeight);
		Log.d("sun", "inSampleSize=" + options.inSampleSize);

		// decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
}
