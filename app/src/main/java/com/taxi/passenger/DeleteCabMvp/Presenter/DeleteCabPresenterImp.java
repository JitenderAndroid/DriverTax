package com.taxi.passenger.DeleteCabMvp.Presenter;

import com.taxi.passenger.DeleteCabMvp.Interactor.DeleteCabInteractorImp;
import com.taxi.passenger.DeleteCabMvp.InterfaceProviderDC.InterfacesContractorDeletCab;
import com.taxi.passenger.DeleteCabMvp.Model.DeleteCabBean;
import com.taxi.passenger.DeleteCabMvp.View.DeleteCabView;
import com.taxi.passenger.activity.AllTripActivity;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public class DeleteCabPresenterImp implements InterfacesContractorDeletCab.PresenterDeleteCab{

  private  DeleteCabView deleteCabView;
  private InterfacesContractorDeletCab.InteractorDeleteCab   deleteCabInteractor;
  int mposition ;
    public DeleteCabPresenterImp(  DeleteCabView deleteCabView) {
        this.deleteCabView=deleteCabView;
        this.deleteCabInteractor= new DeleteCabInteractorImp(this);
    }

    @Override
    public void setParameters(String booking_id ,String  uid  ,int position ) {
        mposition = position;
      deleteCabInteractor.setParameterHitApi(booking_id, uid);
    }

    @Override
    public void afterSuccessResponceAllTrip(DeleteCabBean deleteCabBean ) {
     deleteCabView.hideProgressDeleteTrip();
     deleteCabView.onResponce(deleteCabBean,mposition);
    }

    @Override
    public void afterFailureResponceAllTrip() {
        deleteCabView.hideProgressDeleteTrip();
    }
}
