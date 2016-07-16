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

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.model.LsAppDetailed;
import com.stedi.lsportfolio.model.StoreLink;
import com.stedi.lsportfolio.other.Utils;
import com.stedi.lsportfolio.ui.other.BlockingViewPager;
import com.stedi.lsportfolio.ui.other.LsAppScreenPagerAdapter;

// TODO screens borders
// TODO finish
// TODO store icons on tablets gravity
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
        Utils.loadWithPicasso(app.getIconUrl(), (ImageView) findViewById(R.id.ls_app_activity_icon));
        ((TextView) findViewById(R.id.ls_app_activity_name)).setText(app.getName());
        ((TextView) findViewById(R.id.ls_app_activity_description)).setText(app.getDescription());
    }

    private void fillViewPager() {
        int lsAppScreensCount = app.getGalleryUrls().size();
        if (lsAppScreensCount == 0)
            return;

        BlockingViewPager pager = (BlockingViewPager) findViewById(R.id.ls_app_activity_pager);
        pager.setVisibility(View.VISIBLE);

        float pageMargin = Utils.dp2px(24); // space between images
        pager.setPageMargin((int) pageMargin);

        float leftPadding = pager.getPaddingLeft();
        float rightPadding = pager.getPaddingRight();

        int lsAppScreenWidth = getResources().getDimensionPixelSize(R.dimen.ls_app_screen_width); // image width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        float pageWidth = 1 / ((screenWidth - leftPadding - rightPadding) / lsAppScreenWidth); // for adapter getPageWidth()

        float fullPagerWidth = (lsAppScreensCount * lsAppScreenWidth) +
                leftPadding + rightPadding +
                ((lsAppScreensCount - 1) * pageMargin);

        if (fullPagerWidth <= screenWidth)
            pager.disableSwipe(true);

        pager.setOffscreenPageLimit((int) (3 / pageWidth));
        pager.setAdapter(new LsAppScreenPagerAdapter(app.getGalleryUrls(), pageWidth));
    }

    private void fillStoreLinks() {
        if (app.getStoreLinks().size() == 0)
            return;
        boolean isSw600Dp = Utils.isSw600dp();
        int margin = (int) Utils.dp2px(24);
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
