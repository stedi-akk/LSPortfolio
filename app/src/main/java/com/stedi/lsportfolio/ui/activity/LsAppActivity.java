package com.stedi.lsportfolio.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.Utils;
import com.stedi.lsportfolio.model.LsApp;

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
}
