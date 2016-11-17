package com.micahherrera.munch.Model;

import com.micahherrera.munch.Model.contract.YelpApi3;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by micahherrera on 10/28/16.
 */

public class RetrofitFactoryYelp {
    public static String BASE_URL = "https://api.yelp.com";
    public static YelpApi3 service;
    private OkHttpClient httpClient;

    public static YelpApi3 getInstance(){
        if(service == null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(YelpApi3.class);
            return service;
        } else {
            return service;
        }
    }
}

