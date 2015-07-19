package com.sun.training.display_bitmap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Editor;
import com.sun.training.R;

public class CacheBitmapActivity extends Activity implements OnClickListener {
	private static final String TAG = CacheBitmapActivity.class.getSimpleName();

	private LruCache<String, Bitmap> mMemoryCache;

	// -------Disk Cache-------
	private DiskLruCache mDiskLruCache;
	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;// 10MB
	private static final String DISK_CACHE_SUBDIR = "thumbnails";

	private ImageView imv;
	private Button btn;
	private final String txtCacheBitmap = "Cache Bitmap";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);

		imv = (ImageView) findViewById(R.id.img_common);
		btn = (Button) findViewById(R.id.btn_common);
		btn.setText(txtCacheBitmap);
		btn.setOnClickListener(this);

		// ---Initialize memory cache-----
		// get max available VM memory
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// use 1/8th of the available memory for this memory cache
		final int cacheSize = maxMemory / 8;

		// --use a fragment which is preserved by calling
		// setRetainInstance(true)
		RetainFragment retainFragment = RetainFragment
				.findOrCreateRetainFragment(getFragmentManager());
		mMemoryCache = retainFragment.mRetainedCache;
		if (mMemoryCache == null) {
			mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					// the cache size will be measured in kilobytes rather than
					// number of items
					return value.getByteCount() / 1024;
				}
			};
			retainFragment.mRetainedCache = mMemoryCache;
		} else {
			Log.d(TAG, "mMemoryCache is retained by RetainFragment");
		}

		// ------Initialize disk cache on background thread---------
		File cacheDir = getDiskCacheDir(this, DISK_CACHE_SUBDIR);
		new InitDiskCacheTask().execute(cacheDir);
	}

	private File getDiskCacheDir(Context context, String uniqueName) {
		// check if media is mounted or storage is built-in, if so, try and use
		// external cache dir otherwise use internal cache dir
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable() ? getExternalCacheDir()
				.getPath() : getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	@Override
	public void onClick(View v) {
		if (v instanceof Button) {
			String txt = ((Button) v).getText().toString();
			if (txt.equals(txtCacheBitmap)) {
				loadBitmap(R.drawable.img_flower, imv);
			}
		}

	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public void addBitmapToCache(String key, Bitmap bitmap) {
		// add to memory cache as before
		addBitmapToMemoryCache(key, bitmap);

		// alse add to disk cache
		synchronized (mDiskCacheLock) {
			try {
				if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
					Editor editor = mDiskLruCache.edit(key);
					OutputStream os = editor.newOutputStream(0);
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
					os.close();
					editor.commit();
					mDiskLruCache.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	public Bitmap getBitmapFromDiskCache(String key) {
		synchronized (mDiskCacheLock) {
			// wait while disk cache is started from background thread
			while (mDiskCacheStarting) {
				try {
					mDiskCacheLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (mDiskLruCache != null) {
				try {
					DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
					if (snapshot != null) {
						InputStream is = snapshot.getInputStream(0);
						return BitmapFactory.decodeStream(is);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void loadBitmap(int resId, ImageView imageView) {
		final String imageKey = String.valueOf(resId);

		// check LruCache first
		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		Log.d(TAG, "loadBitmap getBitmapFromMemCache:" + bitmap);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.img_placeholder);
			BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			task.execute(resId);
		}
	}

	public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;

		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			final String key = String.valueOf(params[0]);
			// check disk cache in background thread
			Bitmap bitmap = getBitmapFromDiskCache(key);
			Log.d(TAG, "doInBackground getBitmapFromDiskCache:" + bitmap);
			if (bitmap == null) {// not found in disk cache
				// process as normal
				bitmap = DisplayBitmapUtils.decodeSampledBitmapFromResource(
						imageViewReference.get().getContext().getResources(),
						params[0], 100, 100);
				Log.d(TAG, "doInBackground decodeSampledBitmapFromResource");
			}

			// // update code to add entries to the memory cache
			// addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);

			// add final bitmap to caches
			addBitmapToCache(key, bitmap);

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	class InitDiskCacheTask extends AsyncTask<File, Void, Void> {

		@Override
		protected Void doInBackground(File... params) {
			synchronized (mDiskCacheLock) {
				File cacheDir = params[0];
				try {
					mDiskLruCache = DiskLruCache.open(cacheDir, 10, 1,
							DISK_CACHE_SIZE);
					mDiskCacheStarting = false;
					mDiskCacheLock.notifyAll();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

	}
}
