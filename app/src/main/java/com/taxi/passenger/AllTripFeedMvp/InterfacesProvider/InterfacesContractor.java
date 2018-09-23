package com.taxi.passenger.AllTripFeedMvp.InterfacesProvider;

import com.taxi.passenger.AllTripFeedMvp.Model.AllTrips;
import com.taxi.passenger.utils.AllTripFeed;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public interface InterfacesContractor {

    public interface  Presenter{

        void  setParameters(String  userId, String off,int offset);
        void  afterSuccessResponceAllTrip(AllTrips allTripFeed);
        void  afterFailureResponceAllTrip();


    }

    public  interface  Interactor{

        void  setParametersForHitApi( String userId, String off);


    }
}
