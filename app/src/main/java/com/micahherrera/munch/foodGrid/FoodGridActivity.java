package com.micahherrera.munch.foodgrid;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.micahherrera.munch.Model.DataRepository;
import com.micahherrera.munch.Model.LocationService;
import com.micahherrera.munch.Model.RetrofitFactoryYelp;
import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.R;
import com.micahherrera.munch.SettingsActivity;
import com.micahherrera.munch.businessdetail.BusinessDetailActivity;
import com.micahherrera.munch.foodgrid.adapter.FoodGridRecyclerAdapter;

import java.util.List;

import static com.micahherrera.munch.foodgrid.FoodGridPresenter.SETTINGS_RETURN;

public class FoodGridActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, FoodGridView{

    private FoodGridPresenterContract foodGridPresenter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FoodGridRecyclerAdapter adapter;
    public static final int REQUEST_LOCATION = 1;
    private ProgressBar progressBar;
    private TextView textError;
    private DataRepository mRepo;
    private BroadcastReceiver broadcastReceiver;
    private static final String SHARED_PREFERENCES = "preferences";
    private static final String OAUTH_PREFS = "oauth";
    private static final String connectionError = "There was an internet connection error. " +
            "Please check your wifi connection.";
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        progressBar = (ProgressBar) findViewById(R.id.progressBarGrid);
        recyclerView = (RecyclerView)findViewById(R.id.grid_recycler);
        textError=(TextView)findViewById(R.id.textError);

        RetrofitFactoryYelp.setToken(getSharedPreferences(SHARED_PREFERENCES, 0)
                .getString(OAUTH_PREFS, null));

        mRepo = new DataRepository();

        foodGridPresenter = new FoodGridPresenter(this, mRepo);

        boolean isConnected = checkConnection();
        if(isConnected) {
            checkPermissions();

        }
        foodGridPresenter.getNearby();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "onRequestPermissionsResult: request worked");
                    onPermissionGranted();

                }
                return;
            }
        }
    }

    public void setTextError(String errorMessage){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_grid, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                foodGridPresenter.goToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == foodGridPresenter.getSettingsReturn()){
            Bundle bundle = data.getBundleExtra("bundle");
            foodGridPresenter.newSettings(bundle);

        }
    }

    @Override
    public void renderFoods(List<Food> foodList) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutManager = new GridLayoutManager(this, 3);
        adapter = new FoodGridRecyclerAdapter(foodList, this, metrics.widthPixels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        progressBar.setVisibility(View.GONE);

    }


    @Override
    public void navigateToBusiness(Food food) {
        Intent intent = new Intent(this, BusinessDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name_id", food.getFoodId());
        bundle.putString("image_url", food.getFoodPic());
        intent.putExtra("bundle", bundle);
        startActivity(intent);

    }

    @Override
    public boolean checkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("TAG", "onCreate: You are connected");
            return true;

        } else {
            Log.d("TAG", "onCreate: You are not connected");
            setTextError(connectionError);
            return false;

        }

    }

    public void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    FoodGridActivity.REQUEST_LOCATION);
        } else {
            Log.d("TAG", "checkPermissions: good");
            onPermissionGranted();

        }
    }

    public void onPermissionGranted(){
        setupBroadcastReceiver();
        foodGridPresenter.setupYelp();
        getLocation();


    }

    public void unregister(){
        unregisterReceiver(broadcastReceiver);

    }

    public void setupBroadcastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("coordinatesLoaded");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                Log.i("Receiver", "Broadcast received: " + action);

                if(action.equals("coordinatesLoaded")){
                    Bundle bundle = intent.getBundleExtra("bundle");
                    foodGridPresenter.getCoordinates(bundle);
                    unregister();

                }
            }
        };
        registerReceiver(broadcastReceiver,filter);

    }

    public void getLocation(){
        Intent getLocation = new Intent(this, LocationService.class);
        getLocation.putExtra("location", "location");
        startService(getLocation);

    }

    @Override
    public void saveToken(String token) {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFERENCES, 0);
        SharedPreferences.Editor e = sp.edit();
        e.putString(OAUTH_PREFS, token);
        e.commit();
        RetrofitFactoryYelp.setToken(token);

    }

    @Override
    public void goToSettings(Bundle bundle) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, SETTINGS_RETURN);
    }

    @Override
    public void showFoodsLoading() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }


}
