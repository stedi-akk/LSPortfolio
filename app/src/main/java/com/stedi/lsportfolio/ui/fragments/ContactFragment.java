package com.stedi.lsportfolio.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.ui.activity.ToolbarActivity;

public class ContactFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToolbarActivity act = (ToolbarActivity) getActivity();
        act.setToolbarIcon(ToolbarActivity.ToolbarIcon.BACK);
        act.setToolbarTitle(R.string.contact);

        return inflater.inflate(R.layout.contact_fragment, container, false);
    }
}
