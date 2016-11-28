package com.micahherrera.munch.foodgrid;

import com.micahherrera.munch.Model.data.Food;

import java.util.List;

/**
 * Created by micahherrera on 11/16/16.
 */

public interface FoodGridView {
    void renderFoods(List<Food> foodList);
    void navigateToBusiness(Food food);
}
