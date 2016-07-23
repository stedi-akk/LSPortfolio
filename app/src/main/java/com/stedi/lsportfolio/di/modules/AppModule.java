package com.stedi.lsportfolio.di.modules;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Singleton @Provides Bus provideBus() {
        return new Bus();
    }
}
