package com.taxi.passenger.LoginMvp.View;

import com.taxi.passenger.model.response.UserLoginDetailModel;

/**
 * Created by Abhilasha Yadav on 10/13/2017.
 */

public interface LoginView {


    void showProgress();
    void hideProgress();
    void  onLogin(String msg  , UserLoginDetailModel userLoginDetailModel);

}
