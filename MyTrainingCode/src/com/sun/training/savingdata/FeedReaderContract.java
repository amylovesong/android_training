package com.sun.training.savingdata;

import android.provider.BaseColumns;

public class FeedReaderContract {

	public FeedReaderContract() {
	}

	public static abstract class FeedEntry implements BaseColumns {
		public static final String TABLE_NAME = "entry";
		public static final String COLUMN_NAME_ENTRY_ID = "entryid";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_SUBTITLE = "subtitle";
		public static final String COLUMN_NAME_CONTENT = "content";
		public static final String COLUMN_NAME_UPDATED = "updated";
		public static final String COLUMN_NAME_NULLABLE = "null";
	}

}
