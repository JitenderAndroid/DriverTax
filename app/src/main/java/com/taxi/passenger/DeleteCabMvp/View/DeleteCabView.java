package com.taxi.passenger.DeleteCabMvp.View;

import com.taxi.passenger.DeleteCabMvp.Model.DeleteCabBean;

/**
 * Created by Abhilasha Yadav on 12/12/2017.
 */

public interface DeleteCabView {


    //void showProgressDeleteTrip();
    void hideProgressDeleteTrip();
    void onResponce(DeleteCabBean deleteCabBean ,int position) ;
}
