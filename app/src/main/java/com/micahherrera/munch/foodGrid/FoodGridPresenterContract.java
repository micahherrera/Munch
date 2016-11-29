package com.micahherrera.munch.foodgrid;

import android.os.Bundle;

import com.micahherrera.munch.Model.data.Food;

import java.util.List;

/**
 * Created by micahherrera on 11/11/16.
 */

public interface FoodGridPresenterContract {
    void getNearby();
    void setupYelp();
    void oauthYelp();
    void getCoordinates(Bundle bundle);
    void goToSettings();
    int getSettingsReturn();
    void newSettings(Bundle bundle);
    void onFoodLoaded(List<Food> foodList);
}
