
package com.taxi.passenger.TripsMvp.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripBookBean {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Isactive")
    @Expose
    private String isactive;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("bookingdetails")
    @Expose
    private List<Bookingdetail> bookingdetails = null;

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

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Bookingdetail> getBookingdetails() {
        return bookingdetails;
    }

    public void setBookingdetails(List<Bookingdetail> bookingdetails) {
        this.bookingdetails = bookingdetails;
    }

}
