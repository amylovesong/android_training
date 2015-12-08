package com.sun.training.display_bitmap;

import com.sun.training.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageDetailFragment extends Fragment {
	private static final String TAG = ImageDetailFragment.class.getSimpleName();

	private static final String IMAGE_DATA_EXTRA = "resId";
	private int mImageNum;
	private ImageView mImageView;

	static ImageDetailFragment newInstance(int imageNum) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putInt(IMAGE_DATA_EXTRA, imageNum);
		f.setArguments(args);
		return f;
	}

	public ImageDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageNum = getArguments() != null ? getArguments().getInt(
				IMAGE_DATA_EXTRA) : -1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		mImageView = (ImageView) v.findViewById(R.id.imageView);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (ImageDetailActivity.class.isInstance(getActivity())) {
			Log.d(TAG, "onActivityCreated mImageNum=" + mImageNum);
			final int resId = ImageDetailActivity.imageResIds[mImageNum];
			Log.d(TAG, "onActivityCreated resId=" + resId);
			// call out to ImageDetailActivity to load the bitmap in a
			// background thread.
			((ImageDetailActivity) getActivity()).loadBitmap(resId, mImageView);
		}
	}
}
