package com.stedi.lsportfolio.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.ui.fragments.ContactFragment;
import com.stedi.lsportfolio.ui.fragments.LsAllAppsFragment;

public class DrawerActivity extends ToolbarActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        setToolbarIconListener(toolbarIconListener);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_activity_drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        findViewById(R.id.drawer_activity_item_apps).setOnClickListener(drawerItemsListener);
        findViewById(R.id.drawer_activity_item_contact).setOnClickListener(drawerItemsListener);

        if (getCurrentFragment() == null)
            showFragment(new LsAllAppsFragment(), false);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    private View.OnClickListener drawerItemsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawerLayout.closeDrawers();
            Fragment frgCurrent = getCurrentFragment();
            if (v.getId() == R.id.drawer_activity_item_apps && frgCurrent instanceof ContactFragment) {
                getSupportFragmentManager().popBackStack();
            } else if (v.getId() == R.id.drawer_activity_item_contact && frgCurrent instanceof LsAllAppsFragment) {
                showFragment(new ContactFragment(), true);
            }
        }
    };

    private View.OnClickListener toolbarIconListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getToolbarIcon() == ToolbarIcon.BACK) {
                getSupportFragmentManager().popBackStack();
            } else if (getToolbarIcon() == ToolbarIcon.DRAWER
                    && drawerLayout.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_UNLOCKED) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
    };

    private void showFragment(Fragment frg, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.drawer_activity_content, frg, frg.getClass().getSimpleName());
        if (addToBackStack)
            ft.addToBackStack(frg.getClass().getSimpleName());
        ft.commit();
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.drawer_activity_content);
    }
}