package com.taxi.passenger.FCM;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Abhilasha Yadav on 9/11/2017.
 */

public class MyFireBaseInstantIDService extends FirebaseInstanceIdService {

    String TAG= "MyFireBase";
    @Override
    public void onTokenRefresh() {


        String refershToken= FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,refershToken);
    }
}
