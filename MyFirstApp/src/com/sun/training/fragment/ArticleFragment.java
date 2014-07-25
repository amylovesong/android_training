package com.sun.training.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sun.training.R;

public class ArticleFragment extends Fragment {
	private View viewContainer;

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
	}
}
