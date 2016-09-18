package com.stedi.lsportfolio.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.api.Api;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.model.Model;
import com.stedi.lsportfolio.other.CachedUiRunnables;
import com.stedi.lsportfolio.other.ContextUtils;
import com.stedi.lsportfolio.other.NoNetworkException;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public class LoadingActivity extends BaseActivity {
    private static boolean isLoading; // in case of back button

    @Inject Bus bus;
    @Inject Api api;
    @Inject CachedUiRunnables cur;
    @Inject Model model;
    @Inject ContextUtils contextUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.loading_activity);
        if (model.getApps() != null) {
            startDrawerActivity();
        } else if (savedInstanceState == null && !isLoading) {
            isLoading = true;
            api.requestLsAllApps()
                    .subscribeOn(Schedulers.io())
                    .subscribe(responseLsAllApps -> cur.post(() -> bus.post(responseLsAllApps)),
                            throwable -> cur.post(() -> bus.post(throwable)));
        }
    }

    @Subscribe
    public void onResponse(ResponseLsAllApps response) {
        model.setApps(response.getApps());
        model.setJobs(response.getJobs());
        startDrawerActivity();
        isLoading = false;
    }

    @Subscribe
    public void onException(Exception ex) {
        ex.printStackTrace();
        contextUtils.showToast(ex instanceof NoNetworkException ? R.string.no_internet : R.string.unknown_error);
        finish();
        isLoading = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cur.postMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cur.cachingMode();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    private void startDrawerActivity() {
        finish();
        startActivity(new Intent(this, DrawerActivity.class));
    }
}
