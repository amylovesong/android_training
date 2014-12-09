package com.sun.training.audio_playback;

import com.sun.training.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

public class ManageAudioPlaybackActivity extends Activity {

	AudioManager am;
	ComponentName componentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multimedia);

		//control volume
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		//control playback
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		componentName = new ComponentName(getApplicationContext(),
				RemoteControlReceiver.class.getName());
	}

	@Override
	protected void onResume() {
		super.onResume();
		am.registerMediaButtonEventReceiver(componentName);
	}

	@Override
	protected void onStop() {
		super.onStop();
		am.unregisterMediaButtonEventReceiver(componentName);
	}
}
