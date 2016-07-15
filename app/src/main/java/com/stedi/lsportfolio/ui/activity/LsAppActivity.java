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

import com.squareup.picasso.Picasso;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.Utils;
import com.stedi.lsportfolio.model.LsAppDetailed;
import com.stedi.lsportfolio.model.StoreLink;
import com.stedi.lsportfolio.ui.other.BlockingViewPager;
import com.stedi.lsportfolio.ui.other.LsAppScreenPagerAdapter;

public class LsAppActivity extends ToolbarActivity {
    public static final String INTENT_APP_KEY = "INTENT_APP_KEY";

    private LsAppDetailed app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (LsAppDetailed) getIntent().getSerializableExtra(INTENT_APP_KEY);
        if (app == null) {
            finish();
            Utils.showToast("fail here");
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
        Picasso.with(this).load(app.getIconUrl()).into((ImageView) findViewById(R.id.ls_app_activity_icon));
        ((TextView) findViewById(R.id.ls_app_activity_name)).setText(app.getName());
        ((TextView) findViewById(R.id.ls_app_activity_description)).setText(app.getDescription());
    }

    private void fillViewPager() {
        BlockingViewPager pager = (BlockingViewPager) findViewById(R.id.ls_app_activity_pager);

        float pageMargin = Utils.dp2px(24); // space between images
        float pagerMargins = Utils.dp2px(48); // left and right margin
        pager.setPageMargin((int) pageMargin);

        int lsAppScreenWidth = getResources().getDimensionPixelSize(R.dimen.ls_app_screen_width); // image width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        float pageWidth = 1 / ((screenWidth - pagerMargins - pagerMargins) / lsAppScreenWidth); // for adapter getPageWidth()

        int lsAppScreensCount = app.getGalleryUrls().size();
        float fullPagerWidth = (lsAppScreensCount * lsAppScreenWidth) +
                pagerMargins + pagerMargins +
                ((lsAppScreensCount - 1) * pageMargin);

        if (fullPagerWidth <= screenWidth)
            pager.disableSwipe(true);

        pager.setOffscreenPageLimit((int) (3 / pageWidth));
        pager.setAdapter(new LsAppScreenPagerAdapter(app.getGalleryUrls(), pageWidth));
    }

    private void fillStoreLinks() {
        LinearLayout container = (LinearLayout) findViewById(R.id.ls_app_activity_stores_container);
        for (final StoreLink link : app.getStoreLinks()) {
            ImageView imageView = new ImageView(this);
            if (link.getType() == null)
                continue;
            imageView.setImageResource(link.getType().getIconResId());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link.getUrl())));
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = (int) Utils.dp2px(24);
            imageView.setLayoutParams(params);
            container.addView(imageView);
        }
    }
}
