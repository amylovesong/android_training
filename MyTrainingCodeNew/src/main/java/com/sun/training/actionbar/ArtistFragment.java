package com.sun.training.actionbar;

import com.sun.training.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArtistFragment extends Fragment {

	private View mContentView;
	private TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContentView = inflater.inflate(R.layout.fragment_artist, container,
				false);
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mTextView = (TextView) mContentView.findViewById(R.id.txv_artist);
		if (mTextView != null) {
			mTextView.setText(ArtistFragment.class.getSimpleName());
		}

		if (mContentView != null) {
			mContentView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (getActivity().getActionBar().isShowing()) {
						getActivity().getActionBar().hide();
					} else {
						getActivity().getActionBar().show();
					}
				}
			});
		}
	}

}
