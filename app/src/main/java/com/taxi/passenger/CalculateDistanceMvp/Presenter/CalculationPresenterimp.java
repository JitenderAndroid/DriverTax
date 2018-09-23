package com.taxi.passenger.CalculateDistanceMvp.Presenter;

import com.taxi.passenger.CalculateDistanceMvp.InterfaceProviderCalculation;
import com.taxi.passenger.CalculateDistanceMvp.InterfaceProviderCalculation.CalculationInteractor;
import com.taxi.passenger.CalculateDistanceMvp.Model.CalculateDistancebean;
import com.taxi.passenger.CalculateDistanceMvp.View.CalculationView;
import com.taxi.passenger.activity.HomeActivity;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public class CalculationPresenterimp implements InterfaceProviderCalculation.CalculationPresenter {
    private CalculationView calculationView;
    private InterfaceProviderCalculation.  CalculationInteractor calculationInteractor;

    public CalculationPresenterimp(CalculationView calculationView) {
        this.calculationView = calculationView;
        this.calculationInteractor = new CalculationInteractorImp(this);

    }

    @Override
    public void calculateHitSetParameter(double dropLongitude, double dropLatitude, double pickupLongtude, double pickupLatitude) {
   calculationInteractor.calculateHitSetParameterInteractor(dropLongitude,dropLatitude,pickupLongtude,pickupLatitude);

    }

    @Override
    public void onSuccessCalculationDistance(CalculateDistancebean calculateDistancebean) {
        calculationView.onResponceDistance(calculateDistancebean);

    }
}
