package com.stedi.lsportfolio.other;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * For posting and caching runnables.
 * Caching mode by default.
 */
@Singleton
public final class CachedUiRunnables {
    private final LinkedList<Runnable> cache = new LinkedList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean cachingMode = true;

    @Inject
    public CachedUiRunnables() {
    }

    /**
     * On postMode() runnable will be posted on UI thread.
     * On cachingMode() runnable will be cached.
     */
    public void post(Runnable runnable) {
        handler.post(() -> {
            if (!cachingMode)
                runnable.run();
            else
                cache.add(runnable);
        });
    }

    /**
     * Disable caching and post all cached runnables on UI thread.
     */
    public void postMode() {
        cachingMode = false;
        releaseCache();
    }

    /**
     * Allow caching.
     */
    public void cachingMode() {
        cachingMode = true;
    }

    public List<Runnable> getCache() {
        return cache;
    }

    private void releaseCache() {
        if (!cache.isEmpty()) {
            LinkedList<Runnable> release = new LinkedList<>(cache);
            cache.clear();
            while (!release.isEmpty())
                post(release.pollFirst());
        }
    }
}
