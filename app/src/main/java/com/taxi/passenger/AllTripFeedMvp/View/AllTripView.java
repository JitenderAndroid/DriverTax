package com.taxi.passenger.AllTripFeedMvp.View;

import com.taxi.passenger.AllTripFeedMvp.Model.AllTrips;
import com.taxi.passenger.utils.AllTripFeed;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public interface AllTripView  {
 /*   void showProgressAllTrip();*/
    void hideProgressAllTrip();
    void onResponce(AllTrips mAllTripFeed ,int offset);
}
