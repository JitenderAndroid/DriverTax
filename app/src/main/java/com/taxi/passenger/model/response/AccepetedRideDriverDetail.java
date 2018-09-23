package com.taxi.passenger.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccepetedRideDriverDetail {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("key")
    @Expose
    private String key;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}