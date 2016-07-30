package com.stedi.lsportfolio;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.lsportfolio.api.Api;
import com.stedi.lsportfolio.api.ResponseLsAllApps;
import com.stedi.lsportfolio.api.ResponseLsApp;
import com.stedi.lsportfolio.di.modules.ApiModule;
import com.stedi.lsportfolio.di.modules.AppModule;
import com.stedi.lsportfolio.other.NoNetworkException;
import com.stedi.lsportfolio.other.ServerException;
import com.stedi.lsportfolio.other.ContextUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.SocketTimeoutException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import rx.Observer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ApiImplTest {
    @Inject Api api;

    @Singleton
    @Component(modules = {AppModule.class, ApiModule.class})
    public interface TestInjector {
        void inject(ApiImplTest test);
    }

    @Test
    public void testResponseLsAllApps() {
        DaggerApiImplTest_TestInjector.builder()
                .appModule(new AppModule(App.getInstance()))
                .build().inject(this);

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
        DaggerApiImplTest_TestInjector.builder()
                .appModule(new AppModule(App.getInstance()))
                .build().inject(this);

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
        DaggerApiImplTest_TestInjector.builder()
                .appModule(new AppModule(App.getInstance()))
                .build().inject(this);

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

    @Test
    public void testTimeout() {
        ApiModule apiModule = spy(new ApiModule());
        when(apiModule.provideTimeout()).thenReturn(10L);
        DaggerApiImplTest_TestInjector.builder()
                .appModule(new AppModule(App.getInstance()))
                .apiModule(apiModule)
                .build().inject(this);

        api.requestLsAllApps().subscribe(new Observer<ResponseLsAllApps>() {
            @Override
            public void onCompleted() {
                fail();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                assertThat(e, is(instanceOf(SocketTimeoutException.class)));
            }

            @Override
            public void onNext(ResponseLsAllApps responseLsAllApps) {
                fail();
            }
        });

        verify(apiModule, times(1)).provideTimeout();
    }

    @Test
    public void testNoNetwork() throws NoNetworkException {
        ContextUtils contextUtils = mock(ContextUtils.class);
        doReturn(false).when(contextUtils).hasInternet();
        doCallRealMethod().when(contextUtils).throwOnNoNetwork();

        AppModule module = spy(new AppModule(App.getInstance()));
        when(module.provideContextUtils(any())).thenReturn(contextUtils);

        DaggerApiImplTest_TestInjector.builder()
                .appModule(module)
                .build().inject(this);

        api.requestLsAllApps().subscribe(new Observer<ResponseLsAllApps>() {
            @Override
            public void onCompleted() {
                fail();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                assertThat(e, is(instanceOf(NoNetworkException.class)));
            }

            @Override
            public void onNext(ResponseLsAllApps responseLsAllApps) {
                fail();
            }
        });

        verify(contextUtils, times(1)).throwOnNoNetwork();
        verify(module, times(1)).provideContextUtils(any());
    }
}
