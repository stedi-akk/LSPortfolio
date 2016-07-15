package com.stedi.lsportfolio;

import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

public class Utils {
    public static void log(String text) {
        Log.d("LS_DEBUG", text);
    }

    public static void showToast(int resId) {
        showToast(App.getContext().getString(resId));
    }

    public static void showToast(CharSequence text) {
        Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, App.getContext().getResources().getDisplayMetrics());
    }
}
