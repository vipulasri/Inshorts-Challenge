package com.github.vipul.inshortschallenge.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.vipul.inshortschallenge.model.News;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class NewsRepository implements NewsDataSource {

    private final NewsDataSource mNewsRemoteDataSource;
    private final NewsDataSource mNewsLocalDataSource;

    Map<String, News> mCachedNews;

    boolean mCacheIsDirty = false;

    @Inject
    NewsRepository(@Remote NewsDataSource newsRemoteDataSource, @Local NewsDataSource newsLocalDataSource) {
        mNewsRemoteDataSource = checkNotNull(newsRemoteDataSource);
        mNewsLocalDataSource = checkNotNull(newsLocalDataSource);
    }


    @Override
    public Single<List<News>> getNews() {
        // Respond immediately with cache if available and not dirty
        if (mCachedNews != null && mCachedNews.size()>0 && !isCacheDirty()) {
            return Observable.fromIterable(mCachedNews.values()).toList();
        } else if (mCachedNews == null || mCachedNews.size()<0) {
            mCachedNews = new LinkedHashMap<>();
        }

        Single<List<News>> remoteTasks = getAndSaveRemoteNews();

        if (isCacheDirty()) {
            return remoteTasks;
        } else {
            Single<List<News>> localTasks = getAndSaveLocalNews();
            return Single.concat(localTasks, remoteTasks)
                    .filter(tasks -> !tasks.isEmpty())
                    .firstOrError();
        }

    }

    @Override
    public void saveNews(@NonNull News news) {

    }

    private Single<List<News>> getAndSaveRemoteNews() {
        Log.d("NewsRepository", "->getAndSaveRemoteNews");
        return mNewsRemoteDataSource
                .getNews()
                .flatMapObservable(Observable::fromIterable)
                .doOnNext(news -> {
                    mCachedNews.put(news.getId(), news);
                }).toList()
                .doOnSuccess(news -> {
                    saveNewsToLocalDataSource(news); mCacheIsDirty = false;
                })
                .doOnError((__) -> getAndSaveLocalNews());
    }

    public Single<List<News>> getAndSaveLocalNews() {
        Log.d("NewsRepository", "->getAndSaveLocalNews");
        return mNewsLocalDataSource
                .getNews()
                .flatMapObservable(Observable::fromIterable)
                .doOnNext(news -> {
                    mCachedNews.put(news.getId(), news);
                })
                .toList();
    }

    private void saveNewsToLocalDataSource(@NonNull List<News> news) {
        Log.d("NewsRepository", "->saveNewsToLocalDataSource"+news.size());
        mNewsLocalDataSource.deleteAllNews();
        for (News news1 : news) {
            mNewsLocalDataSource.saveNews(news1);
        }
    }

    @Override
    public void refreshNews() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllNews() {
        mNewsRemoteDataSource.deleteAllNews();
        mNewsLocalDataSource.deleteAllNews();

        if (mCachedNews == null) {
            mCachedNews = new LinkedHashMap<>();
        }
        mCachedNews.clear();
    }

    public boolean isCacheDirty(){
        return mCacheIsDirty;
    }
}
