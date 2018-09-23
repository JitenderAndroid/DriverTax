package com.taxi.passenger.ChangePasswordMvp.Presenter;

/**
 * Created by Abhilasha Yadav on 10/30/2017.
 */

public interface ChangePasswordPresenter {

    void getBody(String password , int  uid);
    void setSuccessMsg(String status, String msg);


}
