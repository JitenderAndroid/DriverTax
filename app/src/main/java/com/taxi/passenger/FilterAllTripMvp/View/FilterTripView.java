package com.taxi.passenger.FilterAllTripMvp.View;

import com.taxi.passenger.FilterAllTripMvp.Model.FilterTripBean;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public interface FilterTripView  {
    void showProgressFilterTrip();
    void hideProgressFilterTrip();
    void onResponce(FilterTripBean mFilterTripBean,int offset);

}
