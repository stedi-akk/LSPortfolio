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

public class LsAllAppsFragment extends Fragment implements AdapterView.OnItemClickListener {
    private final String KEY_CHECKED_ITEM_POSITION = "KEY_CHECKED_ITEM_POSITION";

    private ListView listView;

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
        listView = (ListView) inflater.inflate(R.layout.ls_all_apps_fragment, container, false);
        listView.setAdapter(new LsAllAppsAdapter(getActivity(), LsAllApps.getInstance().getApps()));
        if (savedInstanceState != null)
            checkedItemPosition = savedInstanceState.getInt(KEY_CHECKED_ITEM_POSITION);
        if (checkedItemPosition != -1)
            listView.setItemChecked(checkedItemPosition, true);
        listView.setOnItemClickListener(this);
        return listView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checkedItemPosition = ((ListView) parent).getCheckedItemPosition();
        final LsApp app = (LsApp) parent.getItemAtPosition(position);
        new AsyncDialog<ResponseLsApp>() {
            @Override
            protected ResponseLsApp doInBackground() throws Exception {
                Utils.throwOnNoNetwork();
                return Server.requestLsApp(app.getId());
            }
        }.execute(this);
    }

    @Subscribe
    public void onResponse(ResponseLsApp response) {
        Intent intent = new Intent(getActivity(), LsAppActivity.class);
        intent.putExtra(LsAppActivity.INTENT_APP_KEY, response.getApp());
        startActivity(intent);
        dropCheckedItem();
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
