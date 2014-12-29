package com.sun.training.animation;

import com.sun.training.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class CrossfadeActivity extends Activity {
	private View mContentView;
	private View mLoadingView;
	private int mShortAnimationDuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crossfade);

		mContentView = findViewById(R.id.content);
		mLoadingView = findViewById(R.id.loading_spinner);

		mContentView.setVisibility(View.GONE);

		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
	}

}
