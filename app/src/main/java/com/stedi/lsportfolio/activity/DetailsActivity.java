package com.stedi.lsportfolio.activity;

import android.os.Bundle;
import android.view.View;

import com.stedi.lsportfolio.R;

public class DetailsActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        getToolbar().setNavigationIcon(R.drawable.ic_back_icon);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
