package com.sun.training.display_bitmap;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.LruCache;

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
			@Override
			protected void entryRemoved(boolean evicted, String key,
					BitmapDrawable oldValue, BitmapDrawable newValue) {
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};
	}

	public boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
}
