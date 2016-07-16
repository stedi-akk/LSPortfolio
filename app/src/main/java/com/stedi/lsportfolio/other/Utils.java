package com.stedi.lsportfolio.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;

public class Utils {
    private static Toast singleToast;

    public static void log(String text) {
        Log.d("LS_DEBUG", text);
    }

    public static void showToast(int resId) {
        showToast(App.getContext().getString(resId));
    }

    public static void showToast(CharSequence text) {
        if (singleToast != null)
            singleToast.cancel();
        singleToast = Toast.makeText(App.getContext(), text, Toast.LENGTH_LONG);
        singleToast.show();
    }

    public static float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, App.getContext().getResources().getDisplayMetrics());
    }

    public static boolean hasInternet() {
        ConnectivityManager cm = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public static void throwOnNoNetwork() throws NoNetworkException {
        if (!Utils.hasInternet())
            throw new NoNetworkException();
    }

    public static void loadWithPicasso(String url, ImageView iv) {
        Utils.loadWithPicasso(url, iv, null);
    }

    public static void loadWithPicasso(String url, ImageView iv, Transformation transformation) {
        RequestCreator creator = Picasso.with(App.getContext())
                .load(url)
                .placeholder(R.drawable.main_progress)
                .error(R.drawable.alert);
        if (transformation != null)
            creator.transform(transformation);
        creator.into(iv);
    }

    public static boolean isSw600dp() {
        return App.getContext().getResources().getBoolean(R.bool.is_sw600dp);
    }
}
