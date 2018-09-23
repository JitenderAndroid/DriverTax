package com.taxi.passenger.ForgotPasswordMvp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import com.taxi.passenger.ForgotPasswordMvp.Presenter.ForgotPasswordPresenterImp;
import com.taxi.passenger.ForgotPasswordMvp.View.ForgotPasswordView;
import com.taxi.passenger.R;
import com.taxi.passenger.utils.Common;

public class ForgotActivity extends AppCompatActivity implements ForgotPasswordView {

    TextView mTvForgotPassword, mTvPasswordLogo, mTvRetrievePassword;
    RelativeLayout mRlBackArrow, mRlRetrivePassword;
    EditText mEtUserName;
    Typeface openSansBold, robotoBold;
    Activity context;
    Dialog progressDialog;
    RotateLoading cusRotateLoading;
    ForgotPasswordPresenterImp mForgotPasswordPresenterImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        progressDialog = new Dialog(ForgotActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        initViews();
        initAssets();
        setTypeFace();
        mForgotPasswordPresenterImp = new ForgotPasswordPresenterImp(this);

        mRlRetrivePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEtUserName.getText().toString().trim().length() == 0) {
                    Common.showMkError(ForgotActivity.this, getResources().getString(R.string.please_enter_email));
                    return;
                } else if (mEtUserName.getText().toString().trim().length() != 0 && !isValidEmail(mEtUserName.getText().toString().trim())) {
                    Common.showMkError(ForgotActivity.this, getResources().getString(R.string.please_enter_valid_email));
                    return;
                }

                if (Common.isNetworkAvailable(ForgotActivity.this)) {
                    // here presenter get value for  hit api
                    mForgotPasswordPresenterImp.getEmailId(mEtUserName.getText().toString().trim());

                } else {
                    Common.showInternetInfo(ForgotActivity.this, getString(R.string.network_prob_msg));
                }

            }
        });

        mRlBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void setTypeFace() {
        mTvForgotPassword.setTypeface(openSansBold);
        mTvPasswordLogo.setTypeface(robotoBold);
        mTvRetrievePassword.setTypeface(robotoBold);
    }

    private void initAssets() {
        openSansBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        robotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

    }

    private void initViews() {
        mTvForgotPassword = findViewById(R.id.txt_forgot_password);
        mRlBackArrow = findViewById(R.id.layout_back_arrow);
        mRlRetrivePassword = findViewById(R.id.layout_retrive_password);
        mEtUserName = findViewById(R.id.edit_username);
        mTvPasswordLogo = findViewById(R.id.txt_for_pass_logo);
        mTvRetrievePassword = findViewById(R.id.txt_retrive_password);
        cusRotateLoading = progressDialog.findViewById(R.id.rotateloading_register);

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTvForgotPassword = null;
        mRlBackArrow = null;
        mEtUserName = null;
        mRlRetrivePassword = null;
        mForgotPasswordPresenterImp = null;

    }


    @Override
    public void showProgress() {
        progressDialog.show();
        cusRotateLoading.start();
    }

    @Override
    public void hideProgress() {
        progressDialog.cancel();
        cusRotateLoading.stop();
    }

    @Override
    public void onSuccessMsg(String msg) {
        Toast toast = Toast.makeText(ForgotActivity.this, msg, Toast.LENGTH_SHORT);
        toast.show();
        mForgotPasswordPresenterImp.onNeedRouting();

    }
}
