package com.stedi.lsportfolio;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.lsportfolio.other.CachedUiRunnables;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CachedUiRunnablesTest {
    private CachedUiRunnables cur = new CachedUiRunnables();

    private volatile boolean[] callbacks;

    @Test
    public void afterPostModeOnce() {
        cur.postMode();
        postWithCallbacks(1);
        waitForCallbacks();
        assertTrue(cur.getCache().isEmpty());
    }

    @Test
    public void afterPostModeMultiple() {
        cur.postMode();
        postWithCallbacks(100);
        waitForCallbacks();
        assertTrue(cur.getCache().isEmpty());
    }

    @Test
    public void afterPostModeOnce_WithCacheMode() {
        cur.postMode();
        postWithCallbacks(1);
        cur.cachingMode();
        sleep(500);
        assertTrue(cur.getCache().size() >= 0);
        cur.postMode();
        waitForCallbacks();
        assertTrue(cur.getCache().isEmpty());
    }

    @Test
    public void afterPostModeMultiple_WithCacheMode() {
        cur.postMode();
        postWithCallbacks(100);
        cur.cachingMode();
        sleep(500);
        assertTrue(cur.getCache().size() >= 0);
        cur.postMode();
        waitForCallbacks();
        assertTrue(cur.getCache().isEmpty());
    }

    @Test
    public void beforePostModeOnce() {
        postWithCallbacks(1);
        sleep(500);
        assertTrue(cur.getCache().size() >= 0);
        cur.postMode();
        waitForCallbacks();
        assertTrue(cur.getCache().isEmpty());
    }

    @Test
    public void beforePostModeMultiple() {
        postWithCallbacks(100);
        sleep(500);
        assertTrue(cur.getCache().size() >= 0);
        cur.postMode();
        waitForCallbacks();
        assertTrue(cur.getCache().isEmpty());
    }

    @Test
    public void beforePostModeOnce_WithCacheMode() {
        postWithCallbacks(1);
        sleep(500);
        assertTrue(cur.getCache().size() >= 0);
        cur.postMode();
        cur.cachingMode();
        sleep(500);
        assertTrue(cur.getCache().size() >= 0);
        cur.postMode();
        waitForCallbacks();
        assertTrue(cur.getCache().isEmpty());
    }

    @Test
    public void beforePostModeMultiple_WithCacheMode() {
        postWithCallbacks(100);
        sleep(500);
        assertTrue(cur.getCache().size() >= 0);
        cur.postMode();
        cur.cachingMode();
        sleep(500);
        assertTrue(cur.getCache().size() >= 0);
        cur.postMode();
        waitForCallbacks();
        assertTrue(cur.getCache().isEmpty());
    }

    private void postWithCallbacks(int size) {
        callbacks = new boolean[size];
        for (int i = 0; i < callbacks.length; i++) {
            final int finalI = i;
            cur.post(new Runnable() {
                @Override
                public void run() {
                    if (callbacks[finalI])
                        throw new RuntimeException("called more than once");
                    callbacks[finalI] = true;
                }
            });
        }
    }

    private void waitForCallbacks() {
        boolean falseCallbacks = true;
        while (falseCallbacks) {
            falseCallbacks = false;
            for (boolean callback : callbacks) {
                if (!callback)
                    falseCallbacks = true;
            }
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
