package com.sun.training.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.sun.training.R;
import com.sun.training.fragment.HeadlinesFragment.OnHeadlineSelectedListener;

public class NewsArticlesActivity extends Activity implements
		OnHeadlineSelectedListener {
	private static final String TAG = NewsArticlesActivity.class
			.getSimpleName();

	private int[] datas = { 1, 2, 3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_articles);

		// Check that the activity is using the layout version with the
		// fragment_container FrameLayout
		if (findViewById(R.id.fragment_container) != null) {

			// However, if we're being restored from a previous state, then we
			// don't need to do anything and should return or else we could end
			// up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create a new Fragment to be placed in the activity layout
			HeadlinesFragment firstFragment = new HeadlinesFragment();

			// In case this activity was started with special instructions from
			// an Intent, pass the Intent's extras to the fragment as arguments
			firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the "fragment_container" FrameLayout
			getFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
	}

	@Override
	public void onArticleSelected(int position) {
		Log.d(TAG, "onArticleSelected position=" + position);

		// The user selected the headline of an article from the
		// HeadlinesFragment
		// Do something here to display that article
		ArticleFragment articleFrag = (ArticleFragment) getFragmentManager()
				.findFragmentById(R.id.article_fragment);
		if (articleFrag != null) {
			// If article frag is available, we're in two-pane layout...
			// Call a method in the ArticleFragment to update its content
			articleFrag.updateArticleView(position);
		} else {
			// Otherwise, we're in the one-pane layout and must swap frags...
			swapHeadlinesWithArticleFrag(position);
		}
	}

	private void swapHeadlinesWithArticleFrag(int position) {
		// Create fragment and give it an argument specifying the article it
		// should show
		ArticleFragment newFragment = new ArticleFragment();
		Bundle args = new Bundle();
		args.putInt(ArticleFragment.ARG_POSITION, position);
		newFragment.setArguments(args);

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment, and add the transaction to the back stack so the user can
		// navigate back
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}

	public int[] getDatas() {
		return datas;
	}
}
