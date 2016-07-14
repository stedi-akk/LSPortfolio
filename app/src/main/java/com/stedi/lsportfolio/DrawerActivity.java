package com.stedi.lsportfolio;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DrawerActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        initToolbar();
        initDrawer();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.drawer_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_menu_icon);
        toolbar.setNavigationOnClickListener(toolbarNavigationListener);
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_activity_drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        findViewById(R.id.drawer_activity_item_aplikacje).setOnClickListener(drawerItemsListener);
        findViewById(R.id.drawer_activity_item_kontakt).setOnClickListener(drawerItemsListener);
    }

    private View.OnClickListener toolbarNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.showToast("its work");
            if (drawerLayout.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_UNLOCKED)
                drawerLayout.openDrawer(GravityCompat.START);
        }
    };

    private View.OnClickListener drawerItemsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.showToast("yes, its work too");
        }
    };
}
