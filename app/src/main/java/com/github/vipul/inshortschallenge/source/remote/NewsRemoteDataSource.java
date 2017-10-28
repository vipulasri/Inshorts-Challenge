package com.github.vipul.inshortschallenge.source.remote;

import android.support.annotation.NonNull;

import com.github.vipul.inshortschallenge.core.ApiService;
import com.github.vipul.inshortschallenge.model.News;
import com.github.vipul.inshortschallenge.source.NewsDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class NewsRemoteDataSource implements NewsDataSource {

    private ApiService mApiService;

    @Inject
    public NewsRemoteDataSource(ApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<List<News>> getNews() {
        return mApiService.news();
    }

    @Override
    public void saveNews(@NonNull News news) {

    }

    @Override
    public void refreshNews() {

    }

    @Override
    public void deleteAllNews() {

    }

}
