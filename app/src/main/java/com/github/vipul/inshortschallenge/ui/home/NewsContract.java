package com.github.vipul.inshortschallenge.ui.home;

import android.support.annotation.NonNull;

import com.github.vipul.inshortschallenge.model.News;
import com.github.vipul.inshortschallenge.ui.base.BasePresenter;
import com.github.vipul.inshortschallenge.ui.base.BaseView;

import java.util.List;

public interface NewsContract {

    interface View extends BaseView<Presenter> {

        void showNews(@NonNull List<News> news);

        void showNoData();
    }

    interface Presenter extends BasePresenter<View> {

        void loadNews(boolean forceUpdate);

        void takeView(NewsContract.View view);

        void dropView();
    }
}
