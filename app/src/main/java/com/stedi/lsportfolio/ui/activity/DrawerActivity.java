package com.stedi.lsportfolio.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.BuildConfig;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.other.ContextUtils;
import com.stedi.lsportfolio.ui.fragments.ContactFragment;
import com.stedi.lsportfolio.ui.fragments.LsAllAppsFragment;
import com.stedi.lsportfolio.ui.other.AboutDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class DrawerActivity extends ToolbarActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    @BindView(R.id.drawer_activity_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_activity_navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer_activity_fab) FloatingActionButton fab;

    @Inject ContextUtils contextUtils;

    private Runnable drawerCloseRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.drawer_activity);
        setToolbarIconListener(toolbarIconListener);

        TextView tvVersion = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navigation_header_version_info);
        tvVersion.setText(BuildConfig.VERSION_NAME);

        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(this);

        Fragment current = getCurrentFragment();
        if (current == null) {
            current = new LsAllAppsFragment();
            showFragment(current, false);
        }

        if (current instanceof LsAllAppsFragment)
            onShowLsAllAppsFragment();
        else if (current instanceof ContactFragment)
            onShowContactFragment();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        if (getCurrentFragment() instanceof ContactFragment)
            onShowLsAllAppsFragment();
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        closeDrawerAndRun(() -> {
            Fragment frgCurrent = getCurrentFragment();
            if (item.getItemId() == R.id.action_ls_all_apps && frgCurrent instanceof ContactFragment) {
                getSupportFragmentManager().popBackStack();
                onShowLsAllAppsFragment();
            } else if (item.getItemId() == R.id.action_contact && frgCurrent instanceof LsAllAppsFragment) {
                showFragment(new ContactFragment(), true);
                onShowContactFragment();
            } else if (item.getItemId() == R.id.action_source_code) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/stedi-akk/LSPortfolio")));
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                    contextUtils.showToast(R.string.unknown_error);
                }
            } else if (item.getItemId() == R.id.action_about) {
                new AboutDialog().show(getSupportFragmentManager(), AboutDialog.class.getSimpleName());
            }
        });
        return true;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (drawerCloseRunnable != null) {
            drawerCloseRunnable.run();
            drawerCloseRunnable = null;
        }
    }

    public void setToolbarScrollingEnabled(boolean enabled) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) getToolbar().getLayoutParams();
        if (enabled) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS |
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        } else {
            params.setScrollFlags(-1);
        }
    }

    public void setFabVisible(boolean visible, boolean animate) {
        if (animate) {
            if (visible)
                fab.show();
            else
                fab.hide();
        } else {
            fab.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        fab.setEnabled(visible);
    }

    private View.OnClickListener toolbarIconListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getToolbarIcon() == ToolbarIcon.BACK && getCurrentFragment() instanceof ContactFragment) {
                getSupportFragmentManager().popBackStack();
                onShowLsAllAppsFragment();
            } else if (getToolbarIcon() == ToolbarIcon.DRAWER
                    && drawerLayout.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_UNLOCKED) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
    };

    @OnClick(R.id.drawer_activity_fab)
    public void onFabClick(View v) {
        Snackbar.make(v, R.string.for_showcase, Snackbar.LENGTH_LONG).show();
    }

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

    private void onShowLsAllAppsFragment() {
        setToolbarScrollingEnabled(true);
        setFabVisible(true, false);
        navigationView.setCheckedItem(R.id.action_ls_all_apps);
    }

    private void onShowContactFragment() {
        setToolbarScrollingEnabled(false);
        setFabVisible(false, false);
        navigationView.setCheckedItem(R.id.action_contact);
    }

    private void closeDrawerAndRun(Runnable run) {
        drawerCloseRunnable = run;
        drawerLayout.closeDrawers();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
