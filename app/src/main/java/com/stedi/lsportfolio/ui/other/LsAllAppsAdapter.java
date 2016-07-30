package com.stedi.lsportfolio.ui.other;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.model.LsApp;
import com.stedi.lsportfolio.other.PicassoHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LsAllAppsAdapter extends BaseAdapter {
    private final PicassoHelper picassoHelper;

    private List<LsApp> apps = new ArrayList<>();

    @Inject
    public LsAllAppsAdapter(PicassoHelper picassoHelper) {
        this.picassoHelper = picassoHelper;
    }

    public void setApps(List<LsApp> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public LsApp getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ls_app_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        LsApp app = getItem(position);
        picassoHelper.load(app.getIconUrl(), holder.imageView);
        holder.textView.setText(app.getName());
        return convertView;
    }

    private class ViewHolder {
        private final ImageView imageView;
        private final TextView textView;

        private ViewHolder(View root) {
            imageView = (ImageView) root.findViewById(R.id.ls_app_item_iv);
            textView = (TextView) root.findViewById(R.id.ls_app_item_tv);
        }
    }
}