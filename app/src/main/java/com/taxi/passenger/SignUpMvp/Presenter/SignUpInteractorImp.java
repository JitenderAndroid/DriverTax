package com.taxi.passenger.SignUpMvp.Presenter;

import android.util.Log;

import com.taxi.passenger.SignUpMvp.Model.RegisterDetailModel;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;
import okhttp3.RequestBody;

/**
 * Created by Abhilasha Yadav on 10/26/2017.
 */

public class SignUpInteractorImp implements SignUpInteractor, NetworkInterfaces.RegisterWebServiceInterface {


    SignUpPresenterImp signUpPresenterImp;

    public SignUpInteractorImp(SignUpPresenterImp signUpPresenterImp) {
        this.signUpPresenterImp = signUpPresenterImp;
    }

    @Override
    public void setSignUpCredentials(RequestBody name, RequestBody email, RequestBody username, RequestBody mobile, RequestBody password, RequestBody isdevice, RequestBody token, RequestBody facebook_id, RequestBody twitter_id, RequestBody dob, RequestBody gender, RequestBody fileBody) {
        WebService.getInstance().callRegisterWebService(name, email, username, mobile, password, isdevice, token, facebook_id, twitter_id, dob, gender, fileBody, this);
    }

    @Override
    public void onSuccess(RegisterDetailModel registerDetailModel) {
        Log.d("success Regisration" ,registerDetailModel.getMessage());
        signUpPresenterImp.getResponceMsg(registerDetailModel);


    }

    @Override
    public void onFailure() {


    }


}
