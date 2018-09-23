package com.taxi.passenger.CalculateDistanceMvp.Presenter;

import com.taxi.passenger.CalculateDistanceMvp.InterfaceProviderCalculation;
import com.taxi.passenger.CalculateDistanceMvp.Model.CalculateDistancebean;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public class CalculationInteractorImp implements InterfaceProviderCalculation.CalculationInteractor,NetworkInterfaces. CalculationDistance{

    CalculationPresenterimp calculationPresenterimp;
    public CalculationInteractorImp(CalculationPresenterimp calculationPresenterimp) {
        this.calculationPresenterimp = calculationPresenterimp;
    }



    @Override
    public void calculateHitSetParameterInteractor(double dropLongitude, double dropLatitude, double pickupLongtude, double pickupLatitude) {
        WebService.getInstance().callCalculationDistanceApi (pickupLatitude + "," + pickupLongtude,dropLatitude + "," + dropLongitude ,this);
    }

    @Override
    public void calculationSuccess(CalculateDistancebean calculateDistancebean) {
        calculationPresenterimp.onSuccessCalculationDistance(calculateDistancebean);
    }

    @Override
    public void calculationFailure() {


    }
}
