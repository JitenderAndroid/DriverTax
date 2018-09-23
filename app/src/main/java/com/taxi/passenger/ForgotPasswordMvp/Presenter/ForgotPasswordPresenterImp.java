package com.taxi.passenger.ForgotPasswordMvp.Presenter;

import com.taxi.passenger.ForgotPasswordMvp.ForgetRouter.ForgotpasswordRouterImp;
import com.taxi.passenger.ForgotPasswordMvp.ForgotActivity;
import com.taxi.passenger.ForgotPasswordMvp.ForgotPaswordInterfacesContractor.InterfacesContrator;
import com.taxi.passenger.ForgotPasswordMvp.View.ForgotPasswordView;


/**
 * Created by Abhilasha Yadav on 10/27/2017.
 */

public class ForgotPasswordPresenterImp  implements InterfacesContrator.ForgotPasswordPresenter{


    private ForgotPasswordView forgotPasswordView;
    private InterfacesContrator. ForgotPasswordInteractor forgotPasswordInteractor;
     private   InterfacesContrator.ForgotPasswordRouter      ForgotPasswordRouter;

    public ForgotPasswordPresenterImp(ForgotPasswordView forgotActivity) {
        this.forgotPasswordView = forgotActivity;
        this.forgotPasswordInteractor= new ForgotPasswordInteractorImp(this);
        this. ForgotPasswordRouter = new ForgotpasswordRouterImp ((ForgotActivity)forgotActivity);


    }

    @Override
    public void getEmailId(String email) {
        forgotPasswordView.showProgress();
        forgotPasswordInteractor.setEmailID(email);

    }

    @Override
    public void getMsgOnSuccess(String msg) {
        forgotPasswordView.hideProgress();
        forgotPasswordView.onSuccessMsg(msg);


    }

    @Override
    public void onNeedRouting() {
        ForgotPasswordRouter.showNextActivity();
    }
}
