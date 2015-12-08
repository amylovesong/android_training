package com.sun.training.interacting_between_apps;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sun.training.R;

public class GetResultActivity extends Activity implements OnClickListener {
	static final int PICK_CONTACT_REQUEST = 1;// The request code
	private Button btnPickContact;
	private TextView txvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_user_to_another_app);

		findViewById(R.id.layout_first_page).setVisibility(View.GONE);
		findViewById(R.id.layout_second_page).setVisibility(View.VISIBLE);
		btnPickContact = (Button) findViewById(R.id.btn_pick_contact);
		btnPickContact.setOnClickListener(this);
		txvResult = (TextView) findViewById(R.id.txv_result);
	}

	@Override
	public void onClick(View v) {
		if (v == btnPickContact) {
			pickContact();
		}
	}

	private void pickContact() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
				Uri.parse("content://contacts"));
		// Show user only contacts w/ phone numbers
		pickContactIntent.setType(Phone.CONTENT_TYPE);
		// Check whether there is an App can resolve this Intent
		if (pickContactIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == PICK_CONTACT_REQUEST) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// Get the Uri that points to the selected contact
				Uri contactUri = data.getData();
				// We only need the NUMBER column, because there will be only
				// one row in the result
				String[] projection = { Phone.NUMBER };
				CursorLoader cursorLoader = new CursorLoader(
						getApplicationContext(), contactUri, projection, null,
						null, null);
				Cursor cursor = cursorLoader.loadInBackground();
				cursor.moveToFirst();

				// Retrieve the phone number from the NUMBER column
				int columnIndex = cursor.getColumnIndex(Phone.NUMBER);
				String number = cursor.getString(columnIndex);
				txvResult.setText("The picked contact: " + number);
			}
		}
	}
}
