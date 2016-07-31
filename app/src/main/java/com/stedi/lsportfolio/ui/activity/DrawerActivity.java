package com.stedi.lsportfolio.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.ui.fragments.ContactFragment;
import com.stedi.lsportfolio.ui.fragments.LsAllAppsFragment;
import com.stedi.lsportfolio.ui.other.AboutDialog;

public class DrawerActivity extends ToolbarActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        setToolbarIconListener(toolbarIconListener);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_activity_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.drawer_activity_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (getCurrentFragment() == null) {
            showFragment(new LsAllAppsFragment(), false);
            navigationView.setCheckedItem(R.id.nav_ls_all_apps);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        if (getCurrentFragment() instanceof ContactFragment)
            navigationView.setCheckedItem(R.id.nav_ls_all_apps);
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawers();
        Fragment frgCurrent = getCurrentFragment();
        if (item.getItemId() == R.id.nav_ls_all_apps && frgCurrent instanceof ContactFragment) {
            getSupportFragmentManager().popBackStack();
        } else if (item.getItemId() == R.id.nav_contact && frgCurrent instanceof LsAllAppsFragment) {
            showFragment(new ContactFragment(), true);
        } else if (item.getItemId() == R.id.nav_about) {
            new AboutDialog().show(getSupportFragmentManager(), AboutDialog.class.getSimpleName());
        }
        return true;
    }

    private View.OnClickListener toolbarIconListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getToolbarIcon() == ToolbarIcon.BACK && getCurrentFragment() instanceof ContactFragment) {
                getSupportFragmentManager().popBackStack();
                navigationView.setCheckedItem(R.id.nav_ls_all_apps);
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
