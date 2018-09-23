package com.taxi.passenger.AllTripFeedMvp.Presenter;

import com.taxi.passenger.AllTripFeedMvp.Interactor.AllTripInteractorImp;
import com.taxi.passenger.AllTripFeedMvp.InterfacesProvider.InterfacesContractor;
import com.taxi.passenger.AllTripFeedMvp.Model.AllTrips;
import com.taxi.passenger.AllTripFeedMvp.View.AllTripView;
import com.taxi.passenger.activity.AllTripActivity;
import com.taxi.passenger.utils.AllTripFeed;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public class AllTripPresenterImp implements InterfacesContractor.Presenter {
    AllTripView allTripView;
    InterfacesContractor.Interactor allTripInteractor;

    int offset =0;

    public AllTripPresenterImp(AllTripView allTripView) {
        this.allTripView= allTripView;
        this.allTripInteractor= new AllTripInteractorImp(this);
    }


    @Override
    public void setParameters(String userId, String off,int offsetValue) {
        offset =  offsetValue;
        allTripInteractor.setParametersForHitApi(userId,off);

    }

    @Override
    public void afterSuccessResponceAllTrip(AllTrips allTripFeed) {
        allTripView.hideProgressAllTrip();
        allTripView.onResponce(allTripFeed,offset);

    }

    @Override
    public void afterFailureResponceAllTrip() {
        allTripView.hideProgressAllTrip();

    }



}
