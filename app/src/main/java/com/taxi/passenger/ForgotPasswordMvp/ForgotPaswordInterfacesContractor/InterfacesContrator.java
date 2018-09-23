package com.taxi.passenger.ForgotPasswordMvp.ForgotPaswordInterfacesContractor;

/**
 * Created by Abhilasha Yadav on 11/3/2017.
 */

public interface InterfacesContrator {


    interface ForgotPasswordInteractor {
        void setEmailID( String email);
    }
    interface ForgotPasswordPresenter {
        void   getEmailId( String email);
        void   getMsgOnSuccess(String msg);
        void   onNeedRouting();
    }
   interface  ForgotPasswordRouter{

       void showNextActivity();

   }
}
