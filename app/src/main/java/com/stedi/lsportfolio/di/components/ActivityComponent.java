package com.stedi.lsportfolio.di.components;

import com.stedi.lsportfolio.di.modules.ActivityModule;
import com.stedi.lsportfolio.ui.activity.LsAppActivity;
import com.stedi.lsportfolio.ui.fragments.LsAllAppsFragment;

import dagger.Subcomponent;

@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(LsAppActivity activity);

    void inject(LsAllAppsFragment fragment);
}
