package com.sun.training.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sun.training.R;

public class HeadlinesFragment extends ListFragment {
	private static final String TAG = HeadlinesFragment.class.getSimpleName();

	private View viewContainer;

	OnHeadlineSelectedListener mCallback;

	// Container Activity must implement this interface
	public interface OnHeadlineSelectedListener {
		public void onArticleSelected(int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// To make sure that the container activity has implemented the callback
		// interface. If not, it throws an exception
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "onListItemClick");
		// Send the event to the host activity
		mCallback.onArticleSelected(position);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewContainer = inflater.inflate(R.layout.fragment_headlines,
				container, false);
		return viewContainer;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		TextView view = (TextView) viewContainer.findViewById(R.id.textView1);
		view.setText(HeadlinesFragment.class.getSimpleName());

		if (getActivity() != null
				&& getActivity() instanceof NewsArticlesActivity) {
			setListAdapter(new MyAdapter(
					((NewsArticlesActivity) getActivity()).getDatas()));
		}
	}

	private class MyAdapter extends BaseAdapter {
		private int[] datas;

		public MyAdapter(int[] datas) {
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(
						getActivity().getApplicationContext()).inflate(
						android.R.layout.simple_list_item_1, null);
			}
			TextView textView = (TextView) convertView
					.findViewById(android.R.id.text1);
			textView.setText("headline " + datas[position]);

			return convertView;
		}

	}
}
