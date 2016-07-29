package com.stedi.lsportfolio;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.lsportfolio.api.Api;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.api.ResponseLsApp;
import com.stedi.lsportfolio.di.modules.ApiModule;
import com.stedi.lsportfolio.di.modules.AppModule;
import com.stedi.lsportfolio.other.ServerException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import rx.Observer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class ApiImplTest {
    @Inject Api api;

    @Singleton
    @Component(modules = {AppModule.class, ApiModule.class})
    public interface TestInjector {
        void inject(ApiImplTest test);
    }

    @Before
    public void before() {
        DaggerApiImplTest_TestInjector.builder()
                .appModule(new AppModule(App.getInstance()))
                .build().inject(this);
    }

    @Test
    public void testResponseLsAllApps() {
        api.requestLsAllApps()
                .subscribe(new Observer<ResponseLsAllApps>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        fail();
                    }

                    @Override
                    public void onNext(ResponseLsAllApps response) {
                        assertThat(response, is(notNullValue()));
                        assertThat(response.getApps(), is(notNullValue()));
                    }
                });
    }

    @Test
    public void testResponseLsAppExist() {
        api.requestLsApp(3)
                .subscribe(new Observer<ResponseLsApp>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        fail();
                    }

                    @Override
                    public void onNext(ResponseLsApp response) {
                        assertThat(response, is(notNullValue()));
                        assertThat(response.getApp(), is(notNullValue()));
                    }
                });
    }

    @Test
    public void testResponseLsAppNotExist() {
        api.requestLsApp(1)
                .subscribe(new Observer<ResponseLsApp>() {
                    @Override
                    public void onCompleted() {
                        fail();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        assertThat(e, is(instanceOf(ServerException.class)));
                    }

                    @Override
                    public void onNext(ResponseLsApp response) {
                        fail();
                    }
                });
    }
}
