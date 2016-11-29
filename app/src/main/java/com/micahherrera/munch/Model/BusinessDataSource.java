package com.micahherrera.munch.Model;

import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Food;

import java.util.List;

/**
 * Created by micahherrera on 11/28/16.
 */

public interface BusinessDataSource {

    interface LoadBusinessCallback {

        void onBusinessLoaded(Business business);

        void onError(String error);
    }

    interface SaveFoodCallback {

        void onFoodSaved();

        void onError(String error);
    }

    interface LoadFoodListCallback {

        void onFoodListLoaded(List<Food> foodList);

        void onError(String error);
    }



    void loadBusiness(String businessId, LoadBusinessCallback callback);

    void saveFood(Food food, SaveFoodCallback callback);

    void loadFoodList(List<Business> businessList, LoadFoodListCallback callback);


}
