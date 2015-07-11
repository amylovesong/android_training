package com.sun.training.animation;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.sun.training.R;

public class LayoutChangesActivity extends Activity {
	private ViewGroup mContainerView;
	private String[] data = { "aaa", "bbbb", "ccc", "dddd" };
	private static int count;

	private boolean canRemove;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_changes);
		mContainerView = (ViewGroup) findViewById(R.id.container);
		mContainerView.setLayoutTransition(new LayoutTransition());

		findViewById(R.id.btn_add).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (count >= data.length) {
					canRemove = true;
				}
				if (count == 0) {
					canRemove = false;
				}
				if (canRemove) {
					removeItem(0);
//					goneItem(data.length - count);
					count--;
				} else {
					addItem(data[count++]);
				}
			}
		});
	}

	private void addItem(String t) {
		TextView newView = new TextView(this);
		newView.setText(t);
		newView.setGravity(Gravity.CENTER);
		newView.setBackgroundResource(android.R.color.darker_gray);
		LayoutParams lp = new LayoutParams(300, 100);
		lp.bottomMargin = 10;
		mContainerView.addView(newView, lp);
	}

	private void removeItem(int index) {
		mContainerView.removeViewAt(index);
	}
	
	private void goneItem(int index){		
		mContainerView.getChildAt(index).setVisibility(View.GONE);
	}
}
