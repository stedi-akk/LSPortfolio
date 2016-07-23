package com.stedi.lsportfolio.other;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class PendingRunnables {
    private final LinkedList<Runnable> runnables = new LinkedList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean isAllowed;

    @Inject
    public PendingRunnables() {
    }

    public synchronized void post(Runnable runnable) {
        if (isAllowed)
            handler.post(runnable);
        else
            runnables.add(runnable);
    }

    public synchronized void allow() {
        isAllowed = true;
        while (!runnables.isEmpty())
            handler.post(runnables.pollFirst());
    }

    public synchronized void forbid() {
        isAllowed = false;
    }
}
