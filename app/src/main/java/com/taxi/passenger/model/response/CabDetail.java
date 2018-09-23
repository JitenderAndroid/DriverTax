
package com.taxi.passenger.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CabDetail {

    @SerializedName("cab_id")
    @Expose
    private String cabId;
    @SerializedName("cartype")
    @Expose
    private String cartype;
    @SerializedName("car_rate")
    @Expose
    private String carRate;
    @SerializedName("transfertype")
    @Expose
    private String transfertype;
    @SerializedName("intialkm")
    @Expose
    private String intialkm;
    @SerializedName("intailrate")
    @Expose
    private String intailrate;
    @SerializedName("standardrate")
    @Expose
    private String standardrate;
    @SerializedName("fromintialkm")
    @Expose
    private String fromintialkm;
    @SerializedName("fromintailrate")
    @Expose
    private String fromintailrate;
    @SerializedName("fromstandardrate")
    @Expose
    private String fromstandardrate;
    @SerializedName("night_fromintialkm")
    @Expose
    private String nightFromintialkm;
    @SerializedName("night_fromintailrate")
    @Expose
    private String nightFromintailrate;
    @SerializedName("extrahour")
    @Expose
    private String extrahour;
    @SerializedName("extrakm")
    @Expose
    private String extrakm;
    @SerializedName("timetype")
    @Expose
    private String timetype;
    @SerializedName("package")
    @Expose
    private String _package;
    @SerializedName("night_package")
    @Expose
    private String nightPackage;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("ride_time_rate")
    @Expose
    private String rideTimeRate;
    @SerializedName("night_ride_time_rate")
    @Expose
    private String nightRideTimeRate;
    @SerializedName("daystarttime")
    @Expose
    private String daystarttime;
    @SerializedName("day_end_time")
    @Expose
    private String dayEndTime;
    @SerializedName("night_start_time")
    @Expose
    private String nightStartTime;
    @SerializedName("night_end_time")
    @Expose
    private String nightEndTime;
    @SerializedName("night_intailrate")
    @Expose
    private String nightIntailrate;
    @SerializedName("night_standardrate")
    @Expose
    private String nightStandardrate;
    @SerializedName("seat_capacity")
    @Expose
    private String seatCapacity;

    String fix_price;
    public String getFixPrice(){
        return fix_price;
    }
    public void setFixPrice(String fix_price){
        this.fix_price = fix_price;
    }
    String id;
    public String getId(){
        return id;
    }
    public void setId(String cabId){
        this.id = cabId;
    }

    boolean is_selected;
    public boolean getiIsSelected(){
        return is_selected;
    }
    public void setIsSelected(boolean is_selected){
        this.is_selected = is_selected;
    }

    String area_id;
    public String getAreaId(){
        return area_id;
    }
    public void setAreaId(String area_id){
        this.area_id = area_id;
    }


    public String getCabId() {
        return cabId;
    }

    public void setCabId(String cabId) {
        this.cabId = cabId;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getCarRate() {
        return carRate;
    }

    public void setCarRate(String carRate) {
        this.carRate = carRate;
    }

    public String getTransfertype() {
        return transfertype;
    }

    public void setTransfertype(String transfertype) {
        this.transfertype = transfertype;
    }

    public String getIntialkm() {
        return intialkm;
    }

    public void setIntialkm(String intialkm) {
        this.intialkm = intialkm;
    }

    public String getIntailrate() {
        return intailrate;
    }

    public void setIntailrate(String intailrate) {
        this.intailrate = intailrate;
    }

    public String getStandardrate() {
        return standardrate;
    }

    public void setStandardrate(String standardrate) {
        this.standardrate = standardrate;
    }

    public String getFromintialkm() {
        return fromintialkm;
    }

    public void setFromintialkm(String fromintialkm) {
        this.fromintialkm = fromintialkm;
    }

    public String getFromintailrate() {
        return fromintailrate;
    }

    public void setFromintailrate(String fromintailrate) {
        this.fromintailrate = fromintailrate;
    }

    public String getFromstandardrate() {
        return fromstandardrate;
    }

    public void setFromstandardrate(String fromstandardrate) {
        this.fromstandardrate = fromstandardrate;
    }

    public String getNightFromintialkm() {
        return nightFromintialkm;
    }

    public void setNightFromintialkm(String nightFromintialkm) {
        this.nightFromintialkm = nightFromintialkm;
    }

    public String getNightFromintailrate() {
        return nightFromintailrate;
    }

    public void setNightFromintailrate(String nightFromintailrate) {
        this.nightFromintailrate = nightFromintailrate;
    }

    public String getExtrahour() {
        return extrahour;
    }

    public void setExtrahour(String extrahour) {
        this.extrahour = extrahour;
    }

    public String getExtrakm() {
        return extrakm;
    }

    public void setExtrakm(String extrakm) {
        this.extrakm = extrakm;
    }

    public String getTimetype() {
        return timetype;
    }

    public void setTimetype(String timetype) {
        this.timetype = timetype;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getNightPackage() {
        return nightPackage;
    }

    public void setNightPackage(String nightPackage) {
        this.nightPackage = nightPackage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRideTimeRate() {
        return rideTimeRate;
    }

    public void setRideTimeRate(String rideTimeRate) {
        this.rideTimeRate = rideTimeRate;
    }

    public String getNightRideTimeRate() {
        return nightRideTimeRate;
    }

    public void setNightRideTimeRate(String nightRideTimeRate) {
        this.nightRideTimeRate = nightRideTimeRate;
    }

    public String getDaystarttime() {
        return daystarttime;
    }

    public void setDaystarttime(String daystarttime) {
        this.daystarttime = daystarttime;
    }

    public String getDayEndTime() {
        return dayEndTime;
    }

    public void setDayEndTime(String dayEndTime) {
        this.dayEndTime = dayEndTime;
    }

    public String getNightStartTime() {
        return nightStartTime;
    }

    public void setNightStartTime(String nightStartTime) {
        this.nightStartTime = nightStartTime;
    }

    public String getNightEndTime() {
        return nightEndTime;
    }

    public void setNightEndTime(String nightEndTime) {
        this.nightEndTime = nightEndTime;
    }

    public String getNightIntailrate() {
        return nightIntailrate;
    }

    public void setNightIntailrate(String nightIntailrate) {
        this.nightIntailrate = nightIntailrate;
    }

    public String getNightStandardrate() {
        return nightStandardrate;
    }

    public void setNightStandardrate(String nightStandardrate) {
        this.nightStandardrate = nightStandardrate;
    }

    public String getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

}
