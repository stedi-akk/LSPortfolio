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

import com.squareup.otto.Subscribe;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.api.ResponseLsApp;
import com.stedi.lsportfolio.api.Server;
import com.stedi.lsportfolio.model.LsAllApps;
import com.stedi.lsportfolio.model.LsApp;
import com.stedi.lsportfolio.other.NoNetworkException;
import com.stedi.lsportfolio.other.Utils;
import com.stedi.lsportfolio.ui.activity.LsAppActivity;
import com.stedi.lsportfolio.ui.activity.ToolbarActivity;
import com.stedi.lsportfolio.ui.other.AsyncDialog;
import com.stedi.lsportfolio.ui.other.LsAllAppsAdapter;

// TODO catch wipe bug
// TODO tryagain button on empty view
public class LsAllAppsFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private final String KEY_CHECKED_ITEM_POSITION = "KEY_CHECKED_ITEM_POSITION";
    private final String KEY_LS_ALL_APPS_REQUESTED = "KEY_LS_ALL_APPS_REQUESTED";
    private final String KEY_IS_SWIPE_REFRESHING = "KEY_IS_SWIPE_REFRESHING";

    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private View emptyView;
    private View tryAgainBtn;

    private boolean lsAllAppsRequested;
    private boolean isSwipeRefreshing;
    private int checkedItemPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            checkedItemPosition = savedInstanceState.getInt(KEY_CHECKED_ITEM_POSITION);
            lsAllAppsRequested = savedInstanceState.getBoolean(KEY_LS_ALL_APPS_REQUESTED);
            isSwipeRefreshing = savedInstanceState.getBoolean(KEY_IS_SWIPE_REFRESHING);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToolbarActivity act = (ToolbarActivity) getActivity();
        act.setToolbarIcon(ToolbarActivity.ToolbarIcon.DRAWER);
        act.setToolbarTitle(R.string.apps);

        View root = inflater.inflate(R.layout.ls_all_apps_fragment, container, false);

        if (swipeLayout != null)
            isSwipeRefreshing = swipeLayout.isRefreshing();
        swipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.ls_all_apps_list_swipe);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        if (isSwipeRefreshing) {
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(true);
                }
            });
        }
        swipeLayout.setOnRefreshListener(this);

        listView = (ListView) root.findViewById(R.id.ls_all_apps_list);
        emptyView = root.findViewById(R.id.ls_all_apps_empty_view);
        tryAgainBtn = root.findViewById(R.id.ls_all_apps_try_again);
        tryAgainBtn.setOnClickListener(this);

        if (LsAllApps.getInstance().getApps() == null) {
            tryAgainBtn.setVisibility(View.VISIBLE);
            swipeLayout.setEnabled(false);
            if (!lsAllAppsRequested) {
                lsAllAppsRequested = true;
                requestLsAllApps();
            }
        } else {
            fillListView();
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        App.getBus().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getBus().unregister(this);
    }

    private void requestLsAllApps() {
        new AsyncDialog<ResponseLsAllApps>() {
            @Override
            protected ResponseLsAllApps doInBackground() throws Exception {
                return Server.requestLsAllApps();
            }
        }.execute(this);
    }

    private void fillListView() {
        swipeLayout.setEnabled(!LsAllApps.getInstance().getApps().isEmpty());
        listView.setEmptyView(emptyView);
        listView.setAdapter(new LsAllAppsAdapter(getActivity(), LsAllApps.getInstance().getApps()));
        if (checkedItemPosition != -1)
            listView.setItemChecked(checkedItemPosition, true);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseLsAllApps response = Server.requestLsAllApps();
                    App.postOnResume(new Runnable() {
                        @Override
                        public void run() {
                            App.getBus().post(response);
                        }
                    });
                } catch (final Exception ex) {
                    App.postOnResume(new Runnable() {
                        @Override
                        public void run() {
                            App.getBus().post(ex);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checkedItemPosition = ((ListView) parent).getCheckedItemPosition();
        final LsApp app = (LsApp) parent.getItemAtPosition(position);
        new AsyncDialog<ResponseLsApp>() {
            @Override
            protected ResponseLsApp doInBackground() throws Exception {
                return Server.requestLsApp(app.getId());
            }
        }.execute(this);
    }

    @Override
    public void onClick(View v) {
        requestLsAllApps();
    }

    @Subscribe
    public void onResponseLsApp(ResponseLsApp response) {
        Intent intent = new Intent(getActivity(), LsAppActivity.class);
        intent.putExtra(LsAppActivity.INTENT_APP_KEY, response.getApp());
        startActivity(intent);
        dropCheckedItem();
    }

    @Subscribe
    public void onResponseLsAllApps(ResponseLsAllApps response) {
        LsAllApps.getInstance().setApps(response.getApps());
        if (tryAgainBtn.getVisibility() == View.VISIBLE)
            tryAgainBtn.setVisibility(View.GONE);
        lsAllAppsRequested = false;
        fillListView();
        disableSwipeLayout();
        Utils.showToast(R.string.list_updated);
    }

    @Subscribe
    public void onException(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof NoNetworkException)
            Utils.showToast(R.string.no_internet);
        else
            Utils.showToast(R.string.unknown_error);
        dropCheckedItem();
        disableSwipeLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CHECKED_ITEM_POSITION, checkedItemPosition);
        outState.putBoolean(KEY_LS_ALL_APPS_REQUESTED, lsAllAppsRequested);
        outState.putBoolean(KEY_IS_SWIPE_REFRESHING, swipeLayout != null ? swipeLayout.isRefreshing() : isSwipeRefreshing);
    }

    private void dropCheckedItem() {
        listView.setItemChecked(checkedItemPosition, false);
        checkedItemPosition = -1;
    }

    private void disableSwipeLayout() {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);
            }
        });
    }
}
