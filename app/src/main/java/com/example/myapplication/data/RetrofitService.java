package com.example.myapplication.data;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitService {
    private static final String BASE_URL = "http://192.168.0.14:8080";
    private static volatile Retrofit retrofit;

    public static Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public static Retrofit getInstance() {
        if (retrofit == null) retrofit = create();
        return retrofit;
    }
}
