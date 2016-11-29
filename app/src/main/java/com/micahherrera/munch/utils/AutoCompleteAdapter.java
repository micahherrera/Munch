package com.micahherrera.munch.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.micahherrera.munch.Model.RetrofitFactoryYelp;
import com.micahherrera.munch.Model.contract.YelpApi3;
import com.micahherrera.munch.Model.data.AutoComplete;
import com.micahherrera.munch.Model.data.Business;
import com.micahherrera.munch.Model.data.Category;
import com.micahherrera.munch.Model.data.Term;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by micahherrera on 11/15/16.
 */

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private ArrayList<String> resultList = new ArrayList<>();
    private AutoComplete autoComplete;
    private YelpApi3 yelp;
    private String token;
    private Map map;

    public AutoCompleteAdapter(Context context, String token, String latitude, String longitude) {
        mContext = context;
        this.token = token;
        map = new HashMap();
        map.put("latitude", latitude);
        map.put("longitude", longitude);

        RetrofitFactoryYelp factoryYelp = new RetrofitFactoryYelp();
        yelp = factoryYelp.getInstance();
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    autoComplete= findTerms(map, constraint.toString());
                    List<Term> termList = autoComplete.getTerms();
                    List<Business> businessList = autoComplete.getBusinesses();
                    List<Category> categoryList = autoComplete.getCategories();

                    for(Term term:termList){
                        resultList.add(term.getText());
                    }
                    for(Business business:businessList){
                        resultList.add(business.getName());
                    }
                    for(Category category:categoryList){
                        resultList.add(category.getAlias());
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private AutoComplete findTerms(Map params, String text) {
        resultList = new ArrayList<>();

        params.put("text", text);
        Call<AutoComplete> call = yelp.autoComplete(params, "Bearer " + token);
        Response<AutoComplete> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "findTerms: couldnt find them");
        }
        return response.body();

    }
}
