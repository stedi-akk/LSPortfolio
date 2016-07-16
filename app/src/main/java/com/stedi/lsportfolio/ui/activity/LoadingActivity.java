package com.stedi.lsportfolio.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Subscribe;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.api.Server;
import com.stedi.lsportfolio.model.LsAllApps;
import com.stedi.lsportfolio.other.NoNetworkException;
import com.stedi.lsportfolio.other.Utils;

public class LoadingActivity extends AppCompatActivity implements Runnable {
    private static Thread loadingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        if (LsAllApps.getInstance().getApps() != null) {
            startDrawerActivity();
        } else if (savedInstanceState == null && loadingThread == null) {
            loadingThread = new Thread(this);
            loadingThread.start();
        }
    }

    @Override
    public void run() {
        try {
            final ResponseLsAllApps response = Server.requestLsAllApps();
            App.postOnResume(new Runnable() {
                @Override
                public void run() {
                    App.getBus().post(response);
                    loadingThread = null;
                }
            });
        } catch (final Exception ex) {
            App.postOnResume(new Runnable() {
                @Override
                public void run() {
                    App.getBus().post(ex);
                    loadingThread = null;
                }
            });
        }
    }

    @Subscribe
    public void onResponse(ResponseLsAllApps response) {
        LsAllApps.getInstance().setApps(response.getApps());
        startDrawerActivity();
    }

    @Subscribe
    public void onException(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof NoNetworkException)
            Utils.showToast(R.string.no_internet);
        else
            Utils.showToast(R.string.unknown_error);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.getBus().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.getBus().unregister(this);
    }

    private void startDrawerActivity() {
        finish();
        startActivity(new Intent(this, DrawerActivity.class));
    }
}
