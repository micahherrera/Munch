package com.micahherrera.munch.businessdetail;

import com.micahherrera.munch.Model.BusinessDataSource;
import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.Model.data.Review;

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
    public void loadBusiness(final String businessId) {
        mRepo.loadBusiness(businessId, new BusinessDataSource.LoadBusinessCallback() {
            @Override
            public void onBusinessLoaded(final Business business) {

                List<Business> businessList = new ArrayList<>();
                businessList.add(business);
                mRepo.loadFoodList(businessList, new BusinessDataSource.LoadFoodListCallback() {

                    @Override
                    public void onFoodListLoaded(final List<Food> foodList) {
                        mRepo.loadBusinessReviews(businessId,
                                new BusinessDataSource.LoadReviewListCallback() {
                            @Override
                            public void onReviewListLoaded(List<Review> reviewList) {
                                mView.showBusinessDetails(business, foodList, reviewList);
                            }

                            @Override
                            public void onError(String error) {
                                mView.showNoReviewListDetails(error);
                            }
                        });

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
