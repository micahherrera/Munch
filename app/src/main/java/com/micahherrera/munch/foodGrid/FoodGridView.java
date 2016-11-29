package com.micahherrera.munch.foodgrid;

import android.os.Bundle;

import com.micahherrera.munch.Model.data.Food;

import java.util.List;

/**
 * Created by micahherrera on 11/16/16.
 */

public interface FoodGridView {
    void renderFoods(List<Food> foodList);
    void navigateToBusiness(Food food);
    boolean checkConnection();
    void unregister();
    void checkPermissions();
    void onPermissionGranted();
    void setupBroadcastReceiver();
    void setTextError(String error);
    void getLocation();
    void saveToken(String token);
    String getToken();
    void goToSettings(Bundle bundle);
}
