package com.stedi.lsportfolio.ui.other;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager with ability to disable swipes
 */
public class BlockingViewPager extends ViewPager {
    private boolean disableSwipe;

    public BlockingViewPager(Context context) {
        super(context);
    }

    public BlockingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (disableSwipe)
            return false;
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (disableSwipe)
            return false;
        return super.onTouchEvent(event);
    }

    public void disableSwipe(boolean disable) {
        this.disableSwipe = disable;
    }
}