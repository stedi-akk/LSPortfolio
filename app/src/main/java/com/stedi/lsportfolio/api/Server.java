package com.stedi.lsportfolio.api;

import com.stedi.lsportfolio.other.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class Server {
    private static final String URL = "http://www.looksoft.pl";
    private static final int TIMEOUT_SEC = 10;

    private interface RequestLsAllApps {
        @GET("/api/main")
        Call<ResponseLsAllApps> get();
    }

    private interface RequestLsApp {
        @GET("/api/product/{id}")
        Call<ResponseLsApp> get(@Path("id") long id);
    }

    public static ResponseLsAllApps requestLsAllApps() throws Exception {
        Utils.throwOnNoNetwork();
        Call<ResponseLsAllApps> call = Server.with(RequestLsAllApps.class).get();
        return call.execute().body();
    }

    public static ResponseLsApp requestLsApp(long id) throws Exception {
        Utils.throwOnNoNetwork();
        Call<ResponseLsApp> call = Server.with(RequestLsApp.class).get(id);
        return call.execute().body();
    }

    private static <T> T with(Class<T> service) {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(newHttpClient())
                .build().create(service);
    }

    private static OkHttpClient newHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }
}
