package com.stedi.lsportfolio.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.api.Api;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.model.LsAllApps;
import com.stedi.lsportfolio.other.NoNetworkException;
import com.stedi.lsportfolio.other.PendingUiRunnables;
import com.stedi.lsportfolio.other.Utils;

import javax.inject.Inject;

public class LoadingActivity extends AppCompatActivity implements Runnable {
    private static Thread loadingThread; // in case of back button

    @Inject Bus bus;
    @Inject Api api;
    @Inject PendingUiRunnables pur;
    @Inject LsAllApps allApps;
    @Inject Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInjector().inject(this);
        setContentView(R.layout.loading_activity);
        if (allApps.getApps() != null) {
            startDrawerActivity();
        } else if (savedInstanceState == null && loadingThread == null) {
            loadingThread = new Thread(this);
            loadingThread.start();
        }
    }

    @Override
    public void run() {
        try {
            final ResponseLsAllApps response = api.requestLsAllApps();
            pur.post(new Runnable() {
                @Override
                public void run() {
                    bus.post(response);
                    loadingThread = null;
                }
            });
        } catch (final Exception ex) {
            pur.post(new Runnable() {
                @Override
                public void run() {
                    bus.post(ex);
                    loadingThread = null;
                }
            });
        }
    }

    @Subscribe
    public void onResponse(ResponseLsAllApps response) {
        allApps.setApps(response.getApps());
        startDrawerActivity();
    }

    @Subscribe
    public void onException(Exception ex) {
        ex.printStackTrace();
        utils.showToast(ex instanceof NoNetworkException ? R.string.no_internet : R.string.unknown_error);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pur.postMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pur.cachingMode();
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
