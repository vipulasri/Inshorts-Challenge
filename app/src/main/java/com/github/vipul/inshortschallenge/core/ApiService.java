package com.github.vipul.inshortschallenge.core;
import com.github.vipul.inshortschallenge.model.News;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {

    @GET("newsjson")
    Single<List<News>> news();
}
