package com.stedi.lsportfolio.other;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * For posting and caching runnables.
 * Caching mode by default.
 */
@Singleton
public final class PendingUiRunnables {
    private final LinkedList<Runnable> cache = new LinkedList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private volatile boolean cachingMode = true;

    @Inject
    public PendingUiRunnables() {
    }

    /**
     * On postMode() runnable will be executed immediately to UI thread.
     * On cachingMode() runnable will be cached.
     */
    public synchronized void post(Runnable runnable) {
        if (!cachingMode)
            handler.post(runnable);
        else
            cache.add(runnable);
    }

    /**
     * Disable caching and post all cached runnables to UI thread.
     */
    public synchronized void postMode() {
        cachingMode = false;
        while (!cache.isEmpty())
            handler.post(cache.pollFirst());
    }

    /**
     * Allow caching.
     */
    public synchronized void cachingMode() {
        cachingMode = true;
    }
}
