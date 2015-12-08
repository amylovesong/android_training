package com.sun.training.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sun.training.R;

public class ArticleFragment extends Fragment {

	public static final String ARG_POSITION = "arg_position";

	private static final String TAG = ArticleFragment.class.getSimpleName();

	private View viewContainer;
	private View articleView;

	public ArticleFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		viewContainer = inflater.inflate(R.layout.fragment_article, container,
				false);
		return viewContainer;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		TextView view = (TextView) viewContainer.findViewById(R.id.textView1);
		view.setText(ArticleFragment.class.getSimpleName());

		initArticleView();
	}

	private void initArticleView() {
		articleView = viewContainer.findViewById(R.id.article_view);
		Bundle bundle = getArguments();
		Log.d(TAG, "initArticleView bundle=" + bundle);
		if (bundle != null) {
			int position = bundle.getInt(ARG_POSITION, -1);
			updateArticleView(position);
		}
	}

	public void updateArticleView(int position) {
		if (articleView != null && articleView instanceof TextView
				&& getDatas() != null) {
			((TextView) articleView).setText("article " + getDatas()[position]);
		}
	}

	private int[] getDatas() {
		if (getActivity() != null
				&& getActivity() instanceof NewsArticlesActivity) {
			return ((NewsArticlesActivity) getActivity()).getDatas();
		}

		return null;
	}
}
