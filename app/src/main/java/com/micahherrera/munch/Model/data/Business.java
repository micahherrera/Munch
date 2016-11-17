package com.micahherrera.munch.Model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Business {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("is_claimed")
    @Expose
    private boolean isClaimed;
    @SerializedName("is_closed")
    @Expose
    private boolean isClosed;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("review_count")
    @Expose
    private int reviewCount;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("photos")
    @Expose
    private List<String> photos = new ArrayList<String>();
    @SerializedName("hours")
    @Expose
    private List<Hour> hours = new ArrayList<Hour>();
    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("location")
    @Expose
    private Location location;

    /**
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     *
     * @param imageUrl
     *     The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return
     *     The isClaimed
     */
    public boolean isIsClaimed() {
        return isClaimed;
    }

    /**
     *
     * @param isClaimed
     *     The is_claimed
     */
    public void setIsClaimed(boolean isClaimed) {
        this.isClaimed = isClaimed;
    }

    /**
     *
     * @return
     *     The isClosed
     */
    public boolean isIsClosed() {
        return isClosed;
    }

    /**
     *
     * @param isClosed
     *     The is_closed
     */
    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
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

    /**
     *
     * @return
     *     The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     *     The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     *     The rating
     */
    public double getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     *     The rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     *     The reviewCount
     */
    public int getReviewCount() {
        return reviewCount;
    }

    /**
     *
     * @param reviewCount
     *     The review_count
     */
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    /**
     *
     * @return
     *     The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     *     The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     *     The photos
     */
    public List<String> getPhotos() {
        return photos;
    }

    /**
     *
     * @param photos
     *     The photos
     */
    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    /**
     *
     * @return
     *     The hours
     */
    public List<Hour> getHours() {
        return hours;
    }

    /**
     *
     * @param hours
     *     The hours
     */
    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }

    /**
     *
     * @return
     *     The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     *
     * @param categories
     *     The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     *
     * @return
     *     The coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     *
     * @param coordinates
     *     The coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     *
     * @return
     *     The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     *     The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

}