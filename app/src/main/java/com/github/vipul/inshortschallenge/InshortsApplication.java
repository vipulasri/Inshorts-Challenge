package com.github.vipul.inshortschallenge;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.vipul.inshortschallenge.di.AppComponent;
import com.github.vipul.inshortschallenge.di.DaggerAppComponent;
import com.github.vipul.inshortschallenge.di.network.NetworkModule;
import com.github.vipul.inshortschallenge.source.NewsRepository;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import retrofit2.Retrofit;

/**
 * Created by Vipul on 16/09/17.
 */

public class InshortsApplication extends DaggerApplication {

    public static final String TAG = InshortsApplication.class.getSimpleName();

    private static InshortsApplication _instance;

    @Inject NewsRepository mNewsRepository;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .application(this)
                .networkModule(new NetworkModule(BuildConfig.API_BASE_URL))
                .build();
        appComponent.inject(this);
        return appComponent;
    }

    public static InshortsApplication getInstance() {
        return _instance;
    }
}
