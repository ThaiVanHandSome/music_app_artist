package com.example.music_app_artist.retrofit;

import android.content.Context;
import android.icu.text.UnicodeSet;

import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.utils.Const;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS).build();

    public static Retrofit getRetrofit() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.110.122:8989/api/v1/")
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", " Bearer " + Const.getAccessToken())
                    .build();
            return chain.proceed(newRequest);
        }).build();
        return client;
    }
}
