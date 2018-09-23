package com.taxi.passenger.FilterAllTripMvp.InterfaceContractor;

import com.taxi.passenger.FilterAllTripMvp.Model.FilterTripBean;
import com.taxi.passenger.utils.AllTripFeed;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public interface FilterCabInterfaceContractor {

    public  interface  FilterCabPresenter{

        void  setParameters(String  userId, String off,String filter,int offset);
        void  afterSuccessResponceAllTrip(FilterTripBean allTripFeed  );
        void  afterFailureResponceAllTrip();
    }

    interface   FilterCabInteractor{
        void  setParametersForHitApi( String  userId, String off,String filter);
    }
}
