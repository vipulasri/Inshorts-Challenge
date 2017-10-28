package com.github.vipul.inshortschallenge.source;

import android.content.Context;

import com.github.vipul.inshortschallenge.core.ApiService;
import com.github.vipul.inshortschallenge.source.local.NewsLocalDataSource;
import com.github.vipul.inshortschallenge.source.remote.NewsRemoteDataSource;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Vipul on 16/09/17.
 */

@Module
public class NewsRepositoryModule {

    @Provides
    @Singleton
    @Local
    NewsDataSource provideNewsLocalDataSource(Context context) {
        return new NewsLocalDataSource(context);
    }

    @Provides
    @Singleton
    @Remote
    NewsDataSource provideNewsRemoteDataSource(ApiService apiService) {
        return new NewsRemoteDataSource(apiService);
    }
}
