package com.taxi.passenger.SignUpMvp.Presenter;

import com.taxi.passenger.SignUpMvp.Model.RegisterDetailModel;
import com.taxi.passenger.SignUpMvp.View.SignUpView;
import okhttp3.RequestBody;

/**
 * Created by Abhilasha Yadav on 10/26/2017.
 */

public class SignUpPresenterImp implements SignUpPresenter{
    SignUpView mSignUpView;
    SignUpInteractor mSignUpInteractor;


    public SignUpPresenterImp(SignUpView mSignUpView) {
        this.mSignUpView = mSignUpView;
        this.mSignUpInteractor= new SignUpInteractorImp(this);
    }





    @Override


    public void getSignUpCredentials(RequestBody name, RequestBody email, RequestBody username, RequestBody mobile, RequestBody password, RequestBody isdevice, RequestBody token, RequestBody facebook_id, RequestBody twitter_id, RequestBody dob, RequestBody gender, RequestBody fileBody) {
            mSignUpView.showProgress();



        mSignUpInteractor.setSignUpCredentials(name,  email,  username, mobile, password,  isdevice,  token,  facebook_id,  twitter_id,  dob, gender,  fileBody);
        }

    @Override
       public void getResponceMsg(RegisterDetailModel registerDetailModel) {
        mSignUpView.hideProgress();
        mSignUpView.onSignUp(registerDetailModel);

    }
}
