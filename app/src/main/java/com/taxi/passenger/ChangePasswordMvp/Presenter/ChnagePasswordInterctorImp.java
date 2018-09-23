package com.taxi.passenger.ChangePasswordMvp.Presenter;

import android.util.Log;

import com.taxi.passenger.ChangePasswordMvp.Model.ChangePasswordModel;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;

/**
 * Created by Abhilasha Yadav on 10/30/2017.
 */

public class ChnagePasswordInterctorImp  implements  ChangePasswordInterector, NetworkInterfaces.ChangePassword{


    ChangePasswordPresenterImp changePasswordPresenterImp;

    public ChnagePasswordInterctorImp(ChangePasswordPresenterImp changePasswordPresenterImp) {
        this.changePasswordPresenterImp=changePasswordPresenterImp;
    }

    @Override
    public void setBody(String password, int uid) {
        WebService.getInstance().callChangePassword(password,uid,this);
    }

    @Override
    public void onSuccess(ChangePasswordModel changePasswordModel) {
        Log.d("success",changePasswordModel.getStatus());
        changePasswordPresenterImp.setSuccessMsg(changePasswordModel.getStatus(),changePasswordModel.getStatus());
    }

    @Override
    public void onFailure() {
        changePasswordPresenterImp.setSuccessMsg("", "there is problem");

    }
}
