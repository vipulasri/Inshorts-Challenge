package com.github.vipul.inshortschallenge.source.local;

import android.provider.BaseColumns;

public final class NewsPersistenceContract {

    private NewsPersistenceContract() {}

    public static abstract class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PUBLISHER = "publisher";
        public static final String COLUMN_HOSTNAME = "hostname";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_URL = "url";
    }
}
