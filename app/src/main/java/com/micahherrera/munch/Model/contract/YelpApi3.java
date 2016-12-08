package com.micahherrera.munch.Model.contract;

import com.micahherrera.munch.Model.data.AutoComplete;
import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Reviews;
import com.micahherrera.munch.Model.data.SearchYelpResponse;
import com.micahherrera.munch.Model.data.TokenO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by micahherrera on 10/28/16.
 */

public interface YelpApi3 {

    @POST("/oauth2/token")
    Call<TokenO> oauth(@QueryMap Map<String, String> oauthParams);

    @GET("/v3/businesses/search")
    Call<SearchYelpResponse> search(@QueryMap Map<String, String> params,
                                    @Header("Authorization") String token);

    @GET("/v3/businesses/{id}")
    Call<Business> getBusiness(@Path("id") String businessId,
                               @Header("Authorization") String token);

    @GET("v3/autocomplete")
    Call<AutoComplete> autoComplete(@QueryMap Map<String, String> params,
                                    @Header("Authorization") String token);

    @GET("v3/businesses/{id}/reviews")
    Call<Reviews> getReviews(@Path("id") String businessId,
                             @Header("Authorization") String token);

}
