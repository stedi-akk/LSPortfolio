package com.stedi.lsportfolio;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.lsportfolio.other.PendingUiRunnables;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PendingUiRunnablesTest {
    private final Object lock = new Object();
    private final PendingUiRunnables pur = new PendingUiRunnables();

    @Test
    public void afterPostMode() {
        pur.postMode();
        pur.post(new Runnable() {
            @Override
            public void run() {
                notifyTest();
            }
        });
        waitTest();
    }

    @Test
    public void beforePostMode() {
        pur.post(new Runnable() {
            @Override
            public void run() {
                notifyTest();
            }
        });
        pur.postMode();
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock) {
            lock.notify();
        }
    }
}
