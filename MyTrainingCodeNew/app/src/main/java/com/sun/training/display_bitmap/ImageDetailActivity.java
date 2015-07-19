package com.sun.training.display_bitmap;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.sun.training.R;

public class ImageDetailActivity extends FragmentActivity {
	public static final String EXTRA_IMAGE = "extra_image";

	private ImagePagerAdapter mAdapter;
	private ViewPager mPager;

	public static final Integer[] imageResIds = new Integer[] {
			R.drawable.img_zoro, R.drawable.img_flower,
			R.drawable.ic_action_search, R.drawable.img_flower,
			R.drawable.img_zoro, R.drawable.ic_action_search,
			R.drawable.img_zoro, R.drawable.img_flower,
			R.drawable.ic_action_search, R.drawable.img_flower,
			R.drawable.img_zoro, R.drawable.ic_action_search };

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.image_detail_pager);

		Intent intent = getIntent();
		if (intent != null && intent.hasExtra(EXTRA_IMAGE)) {
			mPager = (ViewPager) findViewById(R.id.pager);
			mAdapter = new ImagePagerAdapter(getSupportFragmentManager(),
					imageResIds.length);
			mPager.setAdapter(mAdapter);
			mPager.setCurrentItem(intent.getIntExtra(EXTRA_IMAGE, 0));
		} else {
			ImageGridFragment fragment = new ImageGridFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, fragment).commit();
		}
	}

	public void loadBitmap(int resId, ImageView imageView) {
		imageView.setImageResource(R.drawable.img_placeholder);
		BitmapWorkerTask task = new BitmapWorkerTask(imageView);
		task.execute(resId);
	}

	public static class ImagePagerAdapter extends FragmentStatePagerAdapter {
		private final int mSize;

		public ImagePagerAdapter(FragmentManager fm, int size) {
			super(fm);
			mSize = size;
		}

		@Override
		public Fragment getItem(int position) {
			return ImageDetailFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return mSize;
		}
	}

	public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		public int data = 0;

		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			data = params[0];
			return DisplayBitmapUtils.decodeSampledBitmapFromResource(
					imageViewReference.get().getContext().getResources(), data,
					100, 100);
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
}
