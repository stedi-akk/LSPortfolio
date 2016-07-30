package com.stedi.lsportfolio.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.widget.Toast;

import com.stedi.lsportfolio.R;

/**
 * Utils methods based on context
 */
public class ContextUtils {
    private final Context context;

    private Toast lastToast; // for closing existing toast

    public ContextUtils(Context context) {
        this.context = context;
    }

    public void showToast(int resId) {
        showToast(context.getString(resId));
    }

    public void showToast(CharSequence text) {
        if (lastToast != null)
            lastToast.cancel();
        lastToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        lastToast.show();
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public boolean hasInternet() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo[] netsInfo = cm.getAllNetworkInfo();
        if (netsInfo == null)
            return false;
        for (NetworkInfo ni : netsInfo) {
            String type = ni.getTypeName();
            if (ni.isConnected() && (type.equalsIgnoreCase("wifi") || type.equalsIgnoreCase("mobile")))
                return true;
        }
        return false;
    }

    public void throwOnNoNetwork() throws NoNetworkException {
        if (!hasInternet())
            throw new NoNetworkException();
    }

    public boolean isSw600dp() {
        return context.getResources().getBoolean(R.bool.is_sw600dp);
    }
}
