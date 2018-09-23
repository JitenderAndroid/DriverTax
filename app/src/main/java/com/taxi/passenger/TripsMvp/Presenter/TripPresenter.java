package com.taxi.passenger.TripsMvp.Presenter;

import com.taxi.passenger.TripsMvp.Model.Bookingdetail;
import com.taxi.passenger.TripsMvp.Model.TripBookBean;

/**
 * Created by Abhilasha Yadav on 11/9/2017.
 */

public interface TripPresenter {
    void  toSetdataForGetResponce(Bookingdetail bookingdetail);
    void  tripBookingSuccess(TripBookBean mtripBookBean);
    void tripBookingFailure();
}
