package com.micahherrera.munch.businessdetail;

import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.Model.data.Review;

import java.util.List;

/**
 * Created by micahherrera on 11/26/16.
 */

public interface BusinessDetailContract {

    interface View {

        void showBusinessDetails(Business business, List<Food> foodList, List<Review> reviewList);

        void showNoBusinessDetails(String errorMessage);

        void showNoFoodListDetails(String errorMessage);

        void showNoReviewListDetails(String errorMessage);

    }

    interface Presenter {

        void loadBusiness(String businessId);

        void saveFood(Food food);
    }
}
