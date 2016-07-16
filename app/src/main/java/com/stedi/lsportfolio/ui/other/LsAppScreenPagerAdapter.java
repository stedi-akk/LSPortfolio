package com.stedi.lsportfolio.ui.other;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stedi.lsportfolio.App;

import java.util.List;

public class LsAppScreenPagerAdapter extends PagerAdapter {
    private List<String> imgUrls;
    private float pageWidth;

    public LsAppScreenPagerAdapter(List<String> imgUrls, float pageWidth) {
        this.pageWidth = pageWidth;
        this.imgUrls = imgUrls;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ImageView iv = new ImageView(App.getContext());
        Picasso.with(App.getContext()).load(imgUrls.get(position)).into(iv);
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
