package com.taxi.passenger.SignUpMvp.View;

import com.taxi.passenger.SignUpMvp.Model.RegisterDetailModel;

/**
 * Created by Abhilasha Yadav on 10/26/2017.
 */

public interface SignUpView {


    void showProgress();
    void hideProgress();
    void onSignUp(RegisterDetailModel registerDetailModel);
}
