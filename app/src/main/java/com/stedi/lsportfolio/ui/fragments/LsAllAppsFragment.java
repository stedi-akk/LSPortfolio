package com.stedi.lsportfolio.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class LsAllAppsFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final String KEY_CHECKED_ITEM_POSITION = "KEY_CHECKED_ITEM_POSITION";
    private final String KEY_LS_ALL_APPS_REQUESTED = "KEY_LS_ALL_APPS_REQUESTED";

    private ListView listView;
    private View emptyView;
    private View tryAgainBtn;

    private boolean lsAllAppsRequested;
    private int checkedItemPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getBus().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToolbarActivity act = (ToolbarActivity) getActivity();
        act.setToolbarIcon(ToolbarActivity.ToolbarIcon.DRAWER);
        act.setToolbarTitle(R.string.apps);

        if (savedInstanceState != null) {
            checkedItemPosition = savedInstanceState.getInt(KEY_CHECKED_ITEM_POSITION);
            lsAllAppsRequested = savedInstanceState.getBoolean(KEY_LS_ALL_APPS_REQUESTED);
        }

        View root = inflater.inflate(R.layout.ls_all_apps_fragment, container, false);
        listView = (ListView) root.findViewById(R.id.ls_all_apps_list);
        emptyView = root.findViewById(R.id.ls_all_apps_empty_view);
        tryAgainBtn = root.findViewById(R.id.ls_all_apps_try_again);
        tryAgainBtn.setOnClickListener(this);

        if (LsAllApps.getInstance().getApps() == null) {
            tryAgainBtn.setVisibility(View.VISIBLE);
            if (!lsAllAppsRequested) {
                lsAllAppsRequested = true;
                requestLsAllApps();
            }
        } else {
            fillListView();
        }

        return root;
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
        listView.setEmptyView(emptyView);
        listView.setAdapter(new LsAllAppsAdapter(getActivity(), LsAllApps.getInstance().getApps()));
        if (checkedItemPosition != -1)
            listView.setItemChecked(checkedItemPosition, true);
        listView.setOnItemClickListener(this);
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
        tryAgainBtn.setVisibility(View.GONE);
        lsAllAppsRequested = false;
        fillListView();
    }

    @Subscribe
    public void onException(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof NoNetworkException)
            Utils.showToast(R.string.no_internet);
        else
            Utils.showToast(R.string.unknown_error);
        dropCheckedItem();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CHECKED_ITEM_POSITION, checkedItemPosition);
        outState.putBoolean(KEY_LS_ALL_APPS_REQUESTED, lsAllAppsRequested);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getBus().unregister(this);
    }

    private void dropCheckedItem() {
        listView.setItemChecked(checkedItemPosition, false);
        checkedItemPosition = -1;
    }
}
