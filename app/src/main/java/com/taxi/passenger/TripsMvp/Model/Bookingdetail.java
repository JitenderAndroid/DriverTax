
package com.taxi.passenger.TripsMvp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bookingdetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("pickup_area")
    @Expose
    private String pickupArea;
    @SerializedName("drop_area")
    @Expose
    private String dropArea;
    @SerializedName("pickup_date_time")
    @Expose
    private String pickupDateTime;
    @SerializedName("taxi_type")
    @Expose
    private String taxiType;
    @SerializedName("taxi_id")
    @Expose
    private String taxiId;
    @SerializedName("departure_date_time")
    @Expose
    private String departureDateTime;
    @SerializedName("package")
    @Expose
    private String _package;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("book_create_date_time")
    @Expose
    private String bookCreateDateTime;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("isdevice")
    @Expose
    private String isdevice;
    @SerializedName("approx_time")
    @Expose
    private String approxTime;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("final_amount")
    @Expose
    private String finalAmount;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("transfer")
    @Expose
    private String transfer;
    @SerializedName("assigned_for")
    @Expose
    private String assignedFor;
    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("km")
    @Expose
    private String km;
    @SerializedName("timetype")
    @Expose
    private String timetype;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("driver_status")
    @Expose
    private String driverStatus;
    @SerializedName("pickup_lat")
    @Expose
    private String pickupLat;
    @SerializedName("pickup_long")
    @Expose
    private String pickupLong;
    @SerializedName("drop_lat")
    @Expose
    private String dropLat;
    @SerializedName("drop_long")
    @Expose
    private String dropLong;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("reason")
    @Expose
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPickupArea() {
        return pickupArea;
    }

    public void setPickupArea(String pickupArea) {
        this.pickupArea = pickupArea;
    }

    public String getDropArea() {
        return dropArea;
    }

    public void setDropArea(String dropArea) {
        this.dropArea = dropArea;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getTaxiType() {
        return taxiType;
    }

    public void setTaxiType(String taxiType) {
        this.taxiType = taxiType;
    }

    public String getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(String taxiId) {
        this.taxiId = taxiId;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getBookCreateDateTime() {
        return bookCreateDateTime;
    }

    public void setBookCreateDateTime(String bookCreateDateTime) {
        this.bookCreateDateTime = bookCreateDateTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIsdevice() {
        return isdevice;
    }

    public void setIsdevice(String isdevice) {
        this.isdevice = isdevice;
    }

    public String getApproxTime() {
        return approxTime;
    }

    public void setApproxTime(String approxTime) {
        this.approxTime = approxTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getAssignedFor() {
        return assignedFor;
    }

    public void setAssignedFor(String assignedFor) {
        this.assignedFor = assignedFor;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getTimetype() {
        return timetype;
    }

    public void setTimetype(String timetype) {
        this.timetype = timetype;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(String pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getDropLat() {
        return dropLat;
    }

    public void setDropLat(String dropLat) {
        this.dropLat = dropLat;
    }

    public String getDropLong() {
        return dropLong;
    }

    public void setDropLong(String dropLong) {
        this.dropLong = dropLong;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}

