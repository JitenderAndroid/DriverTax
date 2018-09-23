
package com.taxi.passenger.FilterAllTripMvp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllTrip {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("pickup_date")
    @Expose
    private Object pickupDate;
    @SerializedName("pickup_area")
    @Expose
    private String pickupArea;
    @SerializedName("drop_area")
    @Expose
    private String dropArea;
    @SerializedName("pickup_time")
    @Expose
    private Object pickupTime;
    @SerializedName("pickup_date_time")
    @Expose
    private String pickupDateTime;
    @SerializedName("area")
    @Expose
    private Object area;
    @SerializedName("landmark")
    @Expose
    private Object landmark;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("taxi_type")
    @Expose
    private String taxiType;
    @SerializedName("taxi_id")
    @Expose
    private String taxiId;
    @SerializedName("departure_time")
    @Expose
    private Object departureTime;
    @SerializedName("departure_date")
    @Expose
    private Object departureDate;
    @SerializedName("return_date")
    @Expose
    private Object returnDate;
    @SerializedName("flight_number")
    @Expose
    private Object flightNumber;
    @SerializedName("package")
    @Expose
    private String _package;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("book_create_date_time")
    @Expose
    private String bookCreateDateTime;
    @SerializedName("create_date_time")
    @Expose
    private Object createDateTime;
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
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("transfer")
    @Expose
    private String transfer;
    @SerializedName("assigned_for")
    @Expose
    private String assignedFor;
    @SerializedName("item_status")
    @Expose
    private Object itemStatus;
    @SerializedName("transaction")
    @Expose
    private Object transaction;
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
    @SerializedName("pickup_longs")
    @Expose
    private String pickupLongs;
    @SerializedName("drop_lat")
    @Expose
    private String dropLat;
    @SerializedName("drop_longs")
    @Expose
    private String dropLongs;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("car_id")
    @Expose
    private String carId;
    @SerializedName("car_type")
    @Expose
    private String carType;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("seat_capacity")
    @Expose
    private String seatCapacity;
    @SerializedName("driver_detail")

  /*  @Expose
    private com.taxi.passenger.AllTripFeedMvp.Model.DriverDetail driverDetail;
*/
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

    public Object getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Object pickupDate) {
        this.pickupDate = pickupDate;
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

    public Object getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Object pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public Object getArea() {
        return area;
    }

    public void setArea(Object area) {
        this.area = area;
    }

    public Object getLandmark() {
        return landmark;
    }

    public void setLandmark(Object landmark) {
        this.landmark = landmark;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
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

    public Object getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Object departureTime) {
        this.departureTime = departureTime;
    }

    public Object getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Object departureDate) {
        this.departureDate = departureDate;
    }

    public Object getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Object returnDate) {
        this.returnDate = returnDate;
    }

    public Object getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(Object flightNumber) {
        this.flightNumber = flightNumber;
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

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getBookCreateDateTime() {
        return bookCreateDateTime;
    }

    public void setBookCreateDateTime(String bookCreateDateTime) {
        this.bookCreateDateTime = bookCreateDateTime;
    }

    public Object getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Object createDateTime) {
        this.createDateTime = createDateTime;
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

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
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

    public Object getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(Object itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Object getTransaction() {
        return transaction;
    }

    public void setTransaction(Object transaction) {
        this.transaction = transaction;
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

    public String getPickupLongs() {
        return pickupLongs;
    }

    public void setPickupLongs(String pickupLongs) {
        this.pickupLongs = pickupLongs;
    }

    public String getDropLat() {
        return dropLat;
    }

    public void setDropLat(String dropLat) {
        this.dropLat = dropLat;
    }

    public String getDropLongs() {
        return dropLongs;
    }

    public void setDropLongs(String dropLongs) {
        this.dropLongs = dropLongs;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

   /* public com.taxi.passenger.AllTripFeedMvp.Model.DriverDetail getDriverDetail() {
        return driverDetail;
    }

    public void setDriverDetail(DriverDetail driverDetail) {
        this.driverDetail = driverDetail;
    }
*/
}
