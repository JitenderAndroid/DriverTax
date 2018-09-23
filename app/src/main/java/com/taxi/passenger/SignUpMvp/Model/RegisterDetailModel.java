package com.taxi.passenger.SignUpMvp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.taxi.passenger.model.response.CabDetail;
import com.taxi.passenger.model.response.CountryDetail;
import com.taxi.passenger.model.response.TimeDetail;
import com.taxi.passenger.model.response.UserDetail;

public class RegisterDetailModel {

    @SerializedName("status")
    @Expose
    private String status;

 /*   @SerializedName("error ")
    @Expose
    private String status;*/

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Mail")
    @Expose
    private String mail;
    @SerializedName("country_detail")
    @Expose
    private List<CountryDetail> countryDetail = null;
    @SerializedName("user_Detail")
    @Expose
    private List<UserDetail> userDetail = null;
    @SerializedName("time_detail")
    @Expose
    private List<TimeDetail> timeDetail = null;
    @SerializedName("cabDetails")
    @Expose
    private List<CabDetail> cabDetails = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<CountryDetail> getCountryDetail() {
        return countryDetail;
    }

    public void setCountryDetail(List<CountryDetail> countryDetail) {
        this.countryDetail = countryDetail;
    }

    public List<UserDetail> getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(List<UserDetail> userDetail) {
        this.userDetail = userDetail;
    }

    public List<TimeDetail> getTimeDetail() {
        return timeDetail;
    }

    public void setTimeDetail(List<TimeDetail> timeDetail) {
        this.timeDetail = timeDetail;
    }

    public List<CabDetail> getCabDetails() {
        return cabDetails;
    }

    public void setCabDetails(List<CabDetail> cabDetails) {
        this.cabDetails = cabDetails;
    }


}
