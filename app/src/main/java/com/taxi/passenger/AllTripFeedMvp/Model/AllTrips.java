
package com.taxi.passenger.AllTripFeedMvp.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllTrips {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Isactive")
    @Expose
    private String isactive;
    @SerializedName("all_trip")
    @Expose
    private List<AllTrip> allTrip = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public List<AllTrip> getAllTrip() {
        return allTrip;
    }

    public void setAllTrip(List<AllTrip> allTrip) {
        this.allTrip = allTrip;
    }

}
