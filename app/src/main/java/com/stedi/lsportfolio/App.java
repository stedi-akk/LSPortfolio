package com.stedi.lsportfolio;

import android.app.Application;
import android.content.Context;

import com.stedi.lsportfolio.di.components.DaggerInjector;
import com.stedi.lsportfolio.di.components.Injector;

public final class App extends Application {
    private static App instance;

    private Injector injector;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        injector = DaggerInjector.create();
    }

    public static Injector getInjector() {
        return instance.injector;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
