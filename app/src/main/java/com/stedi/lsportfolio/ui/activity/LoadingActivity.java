package com.stedi.lsportfolio.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.Utils;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.api.Server;
import com.stedi.lsportfolio.model.LsAllApps;

public class LoadingActivity extends AppCompatActivity implements Runnable {
    private static Thread loadingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        if (LsAllApps.getInstance().getApps() != null) {
            startActivity(new Intent(this, DrawerActivity.class));
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
                    LsAllApps.getInstance().setApps(response.getApps());
                    startActivity(new Intent(LoadingActivity.this, DrawerActivity.class));
                    loadingThread = null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToast("server fail");
        }
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
}
