package com.stedi.lsportfolio.ui.other;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.model.LsApp;
import com.stedi.lsportfolio.other.PicassoHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LsAllAppsAdapter extends RecyclerView.Adapter<LsAllAppsAdapter.Holder> {
    private final PicassoHelper picassoHelper;

    private List<LsApp> apps = new ArrayList<>();
    private View.OnClickListener listener;

    @Inject
    public LsAllAppsAdapter(PicassoHelper picassoHelper) {
        this.picassoHelper = picassoHelper;
    }

    public void setApps(List<LsApp> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public LsAllAppsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ls_app_item, parent, false);
        if (listener != null)
            v.setOnClickListener(listener);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        LsApp app = getItem(position);
        picassoHelper.load(app.getIconUrl(), holder.imageView);
        holder.textView.setText(app.getName());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public LsApp getItem(int position) {
        return apps.get(position);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;

        private Holder(View root) {
            super(root);
            imageView = (ImageView) root.findViewById(R.id.ls_app_item_iv);
            textView = (TextView) root.findViewById(R.id.ls_app_item_tv);
        }
    }
}