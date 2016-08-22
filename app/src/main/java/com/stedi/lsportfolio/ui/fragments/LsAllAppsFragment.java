package com.stedi.lsportfolio.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.api.Api;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.api.ResponseLsApp;
import com.stedi.lsportfolio.model.LsAllApps;
import com.stedi.lsportfolio.model.LsApp;
import com.stedi.lsportfolio.other.CachedUiRunnables;
import com.stedi.lsportfolio.other.ContextUtils;
import com.stedi.lsportfolio.other.NoNetworkException;
import com.stedi.lsportfolio.ui.activity.BaseActivity;
import com.stedi.lsportfolio.ui.activity.DrawerActivity;
import com.stedi.lsportfolio.ui.activity.LsAppActivity;
import com.stedi.lsportfolio.ui.activity.ToolbarActivity;
import com.stedi.lsportfolio.ui.other.LsAllAppsAdapter;
import com.stedi.lsportfolio.ui.other.RxDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.schedulers.Schedulers;

public class LsAllAppsFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {

    private final String KEY_IS_LS_ALL_APPS_REQUESTED = "KEY_IS_LS_ALL_APPS_REQUESTED";
    private final String KEY_IS_SWIPE_REFRESHING = "KEY_IS_SWIPE_REFRESHING";
    private final String KEY_SEARCH_QUERY = "KEY_SEARCH_QUERY";
    private final String KEY_IS_SEARCH_EXPANDED = "KEY_IS_SEARCH_EXPANDED";

    @BindView(R.id.ls_all_apps_list_swipe) SwipeRefreshLayout swipeLayout;
    @BindView(R.id.ls_all_apps_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.ls_all_apps_empty_view) View emptyView;
    @BindView(R.id.ls_all_apps_try_again_btn) View tryAgainBtn;
    private Unbinder unbinder;

    @Inject Bus bus;
    @Inject Api api;
    @Inject CachedUiRunnables cur;
    @Inject LsAllApps allApps;
    @Inject ContextUtils contextUtils;
    @Inject LsAllAppsAdapter recyclerAdapter;

    private SearchView searchView;
    private String searchQuery = "";
    private boolean isLsAllAppsRequested;
    private boolean isSwipeRefreshing;
    private boolean isSearchExpanded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            isLsAllAppsRequested = savedInstanceState.getBoolean(KEY_IS_LS_ALL_APPS_REQUESTED, false);
            isSwipeRefreshing = savedInstanceState.getBoolean(KEY_IS_SWIPE_REFRESHING, false);
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, "");
            isSearchExpanded = savedInstanceState.getBoolean(KEY_IS_SEARCH_EXPANDED, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getComponent().inject(this);

        ToolbarActivity act = (ToolbarActivity) getActivity();
        act.setToolbarIcon(ToolbarActivity.ToolbarIcon.DRAWER);
        act.setToolbarTitle(R.string.apps);

        View root = inflater.inflate(R.layout.ls_all_apps_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        if (isSwipeRefreshing)
            swipeLayout.post(() -> swipeLayout.setRefreshing(true));
        swipeLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnClickListener(recyclerViewClickListener);
        tryAgainBtn.setOnClickListener(v -> {
            new RxDialog<ResponseLsAllApps>()
                    .with(() -> api.requestLsAllApps())
                    .execute(this);
        });

        if (allApps.getApps() == null) {
            tryAgainBtn.setVisibility(View.VISIBLE);
            swipeLayout.setEnabled(false);
            ((DrawerActivity) getActivity()).setToolbarScrollingEnabled(false);
            if (!isLsAllAppsRequested) {
                isLsAllAppsRequested = true;
                new RxDialog<ResponseLsAllApps>()
                        .with(() -> api.requestLsAllApps())
                        .execute(this);
            }
        } else {
            fillAppsList();
        }

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (allApps.getApps() == null || allApps.getApps().isEmpty())
            return;
        inflater.inflate(R.menu.ls_all_apps_fragment, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        if (isSearchExpanded)
            MenuItemCompat.expandActionView(searchItem);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
        if (!searchQuery.isEmpty())
            searchView.post(() -> searchView.setQuery(searchQuery, true));
    }

    @Override
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        cur.postMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        cur.cachingMode();
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (searchView != null)
            searchView.setOnQueryTextListener(null);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        isSearchExpanded = true;
        swipeLayout.setEnabled(false);
        ((DrawerActivity) getActivity()).setToolbarScrollingEnabled(false);
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        isSearchExpanded = false;
        swipeLayout.setEnabled(true);
        ((DrawerActivity) getActivity()).setToolbarScrollingEnabled(true);
        performSearch("");
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        performSearch(newText);
        return true;
    }

    @Override
    public void onRefresh() {
        isSwipeRefreshing = true;
        api.requestLsAllApps()
                .subscribeOn(Schedulers.io())
                .subscribe(responseLsAllApps -> cur.post(() -> bus.post(responseLsAllApps)),
                        throwable -> cur.post(() -> bus.post(throwable)));
    }

    private View.OnClickListener recyclerViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildLayoutPosition(v);
            LsApp app = recyclerAdapter.getItem(itemPosition);
            new RxDialog<ResponseLsApp>()
                    .with(() -> api.requestLsApp(app.getId()))
                    .execute(LsAllAppsFragment.this);
        }
    };

    @Subscribe
    public void onResponseLsApp(ResponseLsApp response) {
        Intent intent = new Intent(getActivity(), LsAppActivity.class);
        intent.putExtra(LsAppActivity.INTENT_APP_KEY, response.getApp());
        startActivity(intent);
    }

    @Subscribe
    public void onResponseLsAllApps(ResponseLsAllApps response) {
        isLsAllAppsRequested = false;
        allApps.setApps(response.getApps());
        fillAppsList();
        getActivity().invalidateOptionsMenu();
        contextUtils.showToast(R.string.list_updated);
        disableSwipeRefreshing();
    }

    @Subscribe
    public void onException(Exception ex) {
        ex.printStackTrace();
        contextUtils.showToast(ex instanceof NoNetworkException ? R.string.no_internet : R.string.unknown_error);
        disableSwipeRefreshing();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_LS_ALL_APPS_REQUESTED, isLsAllAppsRequested);
        outState.putBoolean(KEY_IS_SWIPE_REFRESHING, isSwipeRefreshing);
        outState.putString(KEY_SEARCH_QUERY, searchQuery);
        outState.putBoolean(KEY_IS_SEARCH_EXPANDED, isSearchExpanded);
    }

    private void fillAppsList() {
        boolean isEmpty = allApps.getApps().isEmpty();
        ((DrawerActivity) getActivity()).setToolbarScrollingEnabled(!isEmpty);
        swipeLayout.setEnabled(!isEmpty);
        recyclerView.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
        tryAgainBtn.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerAdapter.setApps(allApps.getApps());
    }

    private void performSearch(String query) {
        searchQuery = query;
        if (searchQuery.isEmpty()) {
            recyclerAdapter.setApps(allApps.getApps());
        } else {
            Observable.from(allApps.getApps())
                    .filter(lsApp -> lsApp.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .toList()
                    .subscribe(lsApps -> recyclerAdapter.setApps(lsApps, query));
        }
    }

    private void disableSwipeRefreshing() {
        swipeLayout.post(() -> {
            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
                isSwipeRefreshing = false;
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
