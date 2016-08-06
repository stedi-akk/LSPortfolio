package com.stedi.lsportfolio.di.modules;

import android.app.Activity;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @Named("ActivityContext")
    public Context provideContext() {
        return activity;
    }
}
