package com.sun.training.savingdata;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sun.training.R;

public class TestSavingDataActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saving_data);

		Button btnSavingData = (Button) findViewById(R.id.btn_saving_data);
		btnSavingData.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// internal:shared_prefs
		SavingData.sp(getApplicationContext(), this);
		// internal: files, cache
		SavingData.saveFilesOnInternalStorage(getApplicationContext());
		// external public directory
		SavingData.saveFilesOnExternalStorage();
		// external private directory: sdcard/Android/data/pkgname/files
		SavingData.saveFilesOnExternalStorage(getApplicationContext());
		// delete file
		SavingData.deleteFile(new File(
				getExternalFilesDir(Environment.DIRECTORY_PICTURES), "syz"));
		// delete file that saved on internal storage
		SavingData.deleteFile(getApplicationContext(), "myfile");
	}
}
