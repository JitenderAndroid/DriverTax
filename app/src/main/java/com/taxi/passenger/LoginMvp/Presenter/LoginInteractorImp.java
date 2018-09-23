package com.taxi.passenger.LoginMvp.Presenter;

import android.content.Context;

import com.taxi.passenger.LoginMvp.LoginContractor.LoginInterfacesContractor;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;
import com.taxi.passenger.model.response.UserLoginDetailModel;


/**
 * Created by Abhilasha Yadav on 10/26/2017.
 */

public class LoginInteractorImp implements LoginInterfacesContractor.LoginInteractor, NetworkInterfaces.LoginWebServiceInterface {

    Context context;
    LoginPresenterImp mLoginPresenterImp;


    public LoginInteractorImp(LoginPresenterImp loginPresenterImp) {
        this.mLoginPresenterImp = loginPresenterImp;
    }

    @Override
    public void setCredential(String token, String username, String password) {
         WebService.getInstance().callLoginService(token, username, password, this);


    }

    @Override
    public void onSuccess(String status, String errorcode, UserLoginDetailModel userLoginDetail) {
        mLoginPresenterImp.onLoginSuccess(userLoginDetail.getStatus(), userLoginDetail);

    }

    @Override
    public void onFailure(Throwable t) {
        mLoginPresenterImp.onLoginFailure(t.getLocalizedMessage());
    }
}
