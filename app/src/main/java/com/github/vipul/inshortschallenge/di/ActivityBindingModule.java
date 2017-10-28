package com.github.vipul.inshortschallenge.di;

import com.github.vipul.inshortschallenge.ui.NewsDetailsActivity;
import com.github.vipul.inshortschallenge.ui.home.HomeActivity;
import com.github.vipul.inshortschallenge.ui.home.NewsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = NewsModule.class)
    abstract HomeActivity homeActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract NewsDetailsActivity newsDetailsActivity();

}
