package com.stedi.lsportfolio;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.stedi.lsportfolio.di.components.DaggerInjector;
import com.stedi.lsportfolio.di.components.Injector;

import java.util.LinkedList;

public final class App extends Application {
    private static App instance;

    private Injector injector;

    private final LinkedList<Runnable> pendingRunnables = new LinkedList<>();
    private final Handler handler = new Handler();
    private boolean isResumed;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        injector = DaggerInjector.builder().build();
    }

    public static Injector getInjector() {
        return instance.injector;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static void postOnResume(Runnable runnable) {
        if (instance.isResumed)
            instance.handler.post(runnable);
        else
            instance.pendingRunnables.add(runnable);
    }

    public static void onResume() {
        instance.isResumed = true;
        instance.executePendingRunnables();
    }

    public static void onPause() {
        instance.isResumed = false;
    }

    private void executePendingRunnables() {
        while (!pendingRunnables.isEmpty())
            handler.post(pendingRunnables.pollFirst());
    }
}
