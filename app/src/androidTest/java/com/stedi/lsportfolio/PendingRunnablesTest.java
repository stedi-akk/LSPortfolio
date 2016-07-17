package com.stedi.lsportfolio;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PendingRunnablesTest {
    private final Object lock = new Object();

    @Before
    public void before() {
        App.onPause();
    }

    @Test
    public void afterOnResume() {
        App.onResume();
        App.postOnResume(new Runnable() {
            @Override
            public void run() {
                notifyTest();
            }
        });
        waitTest();
    }

    @Test
    public void beforeOnResume() {
        App.postOnResume(new Runnable() {
            @Override
            public void run() {
                notifyTest();
            }
        });
        App.onResume();
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
