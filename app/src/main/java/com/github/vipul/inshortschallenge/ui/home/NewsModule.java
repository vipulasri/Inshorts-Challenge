package com.github.vipul.inshortschallenge.ui.home;

import com.github.vipul.inshortschallenge.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class NewsModule {

    @ActivityScoped
    @Binds abstract NewsContract.Presenter newsPresenter(NewsPresenter presenter);
}
