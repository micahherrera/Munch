package com.micahherrera.munch.foodGrid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import com.micahherrera.munch.Controller.GridAdapter;
import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.R;

import java.util.List;

public class FoodGridActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, FoodGridView{

    private FoodFoodGridPresenter foodGridPresenter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private GridAdapter adapter;
    public static final int REQUEST_LOCATION = 1;
    private ProgressBar progressBar;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        progressBar = (ProgressBar) findViewById(R.id.progressBarGrid);
        recyclerView = (RecyclerView)findViewById(R.id.grid_recycler);
        textError=(TextView)findViewById(R.id.textError);
        foodGridPresenter = new FoodFoodGridPresenter(this, this);
        foodGridPresenter.getNearby();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodGridPresenter.unregister();

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
                    foodGridPresenter.onPermissionGranted();

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
        if (resultCode == foodGridPresenter.SETTINGS_RETURN){
            Bundle bundle = data.getBundleExtra("bundle");
            foodGridPresenter.newSettings(bundle);

        }
    }

    @Override
    public void renderFoods(List<Food> foodList) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutManager = new GridLayoutManager(this, 3);
        adapter = new GridAdapter(foodList, this, metrics.widthPixels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void navigateToBusiness(String businessId) {

    }
}
