package com.stedi.lsportfolio.api;

import com.stedi.lsportfolio.other.Utils;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public class ApiImpl implements Api {
    private final String url;
    private final OkHttpClient client;
    private final Utils utils;

    private interface OnRequest<T> {
        Call<T> getCall();
    }

    public ApiImpl(String url, OkHttpClient client, Utils utils) {
        this.url = url;
        this.client = client;
        this.utils = utils;
    }

    private interface RequestLsAllApps {
        @GET("/api/main")
        Call<ResponseLsAllApps> get();
    }

    private interface RequestLsApp {
        @GET("/api/product/{id}")
        Call<ResponseLsApp> get(@Path("id") long id);
    }

    @Override
    public Observable<ResponseLsAllApps> requestLsAllApps() {
        return request(() -> createCall(RequestLsAllApps.class).get());
    }

    @Override
    public Observable<ResponseLsApp> requestLsApp(long id) {
        return request(() -> createCall(RequestLsApp.class).get(id));
    }

    private <T extends BaseResponse> Observable<T> request(OnRequest<T> onRequest) {
        return Observable.create(subscriber -> {
            try {
                utils.throwOnNoNetwork();
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
