package com.stedi.lsportfolio.other;

import android.util.Log;
import android.widget.Toast;

import com.stedi.lsportfolio.App;

/**
 * For testing purpose
 */
public final class DBG {
    private static final String TAG = "LS_DEBUG";

    public static void log(String text) {
        Log.d(TAG, text);
    }

    public static void toast(String text) {
        Toast.makeText(App.getInstance(), TAG + " " + text, Toast.LENGTH_SHORT).show();
    }
}
