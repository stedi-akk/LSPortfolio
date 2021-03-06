package com.stedi.lsportfolio.di.components;

import com.stedi.lsportfolio.di.modules.ActivityModule;
import com.stedi.lsportfolio.di.modules.ApiModule;
import com.stedi.lsportfolio.di.modules.AppModule;
import com.stedi.lsportfolio.ui.activity.DrawerActivity;
import com.stedi.lsportfolio.ui.activity.LoadingActivity;
import com.stedi.lsportfolio.ui.fragments.ContactFragment;
import com.stedi.lsportfolio.ui.other.JobsDialog;
import com.stedi.lsportfolio.ui.other.RxDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    void inject(LoadingActivity activity);

    void inject(DrawerActivity activity);

    void inject(RxDialog.Injections injections);

    void inject(ContactFragment fragment);

    void inject(JobsDialog jobsDialog);

    ActivityComponent activityComponent(ActivityModule module);
}
