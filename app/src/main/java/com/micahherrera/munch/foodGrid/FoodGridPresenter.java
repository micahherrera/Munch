package com.micahherrera.munch.foodgrid;

import android.os.Bundle;
import android.util.Log;

import com.micahherrera.munch.Model.BusinessDataSource;
import com.micahherrera.munch.Model.RetrofitFactoryYelp;
import com.micahherrera.munch.Model.contract.YelpApi3;
import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.Model.data.SearchYelpResponse;
import com.micahherrera.munch.Model.data.TokenO;

import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by micahherrera on 10/17/16.
 */

public class FoodGridPresenter implements FoodGridPresenterContract {

    private YelpApi3 yelpApi3;

    private CoordinateOptions coordinate;

    private static final String CLIENT_ID = "rDxkzZTw8i-o95rVKz2FWA";

    private static final String CLIENT_SECRET =
            "k1Q1tmaMtRip75s2KBOgQ37fLuUs92ycFX9oOiudnkt97c3wXbVA3hum96S4g4dM";

    private static final String locationError = "There was an error getting a geographical location. " +
            "Tap here to manually enter your whereabouts.";

    private static final String oauthError = "There was an error with authentification. " +
            "Contact developer.";

    private static final String yelpSearchError = "There was an error with your search." +
            " Please try again.";

    private Bundle settingsBundle = new Bundle();

    private FoodGridView view;

    private BusinessDataSource mRepo;

    public static final int SETTINGS_RETURN = 1;

    public FoodGridPresenter(FoodGridView view, BusinessDataSource repo){
        this.view = view;
        mRepo = repo;

    }

    public void getNearby(){

    }



    public void getCoordinates(Bundle bundle) {
        Double mLatitude = bundle.getDouble("lat");
        Double mLongitude = bundle.getDouble("long");

        if(mLatitude == null || mLongitude == null){
           view.setTextError(locationError);
        } else {
            Log.d("TAG", "getCoordinates: " + mLatitude + mLongitude);
            coordinate = CoordinateOptions.builder()
                    .latitude(mLatitude)
                    .longitude(mLongitude).build();
            if(!isOauthed()) {
                oauthYelp();

            }
        }
    }

    private void callYelp(String token){

        HashMap parameters = setParameters(settingsBundle);
        Call<SearchYelpResponse> call = yelpApi3.search(parameters, "Bearer " + token);
        Log.d("TAG", "callYelp: callingyelp");
        Callback<SearchYelpResponse> callback = new Callback<SearchYelpResponse>() {

            @Override
            public void onResponse(Call<SearchYelpResponse> call,
                                   Response<SearchYelpResponse> response) {
               SearchYelpResponse searchResponse = response.body();
                // Update UI text with the searchResponse.
                Log.d("TAG", "onResponse: "+response.body());
                Log.d("TAG", "onResponse: "+searchResponse.getBusinesses().get(0).getName());
                loadBusinesses(searchResponse);

            }

            @Override
            public void onFailure(Call<SearchYelpResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.d("TAG", "onFailure: ErrorOnSearch" + t.toString());
                view.setTextError(yelpSearchError);

            }
        };

        call.enqueue(callback);
    }

    private HashMap setParameters(Bundle bundle){
        String pricing = "";

        HashMap parameters = new HashMap();
        parameters.put("term", bundle.getString("term", "food"));
        parameters.put("limit", "20");
        parameters.put("latitude", coordinate.latitude().toString());
        parameters.put("longitude", coordinate.longitude().toString());
        parameters.put("radius_filter", Integer.toString(bundle.getInt("radius_filter", 4800)));
        parameters.put("sort", "2");
        parameters.put("open_now", bundle.getString("open_now", "true"));
        if(bundle.getInt("$")==1){
            pricing += "1,";
        }
        if(bundle.getInt("$$")==1){
            pricing += "2,";
        }
        if(bundle.getInt("$$$")==1){
            pricing += "3,";
        }
        if(bundle.getInt("$$$$")==1){
            pricing += "4,";
        }
        if(!pricing.equals("")) {
            pricing=pricing.substring(0, pricing.length()-1);
            Log.d("TAG", "setParameters: "+pricing);
            parameters.put("price", pricing);
        }

        return parameters;
    }

    private void loadBusinesses(SearchYelpResponse searchYelpResponse){
        Log.d("TAG", "loadBusinesses: "+ searchYelpResponse.getBusinesses().size());
        if(searchYelpResponse.getBusinesses().size() <= 0){
            view.setTextError(yelpSearchError);

        } else {
            mRepo.loadFoodList(searchYelpResponse.getBusinesses(), new BusinessDataSource.LoadFoodListCallback() {
                @Override
                public void onFoodListLoaded(List<Food> foodList) {
                    view.renderFoods(foodList);

                }

                @Override
                public void onError(String error) {

                }
            });

        }
    }



    public void setupYelp(){
        RetrofitFactoryYelp factory = new RetrofitFactoryYelp();
        yelpApi3 = factory.getInstance();

    }

    public void oauthYelp(){
        Map map = new HashMap();
        map.put("client_id", CLIENT_ID);
        map.put("client_secret", CLIENT_SECRET);
        map.put("grant_type", "client_credentials");

        yelpApi3.oauth(map).enqueue(new Callback<TokenO>() {
            @Override
            public void onResponse(Call<TokenO> call, Response<TokenO> response) {
                Log.d("TAG", "onResponse: "+response.body().getAccess_token());
                view.saveToken(response.body().getAccess_token());
                callYelp(response.body().getAccess_token());

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("TAG", "onFailure: no go oauth");
                view.setTextError(oauthError);

            }
        });

    }


    private boolean isOauthed(){
        String token = RetrofitFactoryYelp.getToken();
        if(token==null){
            return false;

        } else {
            Log.d("TAG", "isOauthed: noneneeded" + token);
            callYelp(token);
            return true;

        }
    }

    public void goToSettings(){
        settingsBundle.putString("token", RetrofitFactoryYelp.getToken());
        settingsBundle.putString("latitude", Double.toString(coordinate.latitude()));
        settingsBundle.putString("longitude", Double.toString(coordinate.longitude()));
        view.goToSettings(settingsBundle);

    }

    @Override
    public int getSettingsReturn() {
        return SETTINGS_RETURN;

    }


    @Override
    public void newSettings(Bundle bundle) {
        settingsBundle = bundle;
        if(view.checkConnection()){
            view.checkPermissions();
            view.showFoodsLoading();
        }

    }

    public void onFoodLoaded(List<Food> foodList){
        Log.d("TAG", "onFoodLoaded: loaded");
        Collections.shuffle(foodList);
        view.renderFoods(foodList);

    }
}