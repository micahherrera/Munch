package com.micahherrera.munch.foodgrid.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.R;
import com.micahherrera.munch.foodgrid.FoodGridActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupStateListener;

/**
 * Created by micahherrera on 10/18/16.
 */

public class FoodGridRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        PopupInflaterListener,
        PopupStateListener {
    private FoodGridActivity foodGridActivity;
    private List<Food> foodArrayList;
    private ImageView imageView;
    public View root;

    private LongPressPopup popup;

    private ImageView popupImageView;
    private TextView popupNameView;
    private TextView popupRatingView;
    private TextView popupPriceView;
    private TextView popupDistanceView;

    public FoodGridRecyclerHolder(View itemView, FoodGridActivity foodGridActivity, List<Food> foodArrayList) {
        super(itemView);
        this.foodGridActivity = foodGridActivity;
        this.foodArrayList=foodArrayList;
        root=itemView;
        itemView.setOnClickListener(this);
        imageView = (ImageView) itemView.findViewById(R.id.grid_image);

        if (popup != null && popup.isRegistered()) {
            popup.unregister();
        }

        popup = new LongPressPopupBuilder(itemView.getContext())
                .setTarget(itemView)
                .setPopupView(R.layout.popup_layout, this)
                .setLongPressDuration(700)
//                .setDismissOnLongPressStop(true)
//                .setLongPressReleaseListener(this)
                .setPopupListener(this)
                //.setTag("PopupFood")
                .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_CENTER)
                .build();
        popup.register();
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

    @Override
    public void onViewInflated(@Nullable String popupTag, View root) {
        popupImageView = (ImageView) root.findViewById(R.id.popup_image);
        popupNameView = (TextView) root.findViewById(R.id.popup_name_text);
        popupPriceView = (TextView) root.findViewById(R.id.popup_price_text);
        popupRatingView = (TextView) root.findViewById(R.id.popup_rating_text);
        popupDistanceView = (TextView) root.findViewById(R.id.popup_distance_text);
    }

    @Override
    public void onPopupShow(@Nullable String popupTag) {
        Food food = foodArrayList.get(getAdapterPosition());

        Picasso.with(popup.getContext())
                .load(food.getFoodPic())
                .error(android.R.drawable.alert_dark_frame)
                .into(popupImageView);

        popupNameView.setText(food.getRestaurantName());
        popupPriceView.setText("$%&");
        popupDistanceView.setText("# miles");
        popupRatingView.setText("#");

    }

    @Override
    public void onPopupDismiss(@Nullable String popupTag) {

    }
}
