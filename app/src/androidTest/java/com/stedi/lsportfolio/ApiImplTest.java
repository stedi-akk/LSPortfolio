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
        try {
            ResponseLsAllApps response = api.requestLsAllApps();
            assertThat(response, is(notNullValue()));
            assertThat(response.getApps(), is(notNullValue()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testResponseLsAppExist() {
        try {
            ResponseLsApp response = api.requestLsApp(3);
            assertThat(response, is(notNullValue()));
            assertThat(response.getApp(), is(notNullValue()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = ServerException.class)
    public void testResponseLsAppNotExist() throws Exception {
        try {
            api.requestLsApp(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        fail();
    }
}
