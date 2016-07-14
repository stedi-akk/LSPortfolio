package com.stedi.lsportfolio;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AppsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(R.layout.apps_fragment, container, false);
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add("Item " + (i + 1));
        }
        listView.setAdapter(new AppsAdapter(getActivity(), items));
        return listView;
    }

    private class AppsAdapter extends ArrayAdapter<String> {
        public AppsAdapter(Context context, ArrayList<String> data) {
            super(context, R.layout.apps_fragment_item, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.apps_fragment_item, parent, false);
                ViewHolder holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            convertView.setBackgroundResource(position % 2 == 0 ? R.drawable.default_selector_on_grey : R.drawable.default_selector_on_white);
            return convertView;
        }

        private class ViewHolder {
            private final ImageView imageView;
            private final TextView textView;

            private ViewHolder(View root) {
                imageView = (ImageView) root.findViewById(R.id.apps_fragment_iv);
                textView = (TextView) root.findViewById(R.id.apps_fragment_tv);
            }
        }
    }
}
