package com.micahherrera.munch.businessdetail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.micahherrera.munch.Model.data.Review;
import com.micahherrera.munch.R;
import com.micahherrera.munch.businessdetail.BusinessDetailContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by micahherrera on 12/3/16.
 */

public class ReviewRecyclerAdapter
        extends RecyclerView.Adapter<ReviewRecyclerAdapter.EventRecyclerHolder> {

    private List<Review> mReviewList;
    private LayoutInflater inflater;
    private BusinessDetailContract.View mView;

    public ReviewRecyclerAdapter(List<Review> reviewList, BusinessDetailContract.View view){
        mReviewList = reviewList;
        mView = view;

    }

    @Override
    public ReviewRecyclerAdapter.EventRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        EventRecyclerHolder eventHolder = new EventRecyclerHolder(v);
        return eventHolder;
    }

    @Override
    public void onBindViewHolder(EventRecyclerHolder holder, int position) {
        if(mReviewList.get(position).getUser().getImageUrl()!=null) {
            Picasso.with(holder.itemView.getContext())
                    .load(mReviewList.get(position).getUser().getImageUrl().toString())
                    .resize(250, 250)
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mReviewImage);
        } else {
            holder.mReviewImage.setImageResource(R.mipmap.ic_launcher);
        }
        holder.mReviewUser.setText(mReviewList.get(position).getUser().getName());
        holder.mReviewRating.setText(Integer.toString(mReviewList.get(position).getRating()));
        holder.mReviewDate.setText(mReviewList.get(position).getTimeCreated());
        holder.mReviewReview.setText(mReviewList.get(position).getText());
    }


    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public void replaceData(List<Review> reviewList) {
        setList(reviewList);
        notifyDataSetChanged();
    }

    private void setList(List<Review> reviewList) {
        mReviewList = reviewList;
    }

    public class EventRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView mReviewDate;

        private ImageView mReviewImage;

        //@BindView(R.id.card_event_title)
        private TextView mReviewUser;

        //@BindView(R.id.card_event_location)
        private TextView mReviewRating;

        //@BindView(R.id.card_event_description)
        private TextView mReviewReview;


        public EventRecyclerHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mReviewImage = (ImageView) itemView.findViewById(R.id.card_review_image);
            mReviewUser = (TextView) itemView.findViewById(R.id.card_review_user);
            mReviewRating = (TextView) itemView.findViewById(R.id.card_review_rating);
            mReviewReview = (TextView) itemView.findViewById(R.id.card_review_review);
            mReviewDate = (TextView) itemView.findViewById(R.id.card_review_date);

        }

        @Override
        public void onClick(View view) {
            //TODO: set up clickthrough

        }

    }
}
