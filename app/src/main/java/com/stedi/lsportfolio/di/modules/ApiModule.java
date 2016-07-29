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
    @Provides
    @Named("ServerUrl")
    public String provideServerUrl() {
        return "http://www.looksoft.pl";
    }

    @Provides
    @Named("Timeout")
    public long provideTimeout() {
        return TimeUnit.SECONDS.toMillis(10);
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(@Named("Timeout") long timeout) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    public Api provideApi(@Named("ServerUrl") String url, OkHttpClient client, Utils utils) {
        return new ApiImpl(url, client, utils);
    }
}
