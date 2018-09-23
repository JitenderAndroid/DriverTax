package com.taxi.passenger.CalculateDistanceMvp.View;

import com.taxi.passenger.CalculateDistanceMvp.Model.CalculateDistancebean;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public interface CalculationView {
    void   onResponceDistance(CalculateDistancebean calculateDistancebean);
    void hideProgressHideProgress();
}
