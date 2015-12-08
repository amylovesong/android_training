package com.sun.training.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sun.training.R;

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
				android.R.integer.config_longAnimTime
		// config_shortAnimTime
				);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_crossfade, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.animation:
			crossfade();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void crossfade() {
		final View vFadeIn, vFadeOut;
		if (mContentView.getVisibility() == View.GONE) {
			vFadeIn = mContentView;
			vFadeOut = mLoadingView;
		} else {
			vFadeIn = mLoadingView;
			vFadeOut = mContentView;
		}

		// the view that is fading in
		vFadeIn.setAlpha(0);// completely transparent
		vFadeIn.setVisibility(View.VISIBLE);

		vFadeIn.animate().alpha(1f).setDuration(mShortAnimationDuration)
				.setListener(null);// clear listener

		// the view that is fading out
		vFadeOut.animate().alpha(0).setDuration(mShortAnimationDuration)
				.setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animator animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						// after the animation ends, set the view's visibility
						// to GONE as an optimization step (it won't participate
						// in layout passes, etc.)
						vFadeOut.setVisibility(View.GONE);
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						// TODO Auto-generated method stub

					}
				});
	}

}
