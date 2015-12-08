package com.sun.training.savingdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.training.savingdata.FeedReaderContract.FeedEntry;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class SavingData {
	private static final String TAG = SavingData.class.getSimpleName();

	/* --use SharedPreferences to save key-value sets-- */
	private static String preference_file_key = "com.sun.training.PREFERENCE_FILE_KEY";

	public static void sp(Context context, Activity activity) {
		// get a handle to a SharedPreferences
		// identify the file name
		SharedPreferences
		// sharedPref = context.getSharedPreferences(
		// preference_file_key, Context.MODE_PRIVATE);

		 // if we need just one shared preference file for activity
		sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

		// write to Shared Preferences
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("key", 1);
		editor.commit();

		// read from Shared Preferences
		long value = sharedPref.getInt("key", -1);
		Log.d(TAG, "sp getInt value=" + value);
	}

	/* --save files-- */
	private static String filename = "myfile";

	// Internal storage
	public static void saveFilesOnInternalStorage(Context context) {
		try {
			String fileContent = "amylovesong\n";
			FileOutputStream outputStream;
			// // --create File instance to get a FileOutputStream
			// File file;
			// // ----1.getFilesDir
			// file = new File(context.getFilesDir(), filename);
			// // ----2.getCacheDir(cache files)
			// file = File.createTempFile(filename, null,
			// context.getCacheDir());
			// outputStream = new FileOutputStream(file, true);

			// --openFileOutput to get a FileOutputStream
			outputStream = context.openFileOutput(filename,
					Context.MODE_PRIVATE);
			outputStream.write(fileContent.getBytes());
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// External storage
	public static void saveFilesOnExternalStorage(Context context) {
		if (isExternalStorageWritable()) {
			String albumName = "syz";
			File file;
			if (context != null) {
				file = getAlbumStorageDir(context, albumName);
			} else {
				file = getAlbumStorageDir(albumName);
			}
			querySpace(file);
		} else {
			Log.e(TAG, "External storage is not writable!");
		}
	}

	public static void saveFilesOnExternalStorage() {
		saveFilesOnExternalStorage(null);
	}

	private static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	private static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	// Public directory
	public static File getAlbumStorageDir(String albumName) {
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				albumName);
		if (!file.mkdirs()) {
			Log.e(TAG, "Directory not created");
		}
		return file;
	}

	// Private directory
	public static File getAlbumStorageDir(Context context, String albumName) {
		File file = new File(
				context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
				albumName);
		if (!file.mkdirs()) {
			Log.e(TAG, "Directory not created");
		}
		return file;
	}

	public static void querySpace(File file) {
		long totalSpace = file.getTotalSpace();
		long freeSpace = file.getFreeSpace();
		Log.d(TAG, "totalSpace: " + totalSpace / (1024 * 1024) + "MB"
				+ " freeSpace: " + freeSpace / (1024 * 1024) + "MB");
	}

	public static void deleteFile(File file) {
		file.delete();
	}

	// If the file is saved on internal storage, you can also ask the
	// Context to locate and delete a file by calling deleteFile():
	public static void deleteFile(Context context, String fileName) {
		context.deleteFile(fileName);
	}

	/* saving data in SQL database */
	public static void saveDataIntoDB(Context context) {
		FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
		// Gets the data repository in write mode
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, "id");
		values.put(FeedEntry.COLUMN_NAME_TITLE, "title");
		values.put(FeedEntry.COLUMN_NAME_CONTENT, "content");

		// insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(FeedEntry.TABLE_NAME,
				FeedEntry.COLUMN_NAME_NULLABLE, values);
	}

	public static void readDataFromDB(Context context) {
		FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { FeedEntry._ID, FeedEntry.COLUMN_NAME_TITLE,
				FeedEntry.COLUMN_NAME_UPDATED };

		// how you want the results sorted in the resulting Cursor
		String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";
		Cursor cursor = db.query(FeedEntry.TABLE_NAME, projection, "", null,
				null, null, sortOrder);
		cursor.moveToFirst();
		long itemId = cursor.getLong(cursor
				.getColumnIndexOrThrow(FeedEntry._ID));
	}

	public static void deleteDataFromDB(Context context) {
		int rowId = 2;
		SQLiteDatabase db = new FeedReaderDbHelper(context)
				.getWritableDatabase();
		// define 'where' part of query
		String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		// Specify arguments in placeholder order
		String[] selectionArgs = { String.valueOf(rowId) };
		// issue SQL statement
		db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
	}

	public static void modifyDataOfDB(Context context) {
		int rowId = 2;
		SQLiteDatabase db = new FeedReaderDbHelper(context)
				.getWritableDatabase();
		// new value for one column
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_TITLE, "titleModified");
		// which row to update, based on the ID
		String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(rowId) };

		int count = db.update(FeedEntry.TABLE_NAME, values, selection,
				selectionArgs);
	}
}
