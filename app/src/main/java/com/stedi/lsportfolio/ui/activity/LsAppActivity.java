package com.stedi.lsportfolio.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.stedi.lsportfolio.R;

public class LsAppActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ls_app_activity);
        setToolbarIcon(ToolbarIcon.BACK);
        setToolbarIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setToolbarTitle("Details here");
    }
}
