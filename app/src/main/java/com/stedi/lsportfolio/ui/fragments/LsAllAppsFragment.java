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

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.model.LsAllApps;
import com.stedi.lsportfolio.ui.activity.LsAppActivity;
import com.stedi.lsportfolio.ui.activity.ToolbarActivity;
import com.stedi.lsportfolio.ui.adapters.LsAllAppsAdapter;

public class LsAllAppsFragment extends Fragment implements AdapterView.OnItemClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToolbarActivity act = (ToolbarActivity) getActivity();
        act.setToolbarIcon(ToolbarActivity.ToolbarIcon.DRAWER);
        act.setToolbarTitle(R.string.apps);

        ListView listView = (ListView) inflater.inflate(R.layout.ls_all_apps_fragment, container, false);
        listView.setAdapter(new LsAllAppsAdapter(getActivity(), LsAllApps.getInstance().getApps()));
        listView.setOnItemClickListener(this);
        return listView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), LsAppActivity.class));
    }
}
