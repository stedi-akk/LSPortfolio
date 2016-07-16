package com.stedi.lsportfolio.ui.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.model.LsApp;
import com.stedi.lsportfolio.other.Utils;

import java.util.List;

public class LsAllAppsAdapter extends ArrayAdapter<LsApp> {
    public LsAllAppsAdapter(Context context, List<LsApp> apps) {
        super(context, R.layout.ls_app_item, apps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ls_app_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        convertView.setBackgroundResource(position % 2 == 0 ?
                R.drawable.activated_on_grey : R.drawable.activated_on_white);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        LsApp app = getItem(position);
        Utils.loadWithPicasso(app.getIconUrl(), holder.imageView);
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