package com.sun.training.display_bitmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.sun.training.R;

public class ImageGridFragment extends Fragment implements OnItemClickListener {
	private final String TAG = ImageGridFragment.class.getSimpleName();

	private ImageAadapter mAadapter;

	public static final Integer[] imageResIds = new Integer[] {
			R.drawable.img_zoro, R.drawable.img_flower,
			R.drawable.ic_action_search, R.drawable.img_flower,
			R.drawable.img_zoro, R.drawable.ic_action_search };

	public ImageGridFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAadapter = new ImageAadapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		final View v = inflater.inflate(R.layout.image_grid_fragment,
				container, false);
		final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
		mGridView.setAdapter(mAadapter);
		mGridView.setOnItemClickListener(this);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAadapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
		i.putExtra(ImageDetailActivity.EXTRA_IMAGE, position);
		startActivity(i);
	}

	private class ImageAadapter extends BaseAdapter {
		private final Context mContext;
		final Bitmap mPlaceHolderBitmap = BitmapFactory.decodeResource(
				getResources(), R.drawable.img_placeholder);

		public ImageAadapter(Context context) {
			super();
			mContext = context;
			Log.d(TAG, "ImageAadapter mContext=" + mContext);
		}

		@Override
		public int getCount() {
			Log.d(TAG, "getCount=" + imageResIds.length);
			return imageResIds.length;
		}

		@Override
		public Object getItem(int position) {
			Log.d(TAG, "getItem position=" + position
					+ " imageResIds[position]=" + imageResIds[position]);
			return imageResIds[position];
		}

		@Override
		public long getItemId(int position) {
			Log.d(TAG, "getItemId position=" + position);
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d(TAG, "getView position=" + position);
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				imageView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			} else {
				imageView = (ImageView) convertView;
			}
			loadBitmap(imageResIds[position], imageView);

			return imageView;
		}

		public void loadBitmap(int resId, ImageView imageView) {
			if (BitmapWorkerTask.cancelPotentialWork(resId, imageView)) {
				final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
				final AsyncDrawable asyncDrawable = new AsyncDrawable(
						getResources(), mPlaceHolderBitmap, task);
				imageView.setImageDrawable(asyncDrawable);
				task.execute(resId);
			}
		}
	}
}
