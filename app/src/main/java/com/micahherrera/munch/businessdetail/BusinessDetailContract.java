package com.micahherrera.munch.businessdetail;

import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Food;

/**
 * Created by micahherrera on 11/26/16.
 */

public interface BusinessDetailContract {

    interface View {

        void showBusinessDetails(Business business);

        void showNoBusinessDetails(String errorMessage);

    }

    interface Presenter {

        void loadBusiness(String businessId);

        void saveFood(Food food);
    }
}
