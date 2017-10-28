package com.github.vipul.inshortschallenge.source;

import android.support.annotation.NonNull;

import com.github.vipul.inshortschallenge.model.News;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface NewsDataSource {

    Single<List<News>> getNews();

    void saveNews(@NonNull News news);

    void refreshNews();

    void deleteAllNews();
}
