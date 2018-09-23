package com.taxi.passenger.FilterAllTripMvp.Presenter;

import com.taxi.passenger.FilterAllTripMvp.Interactor.FilterCabInteractorImp;
import com.taxi.passenger.FilterAllTripMvp.InterfaceContractor.FilterCabInterfaceContractor;
import com.taxi.passenger.FilterAllTripMvp.Model.FilterTripBean;
import com.taxi.passenger.FilterAllTripMvp.View.FilterTripView;
import com.taxi.passenger.activity.AllTripActivity;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public class FilterPresenterImp implements FilterCabInterfaceContractor.FilterCabPresenter {
   int offset;
   FilterTripView filterTripView;
   FilterCabInterfaceContractor.FilterCabInteractor filterCabInteractor;

    public FilterPresenterImp(FilterTripView filterTripView) {
        this.filterTripView = filterTripView;
         this.filterCabInteractor= new FilterCabInteractorImp(this);
    }

    @Override
    public void setParameters(String userId, String off, String filter ,int offset) {
      this.offset= offset;
    }

    @Override
    public void afterSuccessResponceAllTrip(FilterTripBean allTripFeed ) {
        filterTripView.hideProgressFilterTrip();
        filterTripView.onResponce(allTripFeed,offset);
    }

    @Override
    public void afterFailureResponceAllTrip() {
        filterTripView.hideProgressFilterTrip();

    }
}
