package com.taxi.passenger.CalculateDistanceMvp;

import com.taxi.passenger.CalculateDistanceMvp.Model.CalculateDistancebean;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public interface InterfaceProviderCalculation {
    interface CalculationPresenter {
        void calculateHitSetParameter(double dropLongitude,
                                      double dropLatitude,
                                      double pickupLongtude,
                                      double pickupLatitude);

        void onSuccessCalculationDistance(CalculateDistancebean calculateDistancebean);
    }


    interface CalculationInteractor {
        void calculateHitSetParameterInteractor(double dropLongitude,
                                                double dropLatitude,
                                                double pickupLongtude,
                                                double pickupLatitude);

    }
}
