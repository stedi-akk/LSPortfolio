package com.stedi.lsportfolio.ui.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
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
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LsAllAppsAdapter extends RecyclerView.Adapter<LsAllAppsAdapter.Holder> {
    private final StyleSpan highlightSpan = new StyleSpan(android.graphics.Typeface.BOLD);
    private final Context context;
    private final PicassoHelper picassoHelper;

    private List<LsApp> apps = new ArrayList<>();
    private View.OnClickListener listener;

    private String highlightQuery;

    @Inject
    public LsAllAppsAdapter(@Named("ActivityContext") Context context, PicassoHelper picassoHelper) {
        this.context = context;
        this.picassoHelper = picassoHelper;
    }

    public void setApps(List<LsApp> apps) {
        setApps(apps, null);
    }

    public void setApps(List<LsApp> apps, String highlightQuery) {
        this.apps = apps;
        this.highlightQuery = highlightQuery;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public LsAllAppsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ls_app_item, parent, false);
        if (listener != null)
            v.setOnClickListener(listener);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        LsApp app = getItem(position);
        picassoHelper.load(app.getIconUrl(), holder.imageView);
        holder.textView.setText(highlightQuery != null ? highlightQuery(app.getName()) : app.getName());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public LsApp getItem(int position) {
        return apps.get(position);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.ls_app_item_iv) ImageView imageView;
        @BindView(R.id.ls_app_item_tv) TextView textView;

        private Holder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }

    private CharSequence highlightQuery(String what) {
        int indexInWhat = what.toLowerCase().indexOf(highlightQuery.toLowerCase());
        Spannable text = new SpannableString(what);
        if (indexInWhat > -1)
            text.setSpan(highlightSpan, indexInWhat, indexInWhat + highlightQuery.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return text;
    }
}