package com.micahherrera.munch.businessdetail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.micahherrera.munch.Model.BusinessDataSource;
import com.micahherrera.munch.Model.DataRepository;
import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.Model.data.Review;
import com.micahherrera.munch.R;

import com.micahherrera.munch.businessdetail.adapter.ReviewRecyclerAdapter;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

public class BusinessDetailActivity extends AppCompatActivity
        implements BusinessDetailContract.View,
        RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    private BusinessDetailContract.Presenter presenter;
    private BusinessDataSource mRepo;

    private ReviewRecyclerAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private TextView mRatingView;
    private TextView mPriceView;
    private TextView mHoursView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        mRepo = new DataRepository();

        mRecyclerView = (RecyclerView) findViewById(R.id.business_detail_recycler);
        mRatingView = (TextView) findViewById(R.id.business_detail_rating);
        mPriceView = (TextView) findViewById(R.id.business_detail_price);
        mHoursView = (TextView) findViewById(R.id.business_detail_hours);

        presenter = new BusinessDetailPresenter(this, mRepo);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String id = bundle.getString("name_id");
        presenter.loadBusiness(id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton) findViewById(R.id.activity_main_rfab);
        //TODO: setup FAB
        RapidFloatingActionContentLabelList rfaContent =
                new RapidFloatingActionContentLabelList(this);

        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();

        items.add(new RFACLabelItem<Integer>()
                .setLabel("Github: wangjiegulu")
                .setResId(R.mipmap.ic_launcher)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0));

        items.add(new RFACLabelItem<Integer>()
                .setLabel("tiantian.china.2@gmail.com")
                .setResId(R.mipmap.ic_launcher)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000,
                        ABTextUtil.dip2px(this, 4)))
                .setWrapper(1));

        items.add(new RFACLabelItem<Integer>()
                .setLabel("WangJie")
                .setResId(R.mipmap.ic_launcher)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(2));

        items.add(new RFACLabelItem<Integer>()
                .setLabel("Compose")
                .setResId(R.mipmap.ic_launcher)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(0xff283593)
                .setWrapper(3));

        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(this, 5));

        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();

    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();

    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();

    }

    @Override
    public void showBusinessDetails(Business business, List<Food> foodList, List<Review> reviewList) {
        mAdapter = new ReviewRecyclerAdapter(reviewList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPriceView.setText(business.getPrice());
        mRatingView.setText(Double.toString(business.getRating()));
        mHoursView.setText(business.getHours().get(0).getOpen().get(0).getStart());


    }

    @Override
    public void showNoBusinessDetails(String errorMessage) {
        //TODO

    }

    @Override
    public void showNoFoodListDetails(String errorMessage) {
        //TODO

    }

    @Override
    public void showNoReviewListDetails(String errorMessage) {
        //TODO

    }

}
