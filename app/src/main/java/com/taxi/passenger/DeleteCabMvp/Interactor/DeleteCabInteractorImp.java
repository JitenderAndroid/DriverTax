package com.taxi.passenger.DeleteCabMvp.Interactor;

import com.taxi.passenger.DeleteCabMvp.InterfaceProviderDC.InterfacesContractorDeletCab;
import com.taxi.passenger.DeleteCabMvp.Model.DeleteCabBean;
import com.taxi.passenger.DeleteCabMvp.Presenter.DeleteCabPresenterImp;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public class DeleteCabInteractorImp implements InterfacesContractorDeletCab.InteractorDeleteCab  ,NetworkInterfaces.DeleteCabWebInterface{
    DeleteCabPresenterImp deleteCabPresenterImp;
    public DeleteCabInteractorImp(DeleteCabPresenterImp deleteCabPresenterImp) {
      this.deleteCabPresenterImp=deleteCabPresenterImp;

    }

    @Override
    public void setParameterHitApi(String booking_id ,String  uid  ) {
        WebService.getInstance().callDeletebooking( booking_id ,  uid ,this );
    }

    @Override
    public void onSuccessDeleteTrip(DeleteCabBean deleteCabBean) {
        deleteCabPresenterImp.afterSuccessResponceAllTrip(deleteCabBean);

    }

    @Override
    public void onFailureDeleteTrip() {
        deleteCabPresenterImp.afterFailureResponceAllTrip();

    }
}
