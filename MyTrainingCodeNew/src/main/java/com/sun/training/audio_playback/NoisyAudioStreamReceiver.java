package com.sun.training.audio_playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class NoisyAudioStreamReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())){
			//pause the playback
			Log.d("sun", "NoisyAudioStreamReceiver ACTION_AUDIO_BECOMING_NOISY");
		}
	}

}
