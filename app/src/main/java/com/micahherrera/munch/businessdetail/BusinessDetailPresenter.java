package com.micahherrera.munch.businessdetail;

import com.micahherrera.munch.Model.data.Food;

/**
 * Created by micahherrera on 11/27/16.
 */

public class BusinessDetailPresenter implements BusinessDetailContract.Presenter {

    private BusinessDetailContract.View mView;

    public BusinessDetailPresenter (BusinessDetailContract.View view){
        mView = view;
    }

    @Override
    public void loadBusiness(String businessId) {

    }

    @Override
    public void saveFood(Food food) {

    }
}
