package com.taxi.passenger.ForgotPasswordMvp.ForgetRouter;

import android.content.Intent;

import com.taxi.passenger.ForgotPasswordMvp.ForgotActivity;
import com.taxi.passenger.ForgotPasswordMvp.ForgotPaswordInterfacesContractor.InterfacesContrator;
import com.taxi.passenger.activity.MainActivity;

/**
 * Created by Abhilasha Yadav on 11/3/2017.
 */

public class ForgotpasswordRouterImp implements InterfacesContrator.ForgotPasswordRouter {
    ForgotActivity  forgotActivity;

    public ForgotpasswordRouterImp(ForgotActivity forgotActivity){
        this.forgotActivity = forgotActivity;


    }

    @Override
    public void showNextActivity() {

        Intent intent = new Intent(forgotActivity, MainActivity.class);
        forgotActivity.startActivity(intent);
        forgotActivity.finish();


        
    }
}
