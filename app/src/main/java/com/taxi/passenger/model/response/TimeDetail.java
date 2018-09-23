
package com.taxi.passenger.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeDetail {

    @SerializedName("tid")
    @Expose
    private String tid;
    @SerializedName("day_start_time")
    @Expose
    private String dayStartTime;
    @SerializedName("day_end_time")
    @Expose
    private String dayEndTime;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getDayStartTime() {
        return dayStartTime;
    }

    public void setDayStartTime(String dayStartTime) {
        this.dayStartTime = dayStartTime;
    }

    public String getDayEndTime() {
        return dayEndTime;
    }

    public void setDayEndTime(String dayEndTime) {
        this.dayEndTime = dayEndTime;
    }

}
