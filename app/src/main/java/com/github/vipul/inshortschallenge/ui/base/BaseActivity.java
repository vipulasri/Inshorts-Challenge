package com.github.vipul.inshortschallenge.ui.base;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.vipul.inshortschallenge.R;
import com.github.vipul.inshortschallenge.utils.NetworkUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Vipul on 16/09/17.
 */

public class BaseActivity extends DaggerAppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        injectViews();

        //Displaying the back button in the action bar
        if (isDisplayHomeAsUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    protected void injectViews() {
        ButterKnife.bind(this);
        setupToolbar();
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setActivityTitle(int title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    public void setActivityTitle(String title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onActionBarHomeIconClicked();
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //If back button is displayed in action bar, return false
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    public void setDisplayHomeAsUpEnabled(boolean value) {
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(value);
    }

    public void setHomeAsUpIndicator(Drawable drawable) {
        if(getSupportActionBar()!=null)
            getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    //Method for when home button is clicked
    public void onActionBarHomeIconClicked() {
        if (isDisplayHomeAsUpEnabled()) {
            onBackPressed();
        } else {
            finish();
        }
    }

    public boolean isInternetAvailable() {
        return NetworkUtils.isNetworkConnected(this);
    }

    public Snackbar getSnackBar(View view, String value) {
        return getSnackBar(view, value, Snackbar.LENGTH_LONG);
    }

    public Snackbar getSnackBar(View view, String value, int length) {
        return Snackbar.make(view, value, length);
    }

    public void showSnackBar(View view, String value) {
        getSnackBar(view, value).show();
    }

    public void showSnackBar(View view, int value) {
        getSnackBar(view, getString(value)).show();
    }

    public void showError(Throwable throwable) {
        if(throwable instanceof SocketTimeoutException)
            showSocketTimeoutError();

        else if(throwable instanceof IOException)
            showInternetError();
    }

    public void showInternetError(){
        showSnackBar(findViewById(android.R.id.content), R.string.no_internet);
    }

    public void showSocketTimeoutError(){
        showSnackBar(findViewById(android.R.id.content), R.string.connection_timeout);
    }
}