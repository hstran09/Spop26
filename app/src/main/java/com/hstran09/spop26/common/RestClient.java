package com.hstran09.spop26.common;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        Interceptor interceptor = chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("X-ListenAPI-Key", "3dca490b5158420192766b3931a02477")
                    .build();
            return chain.proceed(request);
        };
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Setting.RestClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }
}
