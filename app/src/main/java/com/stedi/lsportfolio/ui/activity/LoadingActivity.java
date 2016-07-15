package com.stedi.lsportfolio.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.Utils;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.api.Server;
import com.stedi.lsportfolio.model.LsAllApps;

// TODO ic_launcher
// TODO build.gradle
// TODO splash pending activity start
// TODO correct toolbar shadow
// TODO no internet toast
public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);

        if (savedInstanceState == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ResponseLsAllApps response = Server.requestLsAllApps();
                        LsAllApps.getInstance().setApps(response.getApps());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LoadingActivity.this, DrawerActivity.class));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showToast("server fail");
                    }
                }
            }).start();
        }
    }
}
