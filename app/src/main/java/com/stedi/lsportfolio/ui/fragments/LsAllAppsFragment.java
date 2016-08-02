package com.stedi.lsportfolio.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.schedulers.Schedulers;

public class LsAllAppsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String KEY_IS_LS_ALL_APPS_REQUESTED = "KEY_IS_LS_ALL_APPS_REQUESTED";
    private final String KEY_IS_SWIPE_REFRESHING = "KEY_IS_SWIPE_REFRESHING";

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

    private boolean isLsAllAppsRequested;
    private boolean isSwipeRefreshing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isLsAllAppsRequested = savedInstanceState.getBoolean(KEY_IS_LS_ALL_APPS_REQUESTED);
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
            if (!isLsAllAppsRequested) {
                isLsAllAppsRequested = true;
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        isSwipeRefreshing = true;
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
        fillListView();
        contextUtils.showToast(R.string.list_updated);
        disableSwipeLayout();
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
        outState.putBoolean(KEY_IS_LS_ALL_APPS_REQUESTED, isLsAllAppsRequested);
        outState.putBoolean(KEY_IS_SWIPE_REFRESHING, isSwipeRefreshing);
    }

    private void fillListView() {
        boolean isEmpty = allApps.getApps().isEmpty();
        swipeLayout.setEnabled(!isEmpty);
        recyclerView.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
        tryAgainBtn.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerAdapter.setApps(allApps.getApps());
    }

    private void disableSwipeLayout() {
        swipeLayout.post(() -> {
            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
                isSwipeRefreshing = false;
            }
        });
    }
}
