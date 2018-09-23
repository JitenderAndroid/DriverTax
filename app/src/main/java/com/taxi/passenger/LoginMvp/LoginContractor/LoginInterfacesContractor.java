package com.taxi.passenger.LoginMvp.LoginContractor;

import com.taxi.passenger.model.response.UserLoginDetailModel;

/**
 * Created by Abhilasha Yadav on 11/3/2017.
 */

public interface LoginInterfacesContractor {


     interface LoginPresenter {
        void   getLoginCredentials(String token, String username, String Password);

        void onLoginSuccess(String msg, UserLoginDetailModel userLoginDetailModel );
        void onLoginFailure(String s);


    }

    interface LoginInteractor {
        void setCredential(String token, String username, String password);
    }

}
