
package com.micahherrera.munch.Model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class SearchYelpResponse {

    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("businesses")
    @Expose
    private List<Business> businesses = new ArrayList<Business>();

    /**
     * 
     * @return
     *     The total
     */
    public int getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The businesses
     */
    public List<Business> getBusinesses() {
        return businesses;
    }

    /**
     * 
     * @param businesses
     *     The businesses
     */
    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

}
