package com.stedi.lsportfolio.di.components;

import com.stedi.lsportfolio.di.PerActivity;
import com.stedi.lsportfolio.di.modules.ActivityModule;
import com.stedi.lsportfolio.ui.activity.LsAppActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(LsAppActivity activity);
}
