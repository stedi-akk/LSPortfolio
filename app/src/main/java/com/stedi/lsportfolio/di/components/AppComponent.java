package com.stedi.lsportfolio.di.components;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.stedi.lsportfolio.di.modules.ApiModule;
import com.stedi.lsportfolio.di.modules.AppModule;
import com.stedi.lsportfolio.other.ContextUtils;
import com.stedi.lsportfolio.ui.activity.LoadingActivity;
import com.stedi.lsportfolio.ui.fragments.LsAllAppsFragment;
import com.stedi.lsportfolio.ui.other.RxDialog;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    void inject(LoadingActivity activity);

    void inject(LsAllAppsFragment fragment);

    void inject(RxDialog.Injections injections);

    @Named("ApplicationContext") Context context();

    ContextUtils contextUtils();

    Picasso picasso();
}
