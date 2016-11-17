package com.micahherrera.munch.foodGrid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.micahherrera.munch.Model.DownloadUrlTask;
import com.micahherrera.munch.Model.LocationService;
import com.micahherrera.munch.Model.RetrofitFactoryYelp;
import com.micahherrera.munch.Model.SearchYelpResponse;
import com.micahherrera.munch.Model.contract.GridActivityContract;
import com.micahherrera.munch.Model.contract.YelpApi3;
import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.Model.data.TokenO;
import com.micahherrera.munch.R;
import com.micahherrera.munch.SettingsActivity;
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

public class FoodGridPresenter implements GridActivityContract{
    private YelpApi3 yelpApi3;
    private FoodGridActivity activity;
    private CoordinateOptions coordinate;
    private BroadcastReceiver broadcastReceiver;
    private static final String OAUTH_PREFS = "oauth";
    private static final String SHARED_PREFERENCES = "preferences";
    private static final String CLIENT_ID = "rDxkzZTw8i-o95rVKz2FWA";
    private static final String CLIENT_SECRET =
            "k1Q1tmaMtRip75s2KBOgQ37fLuUs92ycFX9oOiudnkt97c3wXbVA3hum96S4g4dM";
    private static final String locationError = "There was an error getting a geographical location. " +
            "Tap here to manually enter your whereabouts.";
    private static final String oauthError = "There was an error with authentification. " +
            "Contact developer.";
    private static final String yelpSearchError = "There was an error with your search." +
            " Please try again.";
    private static final String connectionError = "There was an internet connection error. " +
            "Please check your wifi connection.";
    private Bundle settingsBundle = new Bundle();

    private FoodGridView view;

    public String token;

    public static final int SETTINGS_RETURN = 1;

    public FoodGridPresenter(FoodGridActivity activity, FoodGridView view){
        this.activity = activity;
        this.view=view;

    }

    public void getNearby(){
        boolean isConnected = checkConnection();
        if(isConnected) {
            checkPermissions();

        }

    }

    private void getLocation(){
        Intent getLocation = new Intent(activity, LocationService.class);
        getLocation.putExtra("location", "location");
        activity.startService(getLocation);

    }

    private void setupBroadcastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("coordinatesLoaded");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                Log.i("Receiver", "Broadcast received: " + action);

                if(action.equals("coordinatesLoaded")){
                    Bundle bundle = intent.getBundleExtra("bundle");
                    getCoordinates(bundle);
                    unregister();

                }
            }
        };
        activity.registerReceiver(broadcastReceiver,filter);

    }

    private void getCoordinates(Bundle bundle) {
        Double mLatitude = bundle.getDouble(activity.getString(R.string.lat));
        Double mLongitude = bundle.getDouble(activity.getString(R.string.longitude));

        if(mLatitude == null || mLongitude == null){
            activity.setTextError(locationError);
        } else {
            Log.d("TAG", "getCoordinates: " + mLatitude);
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
                activity.setTextError(yelpSearchError);

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
            activity.setTextError(yelpSearchError);

        } else {
            new DownloadUrlTask(yelpApi3, this).execute(searchYelpResponse.getBusinesses());

        }
    }

    public void unregister(){
        activity.unregisterReceiver(broadcastReceiver);

    }

    public boolean checkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("TAG", "onCreate: You are connected");
            return true;

        } else {
            Log.d("TAG", "onCreate: You are not connected");
            activity.setTextError(connectionError);
            return false;

        }

    }

    public void checkPermissions(){
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    FoodGridActivity.REQUEST_LOCATION);
        } else {
            Log.d("TAG", "checkPermissions: good");
            onPermissionGranted();

        }
    }

    public void onPermissionGranted(){
        setupBroadcastReceiver();
        getLocation();
        setupYelp();

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

                SharedPreferences sp = activity.getSharedPreferences(SHARED_PREFERENCES, 0);
                SharedPreferences.Editor e = sp.edit();
                e.putString(OAUTH_PREFS, response.body().getAccess_token());
                e.commit();

                callYelp(response.body().getAccess_token());

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("TAG", "onFailure: no go oauth");
                activity.setTextError(oauthError);

            }
        });

    }

    private boolean isOauthed(){
        String token = activity.getSharedPreferences(SHARED_PREFERENCES, 0)
                .getString(OAUTH_PREFS, null);
        if(token==null){
            return false;

        } else {
            Log.d("TAG", "isOauthed: noneneeded");
            callYelp(token);
            return true;

        }
    }

    public void goToSettings(){

        Intent intent = new Intent(activity, SettingsActivity.class);
        intent.putExtra("bundle", settingsBundle);
        activity.startActivityForResult(intent, SETTINGS_RETURN);

    }

    @Override
    public void newSettings(Bundle bundle) {
        settingsBundle = bundle;
        getNearby();

    }

    public void onFoodLoaded(List<Food> foodList){
        Collections.shuffle(foodList);
        view.renderFoods(foodList);
    }
}