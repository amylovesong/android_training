package com.sun.training.actionbar;

import com.sun.training.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AlbumFragment extends Fragment {
	private View mContentView;
	private TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContentView = inflater.inflate(R.layout.fragment_album, container,
				false);
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mTextView = (TextView) mContentView.findViewById(R.id.txv_album);
		if (mTextView != null) {
			mTextView.setText(AlbumFragment.class.getSimpleName());
		}
	}

}
