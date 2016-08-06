package com.stedi.lsportfolio.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.stedi.lsportfolio.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class ToolbarActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    private ToolbarIcon currentIcon;

    public enum ToolbarIcon {
        DRAWER(R.drawable.ic_menu_white),
        BACK(R.drawable.ic_arrow_back_white);

        private int resId;

        ToolbarIcon(int resId) {
            this.resId = resId;
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initToolbar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initToolbar();
    }

    public void setToolbarIcon(ToolbarIcon icon) {
        currentIcon = icon;
        toolbar.setNavigationIcon(currentIcon.resId);
    }

    public ToolbarIcon getToolbarIcon() {
        return currentIcon;
    }

    public void setToolbarIconListener(View.OnClickListener onClickListener) {
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    public void setToolbarTitle(CharSequence title) {
        toolbar.setTitle(title);
    }

    public void setToolbarTitle(int resId) {
        toolbar.setTitle(resId);
    }

    private void initToolbar() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);
    }
}
