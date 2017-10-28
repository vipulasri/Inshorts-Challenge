package com.github.vipul.inshortschallenge.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.vipul.inshortschallenge.model.News;
import com.github.vipul.inshortschallenge.source.NewsDataSource;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.QueryObservable;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class NewsLocalDataSource implements NewsDataSource {

    private NewsDbHelper mDbHelper;

    private final BriteDatabase mDatabaseHelper;
    private Function<Cursor, News> mTaskMapperFunction;

    @Inject
    public NewsLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new NewsDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(mDbHelper, Schedulers.io());
        mDatabaseHelper.setLoggingEnabled(true);
        mTaskMapperFunction = this::getNews;
    }

    @NonNull
    private News getNews(@NonNull Cursor c) {
        News news = new News();
        news.setId(c.getString(c.getColumnIndexOrThrow(NewsPersistenceContract.NewsEntry.COLUMN_ID)));
        news.setTitle(c.getString(c.getColumnIndexOrThrow(NewsPersistenceContract.NewsEntry.COLUMN_TITLE)));
        news.setCategory(c.getString(c.getColumnIndexOrThrow(NewsPersistenceContract.NewsEntry.COLUMN_CATEGORY)));
        news.setPublisher(c.getString(c.getColumnIndexOrThrow(NewsPersistenceContract.NewsEntry.COLUMN_PUBLISHER)));
        news.setHostname(c.getString(c.getColumnIndexOrThrow(NewsPersistenceContract.NewsEntry.COLUMN_HOSTNAME)));
        news.setTimestamp(Long.valueOf(c.getString(c.getColumnIndexOrThrow(NewsPersistenceContract.NewsEntry.COLUMN_TIMESTAMP))));
        news.setUrl(c.getString(c.getColumnIndexOrThrow(NewsPersistenceContract.NewsEntry.COLUMN_URL)));
        return news;
    }

    @Override
    public Single<List<News>> getNews() {
        String[] projection = {
                NewsPersistenceContract.NewsEntry.COLUMN_ID,
                NewsPersistenceContract.NewsEntry.COLUMN_TITLE,
                NewsPersistenceContract.NewsEntry.COLUMN_CATEGORY,
                NewsPersistenceContract.NewsEntry.COLUMN_PUBLISHER,
                NewsPersistenceContract.NewsEntry.COLUMN_HOSTNAME,
                NewsPersistenceContract.NewsEntry.COLUMN_TIMESTAMP,
                NewsPersistenceContract.NewsEntry.COLUMN_URL
        };

        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), NewsPersistenceContract.NewsEntry.TABLE_NAME);
        return mDatabaseHelper.createQuery(NewsPersistenceContract.NewsEntry.TABLE_NAME, sql)
                .mapToList(mTaskMapperFunction)
                .singleOrError();
    }

    @Override
    public void saveNews(@NonNull News news) {
        checkNotNull(news);
        ContentValues values = new ContentValues();
        values.put(NewsPersistenceContract.NewsEntry.COLUMN_ID, news.getId());
        values.put(NewsPersistenceContract.NewsEntry.COLUMN_TITLE, news.getTitle());
        values.put(NewsPersistenceContract.NewsEntry.COLUMN_CATEGORY, news.getCategory());
        values.put(NewsPersistenceContract.NewsEntry.COLUMN_PUBLISHER, news.getPublisher());
        values.put(NewsPersistenceContract.NewsEntry.COLUMN_HOSTNAME, news.getHostname());
        values.put(NewsPersistenceContract.NewsEntry.COLUMN_TIMESTAMP, news.getTimestamp());
        values.put(NewsPersistenceContract.NewsEntry.COLUMN_URL, news.getUrl());
        mDatabaseHelper.insert(NewsPersistenceContract.NewsEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void refreshNews() {

    }

    @Override
    public void deleteAllNews() {
        mDatabaseHelper.delete(NewsPersistenceContract.NewsEntry.TABLE_NAME, null);
    }
}
