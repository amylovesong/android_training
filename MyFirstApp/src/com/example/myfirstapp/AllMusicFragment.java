package com.example.myfirstapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AllMusicFragment extends Fragment {
	private View mContentView;
	private TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContentView = inflater.inflate(R.layout.fragment_all_music, container,
				false);
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mTextView = (TextView) mContentView.findViewById(R.id.txv_all_music);
		if (mTextView != null) {
			mTextView.setText(AllMusicFragment.class.getSimpleName());
			mTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(),
							DetailActivity.class));
				}
			});
		}
	}
}
