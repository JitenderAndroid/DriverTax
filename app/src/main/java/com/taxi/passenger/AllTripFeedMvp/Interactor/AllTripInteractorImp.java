package com.taxi.passenger.AllTripFeedMvp.Interactor;

import com.taxi.passenger.AllTripFeedMvp.InterfacesProvider.InterfacesContractor;
import com.taxi.passenger.AllTripFeedMvp.Model.AllTrips;
import com.taxi.passenger.AllTripFeedMvp.Presenter.AllTripPresenterImp;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public class AllTripInteractorImp implements InterfacesContractor.Interactor,NetworkInterfaces.AllTripByUserWebInterface {
  private   AllTripPresenterImp allTripPresenterImp;

    public AllTripInteractorImp(AllTripPresenterImp allTripPresenterImp) {

        this.allTripPresenterImp= allTripPresenterImp;
    }

    @Override
    public void setParametersForHitApi(String userId, String off) {

        WebService.getInstance().callAllTripByUser(userId,
                off,this);

    }


    @Override
    public void onSuccessAllTrip(AllTrips allTripFeed) {
        allTripPresenterImp.afterSuccessResponceAllTrip(allTripFeed);

    }

    @Override
    public void onFailureAllTrip() {
        allTripPresenterImp.afterFailureResponceAllTrip();

    }
}
