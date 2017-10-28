package com.github.vipul.inshortschallenge.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.vipul.inshortschallenge.R;
import com.github.vipul.inshortschallenge.model.News;
import com.github.vipul.inshortschallenge.ui.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Vipul on 16/09/17.
 */

public class NewsDetailsActivity extends BaseActivity {

    public static final String EXTRA_NEWS = "EXTRA_NEWS";

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.webView)
    WebView mWebView;

    private News mNews;

    public static void launchActivity(@NonNull Activity startingActivity, @NonNull News news) {
        Intent intent = new Intent(startingActivity, NewsDetailsActivity.class);
        intent.putExtra(EXTRA_NEWS, news);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        mNews = getIntent().getParcelableExtra(EXTRA_NEWS);

        setActivityTitle(mNews.getTitle());
        setDisplayHomeAsUpEnabled(true);

        mProgressBar.setMax(100);

        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
        mWebView.getSettings().setAppCachePath("/data/data/"+ getPackageName() +"/cache");
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

        if (isInternetAvailable()) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }

        mWebView.loadUrl(mNews.getUrl());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setProgress(100);
            mProgressBar.setVisibility(View.GONE);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
        }
    }
    private class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            mProgressBar.setProgress(progress);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else
            finish();

        return super.onKeyDown(keyCode, event);
    }
}
