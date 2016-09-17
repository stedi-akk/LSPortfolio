package com.stedi.lsportfolio.ui.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.ui.activity.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactFragment extends Fragment {
    @BindView(R.id.contact_fragment_web_view) WebView webView;
    @BindView(R.id.contact_fragment_progress) View progressView;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToolbarActivity act = (ToolbarActivity) getActivity();
        act.setToolbarIcon(ToolbarActivity.ToolbarIcon.BACK);
        act.setToolbarTitle(R.string.contact);

        View root = inflater.inflate(R.layout.contact_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);

        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress >= 100) {
                    progressView.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                }
            }
        });

        loadPage();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                loadPage();
                return true;
            case R.id.action_clear_cache:
                webView.clearCache(true);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadPage() {
        progressView.setVisibility(View.VISIBLE);
        webView.setVisibility(View.INVISIBLE);
        webView.loadUrl("http://public.looksoft.pl/portfolio/kontakt.html");
    }
}
