package com.stedi.lsportfolio.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.di.components.ActivityComponent;
import com.stedi.lsportfolio.di.modules.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = App.getComponent()
                .activityComponent(new ActivityModule(this));
    }

    public ActivityComponent getComponent() {
        return activityComponent;
    }
}
