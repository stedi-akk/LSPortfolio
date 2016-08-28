package com.stedi.lsportfolio.other;

import android.util.Log;
import android.widget.Toast;

import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.BuildConfig;

/**
 * For debug purpose
 */
public final class DBG {
    private static final String TAG = "LS_DEBUG";

    public static void log(String text) {
        if (!BuildConfig.DEBUG)
            return;
        Log.d(TAG, text);
    }

    public static void toast(String text) {
        if (!BuildConfig.DEBUG)
            return;
        Toast.makeText(App.getInstance(), TAG + " " + text, Toast.LENGTH_SHORT).show();
    }
}
