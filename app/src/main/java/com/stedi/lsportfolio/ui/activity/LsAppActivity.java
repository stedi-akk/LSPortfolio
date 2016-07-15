package com.stedi.lsportfolio.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.Utils;
import com.stedi.lsportfolio.model.LsApp;
import com.stedi.lsportfolio.ui.other.BlockingViewPager;
import com.stedi.lsportfolio.ui.other.LsAppScreenPagerAdapter;

public class LsAppActivity extends ToolbarActivity {
    public static final String INTENT_APP_KEY = "INTENT_APP_KEY";

    private LsApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (LsApp) getIntent().getSerializableExtra(INTENT_APP_KEY);
        if (app == null) {
            finish();
            Utils.showToast("fail here");
        }

        initLayout();
        fillMainInfo();
        initViewPager();
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

    private void initViewPager() {
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
}
