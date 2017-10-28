package com.github.vipul.inshortschallenge.di;

import android.app.Application;

import com.github.vipul.inshortschallenge.InshortsApplication;
import com.github.vipul.inshortschallenge.di.network.NetworkModule;
import com.github.vipul.inshortschallenge.source.NewsRepository;
import com.github.vipul.inshortschallenge.source.NewsRepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {NetworkModule.class,
        NewsRepositoryModule.class,
        ApplicationModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})

public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(InshortsApplication application);

    NewsRepository getNewsRepository();

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();

        Builder networkModule(NetworkModule networkModule);
    }
}
