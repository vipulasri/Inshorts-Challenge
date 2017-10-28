package com.github.vipul.inshortschallenge.ui.home;

import android.support.annotation.NonNull;

import com.github.vipul.inshortschallenge.di.ActivityScoped;
import com.github.vipul.inshortschallenge.model.News;
import com.github.vipul.inshortschallenge.source.NewsRepository;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

@ActivityScoped
final class NewsPresenter implements NewsContract.Presenter {

    private final NewsRepository mNewsRepository;

    @Nullable
    private NewsContract.View mView;

    private boolean mFirstLoad = true;

    @NonNull
    private CompositeDisposable mDisposables;

    @Inject
    NewsPresenter(NewsRepository tasksRepository) {
        mNewsRepository = tasksRepository;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void loadNews(boolean forceUpdate) {
        loadNews(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadNews(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            if (mView != null) {
                mView.showLoading(true);
            }
        }
        if (forceUpdate) {
            mNewsRepository.refreshNews();
        }

        mDisposables.clear();
        Disposable disposable = mNewsRepository
                .getNews()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribeWith(new DisposableObserver<List<News>>() {
                    @Override
                    public void onNext(List<News> news) {
                        processNews(news);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoading(false);
                        mView.showError(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.showLoading(false);
                    }
                });

        mDisposables.add(disposable);
    }

    private void processNews(List<News> news) {
        if (news.isEmpty()) {
            if (mView != null) {
                mView.showNoData();
            }
        } else {
            if (mView != null) {
                mView.showNews(news);
            }
        }
    }


    @Override
    public void takeView(NewsContract.View view) {
        mView = view;
        this.loadNews(false);
    }

    @Override
    public void dropView() {
        mView = null;
        mDisposables.clear();
    }
}
