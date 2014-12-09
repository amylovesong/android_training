package com.sun.training.audio_playback;

import com.sun.training.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;

public class ManageAudioPlaybackActivity extends Activity {

	AudioManager am;
	ComponentName componentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multimedia);

		// control volume
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// control playback
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		componentName = new ComponentName(getApplicationContext(),
				RemoteControlReceiver.class.getName());

		// request audio focus
		int result = am.requestAudioFocus(afChangeListener,
				AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			am.registerMediaButtonEventReceiver(componentName);
			// Start playback.

		}
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

		// abandon audio focus when playback complete
		am.abandonAudioFocus(afChangeListener);
	}

	private OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {

		@Override
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
				// pause playback
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				// resume playback
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				// if the loss is permanent, unregisters our media button event
				// receiver and stops monitoring audio focus changes.
				am.unregisterMediaButtonEventReceiver(componentName);
				am.abandonAudioFocus(afChangeListener);
				// stop playback
			}
			
			//-----Duck!-----
			if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
				//lower the volume
				
			}else if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
				//raise it back to normal
				
			}
		}
	};
	
	//--Dealing with Audio Output Hardware--
	//-----Check What Hardware is Being Used-----
	private void checkWhatHardwareIsUsed(){
		if(am.isBluetoothA2dpOn()){
			//Adjust output for Bluetooth.
		}else if(am.isSpeakerphoneOn()){
			//Adjust output for Speakerphone.
		}else if(am.isWiredHeadsetOn()){
			//Adjust output for headsets.
		}else{
			//If audio plays and none can hear it, is it still playing?
		}
	}
	//-----Handle Changes in the Audio Output Hardware-----
	private NoisyAudioStreamReceiver myNoisyAudioStreamReceiver=new NoisyAudioStreamReceiver();
	private IntentFilter intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
	private void startPlayback(){
		registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
	}
	private void stopPlayback(){
		unregisterReceiver(myNoisyAudioStreamReceiver);
	}
}
