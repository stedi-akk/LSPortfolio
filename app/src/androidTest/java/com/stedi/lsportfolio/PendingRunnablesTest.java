package com.stedi.lsportfolio;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.lsportfolio.other.PendingRunnables;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PendingRunnablesTest {
    private final Object lock = new Object();
    private final PendingRunnables pendingRunnables = new PendingRunnables();

    @Test
    public void afterAllow() {
        pendingRunnables.allow();
        pendingRunnables.post(new Runnable() {
            @Override
            public void run() {
                notifyTest();
            }
        });
        waitTest();
    }

    @Test
    public void beforeAllow() {
        pendingRunnables.post(new Runnable() {
            @Override
            public void run() {
                notifyTest();
            }
        });
        pendingRunnables.allow();
        waitTest();
    }

    private void waitTest() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyTest() {
        synchronized (lock) {
            lock.notify();
        }
    }
}
