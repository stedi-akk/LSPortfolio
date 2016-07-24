package com.stedi.lsportfolio.ui.other;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stedi.lsportfolio.other.BorderTransformation;
import com.stedi.lsportfolio.other.Utils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class LsAppScreenPagerAdapter extends PagerAdapter {
    private final BorderTransformation transformation;
    private final Context context;
    private final Utils utils;

    private List<String> imgUrls;
    private float pageWidth;

    @Inject
    public LsAppScreenPagerAdapter(@Named("ApplicationContext") Context context, Utils utils) {
        this.context = context;
        this.utils = utils;
        this.transformation = new BorderTransformation(Color.LTGRAY, utils.dp2px(1));
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    /**
     * For getPageWidth()
     */
    public void setPageWidth(float pageWidth) {
        this.pageWidth = pageWidth;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ImageView iv = new ImageView(context);
        utils.loadWithPicasso(imgUrls.get(position), iv, transformation);
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
