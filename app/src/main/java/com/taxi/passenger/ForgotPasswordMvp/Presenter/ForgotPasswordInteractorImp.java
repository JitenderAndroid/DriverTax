package com.taxi.passenger.ForgotPasswordMvp.Presenter;

import android.content.Context;
import android.util.Log;

import com.taxi.passenger.ForgotPasswordMvp.ForgotPaswordInterfacesContractor.InterfacesContrator;
import com.taxi.passenger.ForgotPasswordMvp.Model.ForgotPasswordModel;
import com.taxi.passenger.R;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;

/**
 * Created by Abhilasha Yadav on 10/27/2017.
 */

public class ForgotPasswordInteractorImp implements InterfacesContrator.ForgotPasswordInteractor,NetworkInterfaces.ForgotPasswordInterface {
    Context context;
    ForgotPasswordPresenterImp mforgotPasswordPresenterImp;


    public ForgotPasswordInteractorImp(ForgotPasswordPresenterImp forgotPasswordPresentorImp) {
        this.mforgotPasswordPresenterImp = forgotPasswordPresentorImp;
    }

    @Override
    public void setEmailID( String email) {
        WebService.getInstance().callForgotWebService(email,  this);

    }

    @Override
    public void onSuccess(ForgotPasswordModel forgotPasswordModel) {
        if (forgotPasswordModel != null){
            Log.d("ForgotPasswordIntimp",forgotPasswordModel.getStatus());
            mforgotPasswordPresenterImp.getMsgOnSuccess(forgotPasswordModel.getMessage());
        }

    }

    @Override
    public void onFailure() {
        String msg = context. getResources().getString(R.string.error_msg_forgotPass);
        mforgotPasswordPresenterImp.getMsgOnSuccess(msg);


    }
}
