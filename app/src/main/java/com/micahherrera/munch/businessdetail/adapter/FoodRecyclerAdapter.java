package com.micahherrera.munch.businessdetail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.R;
import com.micahherrera.munch.businessdetail.BusinessDetailContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by micahherrera on 12/8/16.
 */

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.FoodRecyclerHolder> {

    private List<Food> mFoodList;
    private LayoutInflater inflater;
    private BusinessDetailContract.View mView;

    public FoodRecyclerAdapter(List<Food> foodList, BusinessDetailContract.View view){
        mFoodList = foodList;
        mView = view;

    }

    @Override
    public FoodRecyclerAdapter.FoodRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        FoodRecyclerAdapter.FoodRecyclerHolder eventHolder = new FoodRecyclerAdapter.FoodRecyclerHolder(v);
        return eventHolder;
    }

    @Override
    public void onBindViewHolder(FoodRecyclerHolder holder, int position) {
        if(mFoodList.get(position).getFoodPic()!=null) {
            Picasso.with(holder.itemView.getContext())
                    .load(mFoodList.get(position).getFoodPic())
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mReviewImage);
        } else {
            holder.mReviewImage.setImageResource(R.mipmap.ic_launcher);
        }
    }


    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    public void replaceData(List<Food> foodList) {
        setList(foodList);
        notifyDataSetChanged();
    }

    private void setList(List<Food> foodList) {
        mFoodList = foodList;
    }

    public class FoodRecyclerHolder extends RecyclerView.ViewHolder{

        private ImageView mReviewImage;

        public FoodRecyclerHolder(View itemView) {
            super(itemView);
            mReviewImage = (ImageView) itemView.findViewById(R.id.business_detail_food_image);

        }
    }
}
