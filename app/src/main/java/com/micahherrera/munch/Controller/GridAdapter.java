package com.micahherrera.munch.Controller;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.R;
import com.micahherrera.munch.foodGrid.FoodGridActivity;

import java.util.List;

/**
 * Created by micahherrera on 10/18/16.
 */

public class GridAdapter extends RecyclerView.Adapter<GridHolder>{
    private List<Food> foodArrayList;
    private LayoutInflater inflater;
    private FoodGridActivity foodGridActivity;
    private int width;

    public GridAdapter (List<Food> myFoodArrayList, FoodGridActivity foodGridActivity, int width){
        foodArrayList = myFoodArrayList;
        this.foodGridActivity = foodGridActivity;
        this.width = width;
    }

    @Override
    public GridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        GridHolder gridHolder = new GridHolder(v, foodGridActivity, foodArrayList);
        return gridHolder;
    }

    @Override
    public void onBindViewHolder(GridHolder holder, int position) {
        //width = (int) foodGridActivity.getResources().getDimension(R.dimen.grid_cell_width);
        holder.putThePhoto(foodArrayList.get(position).getFoodPic(), ((width-8)/3));
        Log.d("TAG", "onBindViewHolder: " + foodArrayList.get(position).getFoodPic());
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }
}
