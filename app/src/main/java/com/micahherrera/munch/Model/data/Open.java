
package com.micahherrera.munch.Model.data;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Open {

    @SerializedName("is_overnight")
    @Expose
    private boolean isOvernight;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("start")
    @Expose
    private String start;

    /**
     * 
     * @return
     *     The isOvernight
     */
    public boolean isIsOvernight() {
        return isOvernight;
    }

    /**
     * 
     * @param isOvernight
     *     The is_overnight
     */
    public void setIsOvernight(boolean isOvernight) {
        this.isOvernight = isOvernight;
    }

    /**
     * 
     * @return
     *     The end
     */
    public String getEnd() {
        return end;
    }

    /**
     * 
     * @param end
     *     The end
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * 
     * @return
     *     The day
     */
    public int getDay() {
        return day;
    }

    /**
     * 
     * @param day
     *     The day
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * 
     * @return
     *     The start
     */
    public String getStart() {
        return start;
    }

    /**
     * 
     * @param start
     *     The start
     */
    public void setStart(String start) {
        this.start = start;
    }

}
