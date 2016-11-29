package com.micahherrera.munch.Model;

import com.micahherrera.munch.Model.contract.YelpApi3;
import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by micahherrera on 11/28/16.
 */

public class DataRepository implements BusinessDataSource {

    @Override
    public void loadBusiness(String businessId, final LoadBusinessCallback callback) {
        YelpApi3 yelp = RetrofitFactoryYelp.getInstance();
        Call<Business> call = yelp.getBusiness(businessId, "Bearer " + RetrofitFactoryYelp.getToken());
        call.enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                callback.onBusinessLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });

    }

    @Override
    public void saveFood(Food food, SaveFoodCallback callback) {

    }

    @Override
    public void loadFoodList(List<Business> businessList, LoadFoodListCallback callback) {
        new DownloadUrlTask(callback).execute(businessList);
    }
}
