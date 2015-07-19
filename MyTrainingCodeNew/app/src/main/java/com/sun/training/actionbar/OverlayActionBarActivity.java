package com.sun.training.actionbar;

import com.sun.training.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OverlayActionBarActivity extends Activity {
	private TextView txvInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_overlay_actionbar);

		txvInfo = (TextView) findViewById(R.id.txv_info);
		txvInfo.setText(OverlayActionBarActivity.class.getSimpleName());
		txvInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActionBar().isShowing()) {
					getActionBar().hide();
				} else {
					getActionBar().show();
				}
			}
		});
	}
}
