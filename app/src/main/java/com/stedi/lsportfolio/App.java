package com.stedi.lsportfolio;

import android.app.Application;
import android.content.Context;

public final class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
