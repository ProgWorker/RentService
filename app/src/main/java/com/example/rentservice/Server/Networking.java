package com.example.rentservice.Server;

import com.example.rentservice.Server.Endpoints.JSONPlaceHolderApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Networking {
    private static Networking mInstance;
    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private Retrofit mRetrofit;

    private Networking() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Networking getInstance() {
        if (mInstance == null) {
            mInstance = new Networking();
        }
        return mInstance;
    }

    public JSONPlaceHolderApi getJSONApi() {
        return mRetrofit.create(JSONPlaceHolderApi.class);
    }
}
