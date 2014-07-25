package com.sun.training.actionbar;

import com.sun.training.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	public final static String EXTRA_MESSAGE = "com.sun.trainingMESSAGE";

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		/*
		 * setContentView(R.layout.activity_main);
		 * 
		 * // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 * 
		 * // If your minSdkVersion is 11 or higher, instead use:
		 * getActionBar().setDisplayHomeAsUpEnabled(true);
		 */

		/* Tab */
		// Notice that setContentView() is not used, because we use the root
		// android.R.id.content as the container for each fragment

		// setup action bar for tabs
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Action Bar");
		// 注掉后无法显示出Tab
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// 是否显示title
		// actionBar.setDisplayShowTitleEnabled(false);
		// Up button 是否可用
		actionBar.setDisplayHomeAsUpEnabled(true);

		Tab tab = actionBar
				.newTab()
				.setText(R.string.tab_text_all)
				.setTabListener(
						new TabListener<AllMusicFragment>(this, "all",
								AllMusicFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.tab_text_artist)
				.setTabListener(
						new TabListener<ArtistFragment>(this, "artist",
								ArtistFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.tab_text_album)
				.setTabListener(
						new TabListener<AlbumFragment>(this, "album",
								AlbumFragment.class));
		actionBar.addTab(tab);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			openSearch();
			return true;

		case R.id.action_settings:
			openSettings();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		showToast(R.string.action_settings);

	}

	private void openSearch() {
		showToast(R.string.aciton_search);
	}

	private void showToast(int resId) {
		Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT)
				.show();
	}

	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
		// Do something in response to button
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Log.d(TAG, "onTabSelected tab=" + tab.getText() + " mFragment="
					+ mFragment);
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If no, initialize and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			Log.d(TAG, "onTabUnselected tab=" + tab.getText() + " mFragment="
					+ mFragment);
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}

		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}
}
