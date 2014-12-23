package com.sun.training.display_bitmap;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.LruCache;

import com.example.android.displayingbitmaps.util.RecyclingBitmapDrawable;

public class ManageBitmapMemory {

	// -----Manage memory on Android 2.3.3 and lower
	private int mCacheRefCount = 0;
	private int mDisplayRefCount = 0;

	private boolean mHasBeenDisplayed;

	// notify the drawable that the display state has changed.
	// keep a count to determine when the drawable is no longer displayed.
	public void setIsDisplayed(boolean isDisplayed) {
		synchronized (this) {
			if (isDisplayed) {
				mDisplayRefCount++;
				mHasBeenDisplayed = true;
			} else {
				mDisplayRefCount--;
			}
		}

		// check to see if recycle() can be called.
		checkState();
	}

	// notify the drawable that the cache state has changed.
	// keep a count to determine when the drawable is no long cached.
	public void setIsCached(boolean isCached) {
		synchronized (this) {
			if (isCached) {
				mCacheRefCount++;
			} else {
				mCacheRefCount--;
			}
		}
		checkState();
	}

	private synchronized void checkState() {
		// if the drawable cache and display ref counts = 0, and this drawable
		// has been displayed, then recycle.
		if (mCacheRefCount <= 0 && mDisplayRefCount <= 0 && mHasBeenDisplayed
				&& hasValidBitmap()) {
			getBitmap().recycle();
		}
	}

	private Bitmap getBitmap() {
		return null;
	}

	private boolean hasValidBitmap() {
		Bitmap bitmap = getBitmap();
		return bitmap != null && !bitmap.isRecycled();
	}

	// -----Manage memory on Android 3.0 and higher-----
	class ImageCache {
		Set<SoftReference<Bitmap>> mReusableBitmaps;
		private LruCache<String, BitmapDrawable> mMemoryCache;

		// --save a bitmap for later use
		public void init() {
			// if we are running on Honeycomb or newer, create a synchronized
			// HashSet of references to reusable bitmaps.
			if (hasHoneycomb()) {
				mReusableBitmaps = Collections
						.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
			}

			mMemoryCache = new LruCache<String, BitmapDrawable>(1024 * 1024 * 4) {
				// notify the removed entry that is no longer being cached.
				@Override
				protected void entryRemoved(boolean evicted, String key,
						BitmapDrawable oldValue, BitmapDrawable newValue) {
					if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
						// the removed entry is a recycling drawable, so notify
						// it
						// that it has been removed from the memory cache.
						((RecyclingBitmapDrawable) oldValue).setIsCached(false);
					} else {
						// the removed entry is a standard BitmapDrawable
						if (hasHoneycomb()) {
							// we're running on Honeycomb or later, so add the
							// bitmap to a SoftReference set for possible use
							// with
							// inBitmap later
							mReusableBitmaps.add(new SoftReference<Bitmap>(
									oldValue.getBitmap()));
						}
					}
				}
			};
		}

		/**
		 * iterates through the reusable bitmaps, looking for one to use for
		 * inBitmap
		 * 
		 * @param options
		 * @return
		 */
		public Bitmap getBitmapFromReusableSet(Options options) {

		}
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static Bitmap decodeSampledBitmapFromFile(String fileName,
			int reqWidth, int reqHeight, ImageCache cache) {
		final BitmapFactory.Options options = new BitmapFactory.Options();

		BitmapFactory.decodeFile(fileName, options);

		// if we're running on Honeycomb or newer, try to use inBitmap
		if (hasHoneycomb()) {
			addInBitmapOptions(options, cache);
		}

		return BitmapFactory.decodeFile(fileName, options);
	}

	private static void addInBitmapOptions(Options options, ImageCache cache) {
		// inBitmap only works with mutable drawable, so force the decoder to
		// return mutable bitmaps.
		options.inMutable = true;

		if (cache != null) {
			// try to find a bitmap to use for inBitmap
			Bitmap inBitmap = cache.getBitmapFromReusableSet(options);
			// if a suitable bitmap has been found, set it as the value of
			// inBitmap.
			if (inBitmap != null) {
				options.inBitmap = inBitmap;
			}
		}
	}

}
