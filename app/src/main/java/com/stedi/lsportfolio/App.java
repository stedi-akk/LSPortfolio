package com.stedi.lsportfolio;

import android.app.Application;

import com.stedi.lsportfolio.di.components.AppComponent;
import com.stedi.lsportfolio.di.components.DaggerAppComponent;
import com.stedi.lsportfolio.di.modules.AppModule;

public final class App extends Application {
    private static App instance;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    /**
     * For testing purpose
     */
    public static App getInstance() {
        return instance;
    }

    public static AppComponent getComponent() {
        return instance.appComponent;
    }
}
