package com.sun.training.audio_playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class RemoteControlReceiver extends BroadcastReceiver {

	private static final String TAG = RemoteControlReceiver.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive action:" + intent.getAction());
		if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
			KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			Log.d(TAG, "onReceive keycode:" + event.getKeyCode());
			if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
				// Handle key press.
				Log.d(TAG, "Handle KEYCODE_MEDIA_PLAY");
			} else if (KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode()) {
				Log.d(TAG, "Handle KEYCODE_VOLUME_UP");
			}
		}

	}

}
