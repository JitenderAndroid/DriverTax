package com.taxi.passenger.RatingMVP.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abhilasha Yadav on 9/27/2017.
 */

public class RatingModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("given_rating_detail")
    @Expose
    private List<String> givenRatingDetail = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getGivenRatingDetail() {
        return givenRatingDetail;
    }

    public void setGivenRatingDetail(List<String> givenRatingDetail) {
        this.givenRatingDetail = givenRatingDetail;
    }


}
