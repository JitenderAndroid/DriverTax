package com.taxi.passenger.ChangePasswordMvp.Presenter;

import com.taxi.passenger.ChangePasswordMvp.View.ChangePasswordView;

/**
 * Created by Abhilasha Yadav on 10/30/2017.
 */

public class ChangePasswordPresenterImp implements  ChangePasswordPresenter {

     ChangePasswordView   changePasswordView;
     ChangePasswordInterector changePasswordInteractor;

    public ChangePasswordPresenterImp(ChangePasswordView changePasswordView) {
        this.changePasswordView= changePasswordView;
        this.changePasswordInteractor = new ChnagePasswordInterctorImp(this);
    }


    @Override
    public void getBody(String password, int uid) {
        changePasswordView.showProgress();
        changePasswordInteractor.setBody(password,uid);


    }

    @Override
    public void setSuccessMsg(String msg, String status) {
        changePasswordView.hideProgress();
        changePasswordView.onSuccessMsg( msg, status);

    }
}
