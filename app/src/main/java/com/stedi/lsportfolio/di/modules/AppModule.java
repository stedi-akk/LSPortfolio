package com.stedi.lsportfolio.di.modules;

import android.content.Context;

import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.other.ContextUtils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Named("ApplicationContext")
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Singleton
    @Provides
    public Bus provideBus() {
        return new Bus();
    }

    @Provides
    public Picasso providePicasso(@Named("ApplicationContext") Context context) {
        return Picasso.with(context);
    }

    @Singleton
    @Provides
    public ContextUtils provideContextUtils(@Named("ApplicationContext") Context context) {
        return new ContextUtils(context);
    }
}
