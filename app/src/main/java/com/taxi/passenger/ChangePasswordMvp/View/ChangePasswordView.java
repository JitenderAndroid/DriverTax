package com.taxi.passenger.ChangePasswordMvp.View;

/**
 * Created by Abhilasha Yadav on 10/30/2017.
 */

public interface ChangePasswordView {

    void showProgress();
    void hideProgress();
    void onSuccessMsg(String s, String msg);
}
