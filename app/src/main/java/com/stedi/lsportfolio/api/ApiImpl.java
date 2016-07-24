package com.stedi.lsportfolio.api;

import com.stedi.lsportfolio.other.Utils;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ApiImpl implements Api {
    private final String url;
    private final OkHttpClient client;
    private final Utils utils;

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
    public ResponseLsAllApps requestLsAllApps() throws Exception {
        utils.throwOnNoNetwork();
        Call<ResponseLsAllApps> call = createCall(RequestLsAllApps.class).get();
        return call.execute().body().validate();
    }

    @Override
    public ResponseLsApp requestLsApp(long id) throws Exception {
        utils.throwOnNoNetwork();
        Call<ResponseLsApp> call = createCall(RequestLsApp.class).get(id);
        return call.execute().body().validate();
    }

    private <T> T createCall(Class<T> service) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(service);
    }
}
