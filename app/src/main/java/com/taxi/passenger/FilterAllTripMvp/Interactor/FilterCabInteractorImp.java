package com.taxi.passenger.FilterAllTripMvp.Interactor;

import com.taxi.passenger.FilterAllTripMvp.InterfaceContractor.FilterCabInterfaceContractor;
import com.taxi.passenger.FilterAllTripMvp.Model.FilterTripBean;
import com.taxi.passenger.FilterAllTripMvp.Presenter.FilterPresenterImp;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public class FilterCabInteractorImp implements FilterCabInterfaceContractor.FilterCabInteractor ,NetworkInterfaces.FilterTripByUserWebInterface{
   private FilterPresenterImp filterPresenterImp;

    public FilterCabInteractorImp(FilterPresenterImp filterPresenterImp) {
        this.filterPresenterImp= filterPresenterImp;

    }

    @Override
    public void setParametersForHitApi(String userId, String off, String filter) {
        WebService.getInstance().callFilterTrip(userId,off,filter,this);
    }

    @Override
    public void onSuccessFilterAllTrip(FilterTripBean allTripFeed) {
        filterPresenterImp.afterSuccessResponceAllTrip(allTripFeed);
    }

    @Override
    public void onFailureFilterAllTrip() {
        filterPresenterImp.afterFailureResponceAllTrip();

    }
}
