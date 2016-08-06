package com.stedi.lsportfolio.di.components;

import com.stedi.lsportfolio.di.modules.ActivityModule;
import com.stedi.lsportfolio.di.modules.ApiModule;
import com.stedi.lsportfolio.di.modules.AppModule;
import com.stedi.lsportfolio.ui.activity.LoadingActivity;
import com.stedi.lsportfolio.ui.other.RxDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    void inject(LoadingActivity activity);

    void inject(RxDialog.Injections injections);

    ActivityComponent activityComponent(ActivityModule module);
}
