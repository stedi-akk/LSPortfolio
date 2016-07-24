package com.stedi.lsportfolio.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.model.LsAppDetailed;
import com.stedi.lsportfolio.model.StoreLink;
import com.stedi.lsportfolio.other.Utils;
import com.stedi.lsportfolio.ui.other.BlockingViewPager;
import com.stedi.lsportfolio.ui.other.LsAppScreenPagerAdapter;

import javax.inject.Inject;

import dagger.Lazy;

public class LsAppActivity extends ToolbarActivity {
    public static final String INTENT_APP_KEY = "INTENT_APP_KEY";

    private LsAppDetailed app;

    @Inject Utils utils;
    @Inject Lazy<LsAppScreenPagerAdapter> lazyScreensAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInjector().inject(this);
        app = (LsAppDetailed) getIntent().getSerializableExtra(INTENT_APP_KEY);
        if (app == null) {
            finish();
            utils.showToast(R.string.unknown_error);
            return;
        }
        initLayout();
        fillMainInfo();
        fillViewPager();
        fillStoreLinks();
    }

    private void initLayout() {
        setContentView(R.layout.ls_app_activity);
        setToolbarIcon(ToolbarIcon.BACK);
        setToolbarIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setToolbarTitle(app.getName());
    }

    private void fillMainInfo() {
        utils.loadWithPicasso(app.getIconUrl(), (ImageView) findViewById(R.id.ls_app_activity_icon));
        ((TextView) findViewById(R.id.ls_app_activity_name)).setText(app.getName());
        ((TextView) findViewById(R.id.ls_app_activity_description)).setText(app.getDescription());
    }

    private void fillViewPager() {
        int lsAppScreensCount = app.getGalleryUrls().size();
        if (lsAppScreensCount == 0)
            return;

        BlockingViewPager pager = (BlockingViewPager) findViewById(R.id.ls_app_activity_pager);
        pager.setVisibility(View.VISIBLE);

        float pageMargin = utils.dp2px(24); // space between images
        pager.setPageMargin((int) pageMargin);

        float leftPadding = pager.getPaddingLeft();
        float rightPadding = pager.getPaddingRight();

        int lsAppScreenWidth = getResources().getDimensionPixelSize(R.dimen.ls_app_screen_width); // image width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        float pageWidth = 1 / ((screenWidth - leftPadding - rightPadding) / lsAppScreenWidth); // adapter getPageWidth()

        float fullPagerWidth = (lsAppScreensCount * lsAppScreenWidth) +
                leftPadding + rightPadding +
                ((lsAppScreensCount - 1) * pageMargin);

        if (fullPagerWidth <= screenWidth)
            pager.disableSwipe(true);

        pager.setOffscreenPageLimit((int) (3 / pageWidth));

        LsAppScreenPagerAdapter adapter = lazyScreensAdapter.get();
        adapter.setImgUrls(app.getGalleryUrls());
        adapter.setPageWidth(pageWidth);

        pager.setAdapter(adapter);
    }

    private void fillStoreLinks() {
        if (app.getStoreLinks().size() == 0)
            return;

        boolean isSw600Dp = utils.isSw600dp();
        int margin = (int) utils.dp2px(24); // space between buttons

        findViewById(R.id.ls_app_activity_bottom_container).setVisibility(View.VISIBLE);
        LinearLayout container = (LinearLayout) findViewById(R.id.ls_app_activity_stores_container);
        container.setOrientation(isSw600Dp ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);

        for (int i = 0; i < app.getStoreLinks().size(); i++) {
            final StoreLink link = app.getStoreLinks().get(i);
            if (link.getType() == null)
                continue;

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(link.getType().getIconResId());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link.getUrl())));
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                        utils.showToast(R.string.unknown_error);
                    }
                }
            });

            if (i < app.getStoreLinks().size() - 1) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (isSw600Dp)
                    params.rightMargin = margin;
                else
                    params.bottomMargin = margin;
                imageView.setLayoutParams(params);
            }

            container.addView(imageView);
        }
    }
}
