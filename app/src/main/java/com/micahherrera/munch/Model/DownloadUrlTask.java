package com.micahherrera.munch.Model;

import android.os.AsyncTask;

import com.micahherrera.munch.Model.contract.YelpApi3;
import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Food;
import com.micahherrera.munch.foodGrid.FoodFoodGridPresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by micahherrera on 10/17/16.
 */

public class DownloadUrlTask extends AsyncTask<List<Business>, Void, Void> {

    YelpApi3 yelpAPI;
    ArrayList<Food> foodList;
    FoodFoodGridPresenter presenter;

    public DownloadUrlTask(YelpApi3 yelpAPI, FoodFoodGridPresenter presenter){
        this.yelpAPI=yelpAPI;
        this.presenter=presenter;
    }

    @Override
    protected Void doInBackground(List<Business>... businesses) {
        int start= 0;
        int finish = businesses[0].size()<20 ? businesses[0].size() : 20;

        foodList = new ArrayList<>();

        for(int i = start; i < finish; i++) {
            Business b = businesses[0].get(i);
            try {
                Document doc = Jsoup.connect("http://www.yelp.com/biz_photos/" + b.getId() +
                        "?tab=food").get();
                //Log.d("TAG", "doInBackground: "+ doc.body().toString());
                Elements all = doc.getAllElements();
                Pattern p = Pattern.compile("(?is)\"src_high_res\": \"(.+?)\"");
                Matcher m = p.matcher(all.toString());
                int i2 = 0;
                while (m.find() && (i2 < 16)) {
                    Food itemUrl = new Food(m.group(1), b.getId(), b.getName());
                    foodList.add(itemUrl);
                    i2++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        presenter.onFoodLoaded(foodList);
    }
}
