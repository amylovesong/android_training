package com.sun.training.actionbar;

import com.sun.training.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DetailActivity extends Activity {
	private Button btnSwitch;
	private ActionBar mActionBar;
	private Button btnStartActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_detail_all_music);

		mActionBar = getActionBar();

		btnSwitch = (Button) findViewById(R.id.btn_switch);
		btnSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mActionBar != null) {
					if (mActionBar.isShowing()) {
						mActionBar.hide();
						btnSwitch
								.setText(R.string.button_text_show_the_action_bar);
					} else {
						mActionBar.show();
						btnSwitch
								.setText(R.string.button_text_hide_the_action_bar);
					}
				}
			}
		});
		btnStartActivity = (Button) findViewById(R.id.btn_go_overlay);
		btnStartActivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(DetailActivity.this,
						OverlayActionBarActivity.class));
			}
		});
	}
}
