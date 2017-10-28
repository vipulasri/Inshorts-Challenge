package com.github.vipul.inshortschallenge.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.vipul.inshortschallenge.R;
import com.github.vipul.inshortschallenge.model.News;
import com.github.vipul.inshortschallenge.model.Sort;
import com.github.vipul.inshortschallenge.ui.NewsDetailsActivity;
import com.github.vipul.inshortschallenge.ui.base.BaseActivity;
import com.github.vipul.inshortschallenge.utils.DateComparator;
import com.github.vipul.inshortschallenge.utils.LocalStoreUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements NewsContract.View, NewsAdapter.Callbacks {

    public static final String TAG_SORT = "TAG_SORT";

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.view_no_content)
    View mNoContent;

    @BindView(R.id.newsRecyclerView)
    RecyclerView mNewsRecyclerView;

    @Inject
    NewsContract.Presenter mPresenter;

    private List<News> mNews = new ArrayList<>();
    private NewsAdapter mNewsAdapter;
    private Sort mSort = Sort.DATE_ASC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNewsAdapter = new NewsAdapter(mNews);
        mNewsAdapter.setCallbacks(this);
        mNewsRecyclerView.setAdapter(mNewsAdapter);

        int spanCount = 2;
        mNewsRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadNews(true);
            }
        });

        showData(false);
        mPresenter.loadNews(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mSort) {
            case DATE_ASC:
                menu.findItem(R.id.sort_by_date_asc).setChecked(true);
                break;
            case DATE_DESC:
                menu.findItem(R.id.sort_by_date_desc).setChecked(true);
                break;
        }
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_date_asc:
                item.setChecked(!item.isChecked());
                onSortChanged(Sort.DATE_ASC);
                break;

            case R.id.sort_by_date_desc:
                item.setChecked(!item.isChecked());
                onSortChanged(Sort.DATE_DESC);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSortChanged(Sort sort) {
        mSort = sort;
        showNews(new ArrayList<>(mNews));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        if(mSort!=null)
            savedInstanceState.putSerializable(TAG_SORT, mSort);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TAG_SORT)) {
                mSort = (Sort) savedInstanceState.getSerializable(TAG_SORT);
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void showLoading(boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showNews(@NonNull List<News> news) {
        Collections.sort(news, new DateComparator());

        if(mSort == Sort.DATE_DESC)
            Collections.reverse(news);

        mNews.clear();
        mNews.addAll(news);
        mNewsAdapter.notifyDataSetChanged();
        showData(true);

        List<String> categories = new ArrayList<>();

        for (News news1 : mNews) {
            if(!categories.contains(news1.getCategory()))
               categories.add(news1.getCategory());
        }

        Log.e("categories", "->"+categories.toString());
    }

    @Override
    public void showNoData() {
        showData(false);
    }

    private void showData(boolean value){
        int recyclerViewVisibility = value ? View.VISIBLE : View.GONE;
        mNewsRecyclerView.setVisibility(recyclerViewVisibility);

        int noContentVisibility = value ? View.GONE : View.VISIBLE;
        mNoContent.setVisibility(noContentVisibility);
    }

    @Override
    public void onNewsClick(@NonNull News news) {
        if(!news.getUrl().isEmpty()) {
            NewsDetailsActivity.launchActivity(this, news);
        }
    }

    @Override
    public void onNewsBookmarkClick(@NonNull String newsId, boolean value) {
        if (value)
            LocalStoreUtils.addToBookmarks(this, newsId);
        else
            LocalStoreUtils.removeFromBookmarks(this, newsId);
    }
}
