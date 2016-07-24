package com.stedi.lsportfolio;

import android.app.Application;

import com.stedi.lsportfolio.di.components.DaggerInjector;
import com.stedi.lsportfolio.di.components.Injector;
import com.stedi.lsportfolio.di.modules.AppModule;

public final class App extends Application {
    private static App instance;

    private Injector injector;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        injector = DaggerInjector.builder()
                .appModule(new AppModule(this))
                .build();
    }

    /**
     * For testing purpose
     */
    public static App getInstance() {
        return instance;
    }

    public static Injector getInjector() {
        return instance.injector;
    }
}
