package com.taxi.passenger.DeleteCabMvp.InterfaceProviderDC;

import com.taxi.passenger.DeleteCabMvp.Model.DeleteCabBean;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public interface InterfacesContractorDeletCab   {

     interface   PresenterDeleteCab {

        void  setParameters(String booking_id ,String  uid ,int position );
        void  afterSuccessResponceAllTrip(DeleteCabBean deleteCabBean);
        void  afterFailureResponceAllTrip();

    }

      interface InteractorDeleteCab{
        void setParameterHitApi(String booking_id ,String  uid  );

    }
}
