package com.taxi.passenger.LoginMvp.Presenter;


import com.taxi.passenger.LoginMvp.LoginContractor.LoginInterfacesContractor;
import com.taxi.passenger.LoginMvp.View.LoginView;
import com.taxi.passenger.model.response.UserLoginDetailModel;

/**
 * Created by Abhilasha Yadav on 10/26/2017.
 */

public class LoginPresenterImp implements LoginInterfacesContractor.LoginPresenter {



    private LoginView ILoginView;
    private LoginInterfacesContractor.LoginInteractor loginInteracter;


    public LoginPresenterImp(LoginView loginView) {
          this.ILoginView =loginView;
           this.loginInteracter= new LoginInteractorImp(this) ;
    }

    @Override
    public void getLoginCredentials(String token, String username, String Password) {
        ILoginView.showProgress();
        loginInteracter.setCredential(token,username, Password);
    }

    @Override
    public void onLoginSuccess(String msg , UserLoginDetailModel userLoginDetailModel ) {
        ILoginView.hideProgress();
        ILoginView.onLogin(msg,userLoginDetailModel);
    }

    @Override
    public void onLoginFailure(String s) {
        ILoginView.hideProgress();
    }
}
