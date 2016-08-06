package com.stedi.lsportfolio.ui.other;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;
import com.stedi.lsportfolio.other.PicassoHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class LsAppScreenPagerAdapter extends PagerAdapter {
    private final Context context;
    private final PicassoHelper picassoHelper;

    private List<String> imgUrls;
    private Transformation transformation;

    private float pageWidth;

    @Inject
    public LsAppScreenPagerAdapter(@Named("ActivityContext") Context context, PicassoHelper picassoHelper) {
        this.context = context;
        this.picassoHelper = picassoHelper;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setPageWidth(float pageWidth) {
        this.pageWidth = pageWidth;
    }

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ImageView iv = new ImageView(context);
        picassoHelper.load(imgUrls.get(position), iv, transformation);
        collection.addView(iv);
        return iv;
    }

    @Override
    public float getPageWidth(int position) {
        return pageWidth;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
