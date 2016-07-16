package com.stedi.lsportfolio;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.squareup.otto.Bus;

import java.util.LinkedList;

// TODO ic_launcher
// TODO build.gradle
// TODO correct toolbar shadow
// TODO layout optimization (text-image, relative, etc)
public final class App extends Application {
    private static App instance;

    private final LinkedList<Runnable> pendingRunnables = new LinkedList<>();
    private final Handler handler = new Handler();

    private Bus bus;
    private boolean isResumed;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        bus = new Bus();
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static Bus getBus() {
        return instance.bus;
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
