package com.github.vipul.inshortschallenge.ui.base;

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();

}
