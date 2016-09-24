package com.stedi.lsportfolio.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.model.LsAppDetailed;
import com.stedi.lsportfolio.model.StoreLink;
import com.stedi.lsportfolio.other.BorderTransformation;
import com.stedi.lsportfolio.other.ContextUtils;
import com.stedi.lsportfolio.other.DBG;
import com.stedi.lsportfolio.other.PicassoHelper;
import com.stedi.lsportfolio.other.StaticUtils;
import com.stedi.lsportfolio.ui.other.BlockingViewPager;
import com.stedi.lsportfolio.ui.other.LsAppScreenPagerAdapter;

import javax.inject.Inject;

import butterknife.OnClick;
import dagger.Lazy;

public class LsAppActivity extends ToolbarActivity {
    public static final String INTENT_APP_KEY = "INTENT_APP_KEY";

    private LsAppDetailed app;

    @Inject ContextUtils contextUtils;
    @Inject PicassoHelper picassoHelper;
    @Inject Lazy<LsAppScreenPagerAdapter> lazyScreensAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        app = (LsAppDetailed) getIntent().getSerializableExtra(INTENT_APP_KEY);
        if (app == null) {
            finish();
            contextUtils.showToast(R.string.unknown_error);
            return;
        }
        initLayout();
        fillMainInfo();
        fillViewPager();
        fillStoreLinks();
    }

    @OnClick(R.id.ls_app_activity_open_in_browser)
    public void onOpenInBrowserClick(View v) {
        try {
            String url = String.format("http://www.looksoft.pl/product?id=%s&lang=%s", app.getId(), StaticUtils.getSupportedLanguage());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            contextUtils.showToast(R.string.unknown_error);
        }
    }

    private void initLayout() {
        setContentView(R.layout.ls_app_activity);
        setToolbarIcon(ToolbarIcon.BACK);
        setToolbarIconListener(v -> finish());
        setToolbarTitle(app.getName());
    }

    private void fillMainInfo() {
        picassoHelper.load(app.getIconUrl(), (ImageView) findViewById(R.id.ls_app_activity_collapsing_layout_image));
        CollapsingToolbarLayout collapsingLayout = (CollapsingToolbarLayout) findViewById(R.id.ls_app_activity_collapsing_layout);
        collapsingLayout.setTitle(app.getName());
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.ls_app_activity_appbarlayout);
        View toolbarShadow = findViewById(R.id.ls_app_activity_toolbar_shadow);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout1.getTotalScrollRange() && toolbarShadow.getVisibility() != View.VISIBLE)
                toolbarShadow.setVisibility(View.VISIBLE);
            else if (toolbarShadow.getVisibility() != View.INVISIBLE)
                toolbarShadow.setVisibility(View.INVISIBLE);
        });
        ((TextView) findViewById(R.id.ls_app_activity_full_description)).setText(app.getFullDescription());
    }

    private void fillViewPager() {
        int lsAppScreensCount = app.getGalleryUrls().size();
        if (lsAppScreensCount == 0)
            return;

        View pagerDivider = findViewById(R.id.ls_app_activity_pager_divider);
        BlockingViewPager pager = (BlockingViewPager) findViewById(R.id.ls_app_activity_pager);

        // should be based on all images, not just on first :/
        Target firstImageTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                pagerDivider.setVisibility(View.VISIBLE);
                pager.setVisibility(View.VISIBLE);

                float pageMargin = contextUtils.dp2px(24); // space between images
                pager.setPageMargin((int) pageMargin);

                float leftPadding = pager.getPaddingLeft();
                float rightPadding = pager.getPaddingRight();

                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                float pagerContentWidht = screenWidth - leftPadding - rightPadding;

                float lsAppScreenHeight = getResources().getDimensionPixelSize(R.dimen.ls_app_screen_height);
                float lsAppScreenWidth = (bitmap.getWidth() * 1f / bitmap.getHeight()) * lsAppScreenHeight;
                if (lsAppScreenWidth > pagerContentWidht)
                    lsAppScreenWidth = pagerContentWidht;

                float pageWidth = 1 / (pagerContentWidht / lsAppScreenWidth); // adapter getPageWidth()

                float fullPagerWidth = (lsAppScreensCount * lsAppScreenWidth) +
                        leftPadding + rightPadding +
                        ((lsAppScreensCount - 1) * pageMargin);

                if (fullPagerWidth <= screenWidth)
                    pager.disableSwipe(true);

                pager.setOffscreenPageLimit((int) (3 / pageWidth));

                LsAppScreenPagerAdapter adapter = lazyScreensAdapter.get();
                adapter.setImgUrls(app.getGalleryUrls());
                adapter.setTransformation(new BorderTransformation(Color.LTGRAY, contextUtils.dp2px(1)));
                adapter.setPageWidth(pageWidth);

                pager.setAdapter(adapter);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                pagerDivider.setVisibility(View.GONE);
                pager.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                pagerDivider.setVisibility(View.GONE);
                pager.setVisibility(View.GONE);
            }
        };

        pager.setTag(firstImageTarget);
        picassoHelper.getPicasso().load(app.getGalleryUrls().get(0)).into(firstImageTarget);
    }

    private void fillStoreLinks() {
        if (app.getStoreLinks().size() == 0)
            return;

        boolean isSw600Dp = contextUtils.isSw600dp();
        int margin = (int) contextUtils.dp2px(24); // space between buttons

        LinearLayout container = (LinearLayout) findViewById(R.id.ls_app_activity_stores_container);
        container.setVisibility(View.VISIBLE);
        container.setOrientation(isSw600Dp ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);

        for (int i = 0; i < app.getStoreLinks().size(); i++) {
            StoreLink link = app.getStoreLinks().get(i);
            if (link.getType() == null)
                continue;

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(link.getType().getIconResId());
            imageView.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link.getUrl())));
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                    contextUtils.showToast(R.string.unknown_error);
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
