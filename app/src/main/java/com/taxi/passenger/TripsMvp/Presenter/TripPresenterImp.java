package com.taxi.passenger.TripsMvp.Presenter;

import com.taxi.passenger.TripsMvp.Model.Bookingdetail;
import com.taxi.passenger.TripsMvp.Model.TripBookBean;
import com.taxi.passenger.TripsMvp.TripRouter.TripRoutingImp;
import com.taxi.passenger.TripsMvp.View.TripView;


/**
 * Created by Abhilasha Yadav on 11/9/2017.
 */

public class TripPresenterImp implements TripPresenter {

    TripView mtripView;
    TripRoutingImp mtripRoutingImp;
    TripIntractor  mTripInteractor;


    public TripPresenterImp(TripView mtripView) {
        this.mtripView= mtripView;
        this.mtripRoutingImp = new TripRoutingImp();
        this.mTripInteractor =new TripInteractorImp(this);
    }

    @Override
    public void toSetdataForGetResponce(Bookingdetail bookingdetail) {
        mtripView.showProgress();
        mTripInteractor.toSetDataForResponce(bookingdetail);


    }

    @Override
    public void tripBookingSuccess(TripBookBean mtripBookBean) {
        mtripView.hideProgress();
        mtripView.onResponce(mtripBookBean);


    }

    @Override
    public void tripBookingFailure() {
        //mtripView.hideProgress();


    }
}
