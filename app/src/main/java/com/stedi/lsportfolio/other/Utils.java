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
import com.stedi.lsportfolio.R;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public final class Utils {
    private final Context context;
    private final Picasso picasso;

    private Toast singleToast;

    @Inject
    public Utils(@Named("ApplicationContext") Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
    }

    public static void log(String text) {
        Log.d("LS_DEBUG", text);
    }

    public void showToast(int resId) {
        showToast(context.getString(resId));
    }

    public void showToast(CharSequence text) {
        if (singleToast != null)
            singleToast.cancel();
        singleToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        singleToast.show();
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

    public void loadWithPicasso(String url, ImageView iv) {
        loadWithPicasso(url, iv, null);
    }

    public void loadWithPicasso(String url, ImageView iv, Transformation transformation) {
        RequestCreator creator = picasso
                .load(url)
                .placeholder(R.drawable.shape_static_progress)
                .error(R.drawable.shape_alert);
        if (transformation != null)
            creator.transform(transformation);
        creator.into(iv);
    }

    public boolean isSw600dp() {
        return context.getResources().getBoolean(R.bool.is_sw600dp);
    }
}
