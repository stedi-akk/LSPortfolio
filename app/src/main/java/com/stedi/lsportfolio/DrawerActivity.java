package com.stedi.lsportfolio;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
        if (getCurrentFragment() == null)
            showFragment(new AppsFragment(), false);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.drawer_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_menu_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_UNLOCKED)
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_activity_drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        findViewById(R.id.drawer_activity_item_apps).setOnClickListener(drawerItemsListener);
        findViewById(R.id.drawer_activity_item_contact).setOnClickListener(drawerItemsListener);
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.drawer_activity_content);
    }

    private void showFragment(Fragment frg, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.drawer_activity_content, frg, frg.getClass().getSimpleName());
        if (addToBackStack)
            ft.addToBackStack(frg.getClass().getSimpleName());
        ft.commit();
    }

    private View.OnClickListener drawerItemsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawerLayout.closeDrawers();
            Fragment frgCurrent = getCurrentFragment();
            if (v.getId() == R.id.drawer_activity_item_apps && frgCurrent instanceof ContactFragment) {
                getSupportFragmentManager().popBackStack();
            } else if (v.getId() == R.id.drawer_activity_item_contact && frgCurrent instanceof AppsFragment) {
                showFragment(new ContactFragment(), true);
            }
        }
    };
}
