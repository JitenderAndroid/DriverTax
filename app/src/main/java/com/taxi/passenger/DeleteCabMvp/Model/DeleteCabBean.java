
package com.taxi.passenger.DeleteCabMvp.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteCabBean {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Isactive")
    @Expose
    private String isactive;
    @SerializedName("trip_detail")
    @Expose
    private List<TripDetail> tripDetail = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public List<TripDetail> getTripDetail() {
        return tripDetail;
    }

    public void setTripDetail(List<TripDetail> tripDetail) {
        this.tripDetail = tripDetail;
    }

}
