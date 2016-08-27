package com.stedi.lsportfolio.api;

import com.stedi.lsportfolio.other.ContextUtils;
import com.stedi.lsportfolio.other.StaticUtils;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public class ApiImpl implements Api {
    private final String url;
    private final OkHttpClient client;
    private final ContextUtils contextUtils;

    private interface OnRequest<T> {
        Call<T> getCall();
    }

    public ApiImpl(String url, OkHttpClient client, ContextUtils contextUtils) {
        this.url = url;
        this.client = client;
        this.contextUtils = contextUtils;
    }

    private interface RequestLsAllApps {
        @GET("/api/main")
        Call<ResponseLsAllApps> get(@Query("lang") String lang);
    }

    private interface RequestLsApp {
        @GET("/api/product/{id}")
        Call<ResponseLsApp> get(@Path("id") long id, @Query("lang") String lang);
    }

    @Override
    public Observable<ResponseLsAllApps> requestLsAllApps() {
        return request(() -> createCall(RequestLsAllApps.class).get(StaticUtils.getSupportedLanguage()));
    }

    @Override
    public Observable<ResponseLsApp> requestLsApp(long id) {
        return request(() -> createCall(RequestLsApp.class).get(id, StaticUtils.getSupportedLanguage()));
    }

    private <T extends BaseResponse> Observable<T> request(OnRequest<T> onRequest) {
        return Observable.create(subscriber -> {
            try {
                contextUtils.throwOnNoNetwork();
                T t = onRequest.getCall().execute().body().validate();
                subscriber.onNext(t);
                subscriber.onCompleted();
            } catch (Throwable throwable) {
                subscriber.onError(throwable);
            }
        });
    }

    private <T> T createCall(Class<T> service) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(service);
    }
}
