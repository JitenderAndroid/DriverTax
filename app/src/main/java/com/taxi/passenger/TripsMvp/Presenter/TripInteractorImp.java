package com.taxi.passenger.TripsMvp.Presenter;

import android.util.Log;

import com.taxi.passenger.TripsMvp.Model.Bookingdetail;
import com.taxi.passenger.TripsMvp.Model.TripBookBean;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;

/**
 * Created by Abhilasha Yadav on 11/9/2017.
 */

public class TripInteractorImp implements  TripIntractor,NetworkInterfaces.BookTrip {

       TripPresenterImp tripPresenterImp;

    public TripInteractorImp(TripPresenterImp tripPresenterImp) {
        this.tripPresenterImp = tripPresenterImp;
    }

    @Override
    public void toSetDataForResponce(Bookingdetail tripBookBean) {
        WebService.getInstance().callTripBookBeanCall(tripBookBean,this);
    }

    @Override
    public void onSuccess(TripBookBean mtripBookBean) {
        Log.d("TripBookBean",""+mtripBookBean);
        if(mtripBookBean!=null) {
            tripPresenterImp.tripBookingSuccess(mtripBookBean);
        }
        else{
            mtripBookBean.setStatus("null");
            tripPresenterImp.tripBookingSuccess(mtripBookBean);
        }

    }

    @Override
    public void onFailure() {

    }
}
