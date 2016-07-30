package com.stedi.lsportfolio.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.api.Api;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.api.ResponseLsApp;
import com.stedi.lsportfolio.model.LsAllApps;
import com.stedi.lsportfolio.model.LsApp;
import com.stedi.lsportfolio.other.CachedUiRunnables;
import com.stedi.lsportfolio.other.ContextUtils;
import com.stedi.lsportfolio.other.NoNetworkException;
import com.stedi.lsportfolio.other.SimpleObserver;
import com.stedi.lsportfolio.ui.activity.LsAppActivity;
import com.stedi.lsportfolio.ui.activity.ToolbarActivity;
import com.stedi.lsportfolio.ui.other.LsAllAppsAdapter;
import com.stedi.lsportfolio.ui.other.RxDialog;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public class LsAllAppsFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private final String KEY_LS_ALL_APPS_REQUESTED = "KEY_LS_ALL_APPS_REQUESTED";
    private final String KEY_IS_SWIPE_REFRESHING = "KEY_IS_SWIPE_REFRESHING";

    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private View emptyView;
    private View tryAgainBtn;

    private boolean lsAllAppsRequested;
    private boolean isSwipeRefreshing;

    @Inject Bus bus;
    @Inject Api api;
    @Inject CachedUiRunnables cur;
    @Inject LsAllApps allApps;
    @Inject ContextUtils contextUtils;
    @Inject LsAllAppsAdapter appsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            lsAllAppsRequested = savedInstanceState.getBoolean(KEY_LS_ALL_APPS_REQUESTED);
            isSwipeRefreshing = savedInstanceState.getBoolean(KEY_IS_SWIPE_REFRESHING);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        App.getInjector().inject(this);

        ToolbarActivity act = (ToolbarActivity) getActivity();
        act.setToolbarIcon(ToolbarActivity.ToolbarIcon.DRAWER);
        act.setToolbarTitle(R.string.apps);

        View root = inflater.inflate(R.layout.ls_all_apps_fragment, container, false);

        if (swipeLayout != null)
            isSwipeRefreshing = swipeLayout.isRefreshing();
        swipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.ls_all_apps_list_swipe);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        if (isSwipeRefreshing)
            swipeLayout.post(() -> swipeLayout.setRefreshing(true));
        swipeLayout.setOnRefreshListener(this);

        listView = (ListView) root.findViewById(R.id.ls_all_apps_list);
        listView.setAdapter(appsAdapter);
        emptyView = root.findViewById(R.id.ls_all_apps_empty_view);
        tryAgainBtn = root.findViewById(R.id.ls_all_apps_try_again_btn);
        tryAgainBtn.setOnClickListener(v -> {
            new RxDialog<ResponseLsAllApps>()
                    .with(() -> api.requestLsAllApps())
                    .execute(this);
        });

        if (allApps.getApps() == null) {
            tryAgainBtn.setVisibility(View.VISIBLE);
            swipeLayout.setEnabled(false);
            if (!lsAllAppsRequested) {
                lsAllAppsRequested = true;
                new RxDialog<ResponseLsAllApps>()
                        .with(() -> api.requestLsAllApps())
                        .execute(this);
            }
        } else {
            fillListView();
        }

        return root;
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
    public void onRefresh() {
        api.requestLsAllApps()
                .subscribeOn(Schedulers.io())
                .subscribe(new SimpleObserver<ResponseLsAllApps>() {
                    @Override
                    public void onError(Throwable e) {
                        cur.post(() -> bus.post(e));
                    }

                    @Override
                    public void onNext(ResponseLsAllApps responseLsAllApps) {
                        cur.post(() -> bus.post(responseLsAllApps));
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LsApp app = (LsApp) parent.getItemAtPosition(position);
        new RxDialog<ResponseLsApp>()
                .with(() -> api.requestLsApp(app.getId()))
                .execute(this);
    }

    @Subscribe
    public void onResponseLsApp(ResponseLsApp response) {
        Intent intent = new Intent(getActivity(), LsAppActivity.class);
        intent.putExtra(LsAppActivity.INTENT_APP_KEY, response.getApp());
        startActivity(intent);
    }

    @Subscribe
    public void onResponseLsAllApps(ResponseLsAllApps response) {
        allApps.setApps(response.getApps());
        if (tryAgainBtn.getVisibility() == View.VISIBLE)
            tryAgainBtn.setVisibility(View.GONE);
        lsAllAppsRequested = false;
        fillListView();
        disableSwipeLayout();
        contextUtils.showToast(R.string.list_updated);
    }

    @Subscribe
    public void onException(Exception ex) {
        ex.printStackTrace();
        contextUtils.showToast(ex instanceof NoNetworkException ? R.string.no_internet : R.string.unknown_error);
        disableSwipeLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LS_ALL_APPS_REQUESTED, lsAllAppsRequested);
        outState.putBoolean(KEY_IS_SWIPE_REFRESHING, swipeLayout != null ? swipeLayout.isRefreshing() : isSwipeRefreshing);
    }

    private void fillListView() {
        boolean isEmpty = allApps.getApps().isEmpty();
        swipeLayout.setEnabled(!isEmpty);
        tryAgainBtn.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        listView.setEmptyView(emptyView);
        appsAdapter.setApps(allApps.getApps());
        listView.setOnItemClickListener(this);
    }

    private void disableSwipeLayout() {
        swipeLayout.post(() -> {
            if (swipeLayout.isRefreshing())
                swipeLayout.setRefreshing(false);
        });
    }
}
