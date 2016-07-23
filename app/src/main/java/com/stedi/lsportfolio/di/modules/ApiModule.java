package com.stedi.lsportfolio.di.modules;

import com.stedi.lsportfolio.api.Api;
import com.stedi.lsportfolio.api.ApiImpl;
import com.stedi.lsportfolio.other.Utils;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class ApiModule {
    @Provides @Named("ServerUrl") String provideServerUrl() {
        return "http://www.looksoft.pl";
    }

    @Singleton @Provides OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    @Provides Api provideApi(@Named("ServerUrl") String url, OkHttpClient client, Utils utils) {
        return new ApiImpl(url, client, utils);
    }
}
