package com.taxi.passenger.TripsMvp.View;

import com.taxi.passenger.TripsMvp.Model.TripBookBean;

/**
 * Created by Abhilasha Yadav on 11/9/2017.
 */

public interface TripView {

    void showProgress();
    void hideProgress();
    void onResponce(TripBookBean mtripBookBean);


}
