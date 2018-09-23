package com.taxi.passenger.SignUpMvp.Presenter;

import com.taxi.passenger.SignUpMvp.Model.RegisterDetailModel;
import okhttp3.RequestBody;

/**
 * Created by Abhilasha Yadav on 10/26/2017.
 */

public interface SignUpPresenter {
    void   getSignUpCredentials(RequestBody name, RequestBody email, RequestBody username, RequestBody mobile, RequestBody password, RequestBody isdevice, RequestBody token, RequestBody facebook_id, RequestBody twitter_id, RequestBody dob, RequestBody gender, RequestBody fileBody);

    void getResponceMsg(RegisterDetailModel registerDetailModel);
}
