package com.taxi.passenger.model.response;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginDetailModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("Isactive")
    @Expose
    private String isactive;
    @SerializedName("time_detail")
    @Expose
    private List<TimeDetail> timeDetail = null;
    @SerializedName("country_detail")
    @Expose
    private List<CountryDetail> countryDetail = null;


    @SerializedName("userdetail")
    @Expose
    private UserDetail userDetail;

    @SerializedName("cabDetails")
    @Expose

    private List<CabDetail> cabDetails = null;
    private String errorcode;

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public List<TimeDetail> getTimeDetail() {
        return timeDetail;
    }

    public void setTimeDetail(List<TimeDetail> timeDetail) {
        this.timeDetail = timeDetail;
    }

    public List<CountryDetail> getCountryDetail() {
        return countryDetail;
    }

    public void setCountryDetail(List<CountryDetail> countryDetail) {
        this.countryDetail = countryDetail;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public List<CabDetail> getCabDetails() {
        return cabDetails;
    }

    public void setCabDetails(List<CabDetail> cabDetails) {
        this.cabDetails = cabDetails;
    }

    public String getErrorcode() {
        return errorcode;
    }
}

