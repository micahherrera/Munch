
package com.micahherrera.munch.Model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Review {

    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("time_created")
    @Expose
    private String timeCreated;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * 
     * @return
     *     The rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     *     The rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * 
     * @return
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The text
     */
    public String getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 
     * @return
     *     The timeCreated
     */
    public String getTimeCreated() {
        return timeCreated;
    }

    /**
     * 
     * @param timeCreated
     *     The time_created
     */
    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
