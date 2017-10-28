package com.github.vipul.inshortschallenge.di.network;

import android.support.annotation.NonNull;

import com.github.vipul.inshortschallenge.BuildConfig;
import com.github.vipul.inshortschallenge.core.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private String baseUrl;

    public NetworkModule(@NonNull String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG)
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return logging;

    }

    @Provides
    @InterceptorInfo
    int provideRetryCount() {
        return 3;
    }

    @Singleton
    @Provides
    Interceptor provideRetryInterceptor(@InterceptorInfo int retryCount) {
        return chain -> {
            Request request = chain.request();
            Response response = null;
            IOException exception = null;

            int tryCount = 0;
            while (tryCount < retryCount && (null == response || !response.isSuccessful())) {
                try {
                    response = chain.proceed(request);
                } catch (IOException e) {
                    exception = e;
                } finally {
                    tryCount++;
                }
            }

            if (null == response && null != exception) {
                throw exception;
            }

            return response;
        };
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor, Interceptor retryInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .addInterceptor(retryInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, GsonConverterFactory gsonConverterFactory, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

}
