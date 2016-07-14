package com.stedi.lsportfolio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stedi.lsportfolio.R;

// TODO ic_launcher
// TODO build.gradle
// TODO splash pending activity start
// TODO correct toolbar shadow
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
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(LoadingActivity.this, DrawerActivity.class));
                        }
                    });
                }
            }).start();
        }
    }
}
