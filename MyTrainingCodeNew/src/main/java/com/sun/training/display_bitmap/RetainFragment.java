package com.sun.training.display_bitmap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;

public class RetainFragment extends Fragment {
	private static final String TAG = RetainFragment.class.getSimpleName();
	public LruCache<String, Bitmap> mRetainedCache;

	public RetainFragment() {
	}

	public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
		RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(TAG);
		if (fragment == null) {
			fragment = new RetainFragment();
			fm.beginTransaction().add(fragment, TAG).commit();
		}

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

}
