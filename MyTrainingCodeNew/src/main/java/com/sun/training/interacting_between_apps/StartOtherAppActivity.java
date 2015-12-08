package com.sun.training.interacting_between_apps;

import java.util.Calendar;
import java.util.List;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sun.training.R;

public class StartOtherAppActivity extends Activity implements OnClickListener {
	private Button btnTel;
	private Button btnViewMap;
	private Button btnViewWebpage;
	private Button btnEmail;
	private Button btnCalendarEvent;
	private Button btnGetResult;

	private String phoneNumber = "13571889521";
	private String locationInfo = "0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_user_to_another_app);

		findView();
		setListener();
	}

	private void setListener() {
		btnTel.setOnClickListener(this);
		btnViewMap.setOnClickListener(this);
		btnViewWebpage.setOnClickListener(this);
		btnEmail.setOnClickListener(this);
		btnCalendarEvent.setOnClickListener(this);
		btnGetResult.setOnClickListener(this);
	}

	private void findView() {
		btnTel = (Button) findViewById(R.id.btn_tel);
		btnTel.setText("Tel:" + phoneNumber);
		btnViewMap = (Button) findViewById(R.id.btn_view_map);
		btnViewWebpage = (Button) findViewById(R.id.btn_view_webpage);
		btnEmail = (Button) findViewById(R.id.btn_email);
		btnCalendarEvent = (Button) findViewById(R.id.btn_calendar_event);
		btnGetResult = (Button) findViewById(R.id.btn_get_result);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		if (v == btnTel) {
			intent = createCallIntent();
		} else if (v == btnViewMap) {
			intent = createViewMapIntent();
		} else if (v == btnViewWebpage) {
			intent = createViewWebpageIntent();
		} else if (v == btnEmail) {
			intent = createEmailIntent();
		} else if (v == btnCalendarEvent) {
			intent = createCalendarIntent();
		} else if (v == btnGetResult) {
			intent = createGetResultIntent();
		}
		if (intent != null && isIntentSafe(intent)) {
			startActivity(intent);
		}
	}

	/**
	 * Verify there is an app to receive the intent
	 * 
	 * @param intent
	 * @return
	 */
	private boolean isIntentSafe(Intent intent) {
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				intent, 0);
		return activities.size() > 0;
	}

	private Intent createCallIntent() {
		Uri number = Uri.parse("tel:" + phoneNumber);
		Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
		return callIntent;
	}

	private Intent createViewMapIntent() {
		// Map point based on address
		Uri location = Uri.parse("geo:" + locationInfo);
		// or Map point based on latitude and longitude
		// locationInfo = "37.422219,-122.08364?z=14";// z param is zoom level
		// Uri location = Uri.parse("geo:" + locationInfo);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
		return mapIntent;
	}

	private Intent createViewWebpageIntent() {
		Uri webpage = Uri.parse("http://www.baidu.com");
		Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

		// the supplied text as the dialog title
		// Intent chooser = Intent.createChooser(webIntent,
		// "View webpage with");

		return webIntent;
		// return chooser;
	}

	// --Use putExtra() to add extra data and use setType() to specify the type
	// of data.--
	private Intent createEmailIntent() {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		// The intent does not have a URI, so declare the "text/plain" MIME type
		emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sunxiaoling@youku.com"});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
		// we can also attach multiple items by passing an ArrayList of Uris
		emailIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse("content://path/to/email/attachment"));

		return emailIntent;
	}

	private Intent createCalendarIntent() {
		Intent calendarIntent = new Intent(Intent.ACTION_INSERT,
				Events.CONTENT_URI);
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2014, 10, 22, 14, 30);
		Calendar endTime = Calendar.getInstance();
		endTime.set(2014, 10, 22, 16, 00);
		calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				beginTime.getTimeInMillis());
		calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
				endTime.getTimeInMillis());
		calendarIntent.putExtra(Events.TITLE, "shopping");
		calendarIntent.putExtra(Events.EVENT_LOCATION, "supermarket");

		return calendarIntent;
	}

	private Intent createGetResultIntent() {
		Intent getResultIntent=new Intent(getApplicationContext(), GetResultActivity.class);
		return getResultIntent;
	}
}
