package com.example.wb_tennis_app.utils;

import android.content.Context;

import com.example.wb_tennis_app.services.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://yourapiurl.com/";
    private Retrofit retrofit;
    private Api api;

    public ApiClient(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }
}
