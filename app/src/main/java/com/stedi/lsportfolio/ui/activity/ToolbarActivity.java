package com.stedi.lsportfolio.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stedi.lsportfolio.R;

public abstract class ToolbarActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ToolbarIcon currentIcon;
    private TextView tvTitle;

    public enum ToolbarIcon {
        DRAWER(R.drawable.ic_menu_icon),
        BACK(R.drawable.ic_back_icon);

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
        tvTitle.setText(title);
    }

    public void setToolbarTitle(int resId) {
        tvTitle.setText(resId);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);
    }
}
