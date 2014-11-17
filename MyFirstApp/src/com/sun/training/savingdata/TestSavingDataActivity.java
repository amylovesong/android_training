package com.sun.training.savingdata;

import com.sun.training.R;

import android.app.Activity;
import android.os.Bundle;

public class TestSavingDataActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saving_data);

		SavingData.sp(getApplicationContext(), this);
	}
}
