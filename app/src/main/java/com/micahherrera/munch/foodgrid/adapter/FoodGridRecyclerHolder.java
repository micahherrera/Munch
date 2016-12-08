package com.micahherrera.munch.foodgrid.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.R;
import com.micahherrera.munch.businessdetail.BusinessDetailActivity;
import com.micahherrera.munch.foodgrid.FoodGridActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by micahherrera on 10/18/16.
 */

public class FoodGridRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private FoodGridActivity foodGridActivity;
    private List<Food> foodArrayList;
    private ImageView imageView;
    public View root;

    public FoodGridRecyclerHolder(View itemView, FoodGridActivity foodGridActivity, List<Food> foodArrayList) {
        super(itemView);
        this.foodGridActivity = foodGridActivity;
        this.foodArrayList=foodArrayList;
        root=itemView;
        itemView.setOnClickListener(this);
        imageView = (ImageView) itemView.findViewById(R.id.grid_image);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        Food food = foodArrayList.get(position);
        foodGridActivity.navigateToBusiness(food);
    }

    public void putThePhoto(String url, int width){
        Picasso.with(itemView.getContext())
                .load(url)
                .resize(width, width)
                .error(android.R.drawable.alert_dark_frame)
                .centerCrop()
                .into(imageView);
    }

    private void goToDetailView(Food food){
        Intent intent = new Intent(foodGridActivity, BusinessDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name_id", food.getFoodId());
        bundle.putString("image_url", food.getFoodPic());
        intent.putExtras(bundle);
        foodGridActivity.startActivity(intent);
    }
}
