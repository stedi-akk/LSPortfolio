package com.stedi.lsportfolio.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.ui.activity.ToolbarActivity;

public class ContactFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToolbarActivity act = (ToolbarActivity) getActivity();
        act.setToolbarIcon(ToolbarActivity.ToolbarIcon.BACK);
        act.setToolbarTitle(R.string.contact);

        View root = inflater.inflate(R.layout.contact_fragment, container, false);
        WebView webView = (WebView) root.findViewById(R.id.contact_fragment_web_view);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setJavaScriptEnabled(true);

        final View progressView = root.findViewById(R.id.contact_fragment_progress);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress >= 100)
                    progressView.setVisibility(View.GONE);
            }
        });

        webView.loadUrl("http://public.looksoft.pl/portfolio/kontakt.html");
        return root;
    }
}
