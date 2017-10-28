package com.github.vipul.inshortschallenge.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "News.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsPersistenceContract.NewsEntry.TABLE_NAME + " (" +
                    NewsPersistenceContract.NewsEntry.COLUMN_ID + TEXT_TYPE + " PRIMARY KEY," +
                    NewsPersistenceContract.NewsEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_PUBLISHER + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_HOSTNAME + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_TIMESTAMP + TEXT_TYPE + COMMA_SEP +
                    NewsPersistenceContract.NewsEntry.COLUMN_URL + TEXT_TYPE +
            " )";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
