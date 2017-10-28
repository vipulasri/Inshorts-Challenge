package com.github.vipul.inshortschallenge.ui.base;

public interface BaseView<T> {

    void showLoading(boolean active);

    void showError(Throwable throwable);

}
