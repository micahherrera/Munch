package com.micahherrera.munch.businessdetail;

import com.micahherrera.munch.Model.BusinessDataSource;
import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micahherrera on 11/27/16.
 */

public class BusinessDetailPresenter implements BusinessDetailContract.Presenter {

    private BusinessDetailContract.View mView;
    private BusinessDataSource mRepo;

    public BusinessDetailPresenter (BusinessDetailContract.View view, BusinessDataSource repo){
        mView = view;
        mRepo = repo;

    }

    @Override
    public void loadBusiness(String businessId) {
        mRepo.loadBusiness(businessId, new BusinessDataSource.LoadBusinessCallback() {
            @Override
            public void onBusinessLoaded(final Business business) {

                List<Business> businessList = new ArrayList<>();
                businessList.add(business);
                mRepo.loadFoodList(businessList, new BusinessDataSource.LoadFoodListCallback() {

                    @Override
                    public void onFoodListLoaded(List<Food> foodList) {
                        mView.showBusinessDetails(business, foodList);
                    }

                    @Override
                    public void onError(String error) {
                        mView.showNoFoodListDetails(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                mView.showNoBusinessDetails(error);
            }
        });

    }

    @Override
    public void saveFood(Food food) {
        mRepo.saveFood(food, new BusinessDataSource.SaveFoodCallback() {
            @Override
            public void onFoodSaved() {

            }

            @Override
            public void onError(String error) {

            }
        });

    }
}
