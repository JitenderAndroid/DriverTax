package com.taxi.passenger.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import com.taxi.passenger.ForgotPasswordMvp.ForgotActivity;
import com.taxi.passenger.LoginMvp.Presenter.LoginPresenterImp;
import com.taxi.passenger.LoginMvp.View.LoginView;
import com.taxi.passenger.R;
import com.taxi.passenger.gpsLocation.GPSTracker;
import com.taxi.passenger.interfaces.NetworkInterfaces;
import com.taxi.passenger.model.response.UserLoginDetailModel;
import com.taxi.passenger.utils.Common;

import static com.taxi.passenger.utils.Common.*;


public class LoginActivity extends AppCompatActivity implements  View.OnClickListener, LoginView {

    private EditText mEtUserName;
    private EditText mEtPassword;
    private RelativeLayout mRlSignIn;
    private RelativeLayout mRlForgot;
    private TextView mTvForgotPassword;
    private RelativeLayout mRlShowHide;
    private TextView mTvShowHide, mtvSignIn, mTvSignInLogo;
    private LinearLayout mLlLoginMain;
    double PickupLongtude;
    double PickupLatitude;
    private Typeface OpenSansRegular, OpenSansBold, regularRoboto, RobotoBold;

    private Dialog ProgressDialog;
    private RotateLoading cusRotateLoading;
    private Common common = new Common();
    SharedPreferences userPref;
    //Error Alert
    private RelativeLayout rlMainView;
    private TextView tvTitle;

    private boolean passwordShowHide = false;

    private String token;
    ProgressBar progressbarLogin;
    private LoginPresenterImp loginPresenterImp;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        initViews();
        initFonts();
        setfonts();

        ProgressDialog = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = ProgressDialog.findViewById(R.id.rotateloading_register);
        userPref = PreferenceManager.getDefaultSharedPreferences(this);
        loginPresenterImp = new LoginPresenterImp(this);

        progressbarLogin = (ProgressBar) findViewById(R.id.progressbarLogin);


        mRlSignIn.setOnClickListener(


                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (mEtUserName.getText().toString().trim().length() == 0) {
                            //showLoginRegisterMkError(LoginActivity.this, getResources().getString(R.string.please_enter_username));
                            showMKPanelError(LoginActivity.this, getResources().getString(R.string.please_enter_username), rlMainView, tvTitle, regularRoboto);
                            return;
                        } else if (mEtPassword.getText().toString().trim().length() == 0) {
                            showMKPanelError(LoginActivity.this, getResources().getString(R.string.please_enter_password), rlMainView, tvTitle, regularRoboto);
                            return;
                        } else if (mEtPassword.getText().toString().trim().length() < 6 || mEtPassword.getText().toString().trim().length() > 32) {
                            showMKPanelError(LoginActivity.this, getResources().getString(R.string.password_length), rlMainView, tvTitle, regularRoboto);
                            return;
                        }

                        if (isNetworkAvailable(LoginActivity.this)) {


                            token = FirebaseInstanceId.getInstance().getToken();
                            Log.d("Token", "" + token);
                            loginPresenterImp.getLoginCredentials(token, mEtUserName.getText().toString().trim(), mEtPassword.getText().toString().trim());


                        } else {
                            showInternetInfo(LoginActivity.this, "");
                        }
                    }
                });

        mRlForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fi = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(fi);
                //finish();
            }
        });

        mRlShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowHide) {
                    mTvShowHide.setText(getResources().getString(R.string.show));
                    mEtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordShowHide = false;
                } else {
                    mEtPassword.setTransformationMethod(new HideReturnsTransformationMethod());
                    mTvShowHide.setText(getResources().getString(R.string.hide));
                    passwordShowHide = true;
                }
                mEtPassword.setSelection(mEtPassword.getText().length());

            }
        });


        Common.ValidationGone(LoginActivity.this, rlMainView, mEtUserName);
        Common.ValidationGone(LoginActivity.this, rlMainView, mEtPassword);


    }

    private void setfonts() {

        mEtUserName.setTypeface(OpenSansRegular);
        mEtPassword.setTypeface(OpenSansRegular);
        mTvForgotPassword.setTypeface(OpenSansRegular);
        mtvSignIn.setTypeface(RobotoBold);
        mTvSignInLogo.setTypeface(RobotoBold);

    }

    private void initFonts() {
        OpenSansBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        OpenSansRegular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        regularRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        RobotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

    }

    private void initViews() {
        rlMainView = (RelativeLayout) findViewById(R.id.rlMainView);
        tvTitle =  findViewById(R.id.tvTitle);
        //mTvtime =  findViewById(R.id.idTvTime);
        mLlLoginMain = (LinearLayout) findViewById(R.id.layout_login_main);
        mEtUserName =  findViewById(R.id.edit_username);
        mEtPassword =  findViewById(R.id.edit_password);
        mRlSignIn = (RelativeLayout) findViewById(R.id.layout_signin);
        mRlForgot = (RelativeLayout) findViewById(R.id.layout_forgot);
        mTvForgotPassword =  findViewById(R.id.txt_forgot_pass);
        mRlShowHide = (RelativeLayout) findViewById(R.id.layout_show_hide);
        mTvShowHide =  findViewById(R.id.txt_hide_show);
        mtvSignIn =  findViewById(R.id.txt_signin);
        mTvSignInLogo =  findViewById(R.id.txt_sign_in_logo);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_signin: {
                break;
            }
            case R.id.layout_login_main: {
                if (rlMainView.getVisibility() == View.VISIBLE) {
                    if (!isFinishing()) {
                        TranslateAnimation slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -100);
                        slideUp.setDuration(10);
                        slideUp.setFillAfter(true);
                        rlMainView.startAnimation(slideUp);
                        slideUp.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                rlMainView.setVisibility(View.GONE);
                            }
                        });

                    }
                }

                break;
            }
            case R.id.layout_forgot: {
                break;
            }
            case R.id.layout_show_hide: {
                break;
            }
        }
    }

    @Override
    public void showProgress() {


        progressbarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {

        progressbarLogin.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLogin(String msg, UserLoginDetailModel userLoginDetailModel) {


        if (userLoginDetailModel.getStatus().equals("success")) {
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.show();


            Gson gson = new Gson();
            String stringCabDetail = gson.toJson(userLoginDetailModel.getCabDetails());
            String stringUserdetail = gson.toJson(userLoginDetailModel.getUserDetail());
            String stringTimeDetail = gson.toJson(userLoginDetailModel.getTimeDetail());
            String stringCountryDetail = gson.toJson(userLoginDetailModel.getCountryDetail());


            Log.d("LoginActivityCabDetail", stringCabDetail);
            Log.d("LoginActivityCabDetail", stringUserdetail);
            Log.d("LoginActivityCabDetail", stringTimeDetail);
            Log.d("LoginActivityCabDetail", stringCountryDetail);
            SharedPreferences.Editor userdetail = userPref.edit();
            userdetail.putString("userDetail", stringUserdetail);
            userdetail.commit();

            SharedPreferences.Editor cabDetail = userPref.edit();
            cabDetail.putString("cabDetail", stringCabDetail);
            cabDetail.commit();


            SharedPreferences.Editor timedetail = userPref.edit();
            timedetail.putString("timeDetail", stringTimeDetail);
            timedetail.commit();

            SharedPreferences.Editor countrydetail = userPref.edit();
            countrydetail.putString("countryDetail", stringCountryDetail);
            countrydetail.commit();


            Log.d("LoginActivityCabDetail", stringCabDetail);
            SharedPreferences.Editor name = userPref.edit();
            name.putString("cabDetail", stringCabDetail);
            name.commit();

            SharedPreferences.Editor isLogin = userPref.edit();
            isLogin.putString("isLogin", "1");
            isLogin.commit();


            SharedPreferences.Editor id = userPref.edit();
            id.putString("id", userLoginDetailModel.getUserDetail().getId());
            Log.d("LoginActivity id", userLoginDetailModel.getUserDetail().getId());
            id.commit();

            SharedPreferences.Editor name1 = userPref.edit();
            name1.putString("name", userLoginDetailModel.getUserDetail().getName());
            Log.d("LoginActivity name", userLoginDetailModel.getUserDetail().getName());
            name1.commit();

            SharedPreferences.Editor passwordPre = userPref.edit();
            passwordPre.putString("password", mEtPassword.getText().toString());

            passwordPre.commit();

            SharedPreferences.Editor username = userPref.edit();
            username.putString("username", userLoginDetailModel.getUserDetail().getUsername());
            Log.d("LoginActivity username", userLoginDetailModel.getUserDetail().getUsername());
            username.commit();

            SharedPreferences.Editor mobile = userPref.edit();
            mobile.putString("mobile", userLoginDetailModel.getUserDetail().getMobile());
            mobile.commit();

            SharedPreferences.Editor email = userPref.edit();
            email.putString("email", userLoginDetailModel.getUserDetail().getEmail());
            email.commit();


            SharedPreferences.Editor userImage = userPref.edit();
            userImage.putString("userImage", userLoginDetailModel.getUserDetail().getImage());
            userImage.commit();

            SharedPreferences.Editor dob = userPref.edit();
            dob.putString("date_of_birth", userLoginDetailModel.getUserDetail().getDob());
            dob.commit();

            SharedPreferences.Editor gender = userPref.edit();
            gender.putString("gender", userLoginDetailModel.getUserDetail().getGender());
            gender.commit();


            if (msg.equals("success")) {
                GPSTracker gpsTracker = new GPSTracker(LoginActivity.this);
                PickupLatitude = gpsTracker.getLatitude();
                PickupLongtude = gpsTracker.getLongitude();
                //  Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent hi = new Intent(LoginActivity.this, HomeActivity.class);
                hi.putExtra("PickupLatitude", PickupLatitude);
                hi.putExtra("PickupLongtude", PickupLongtude);
                startActivity(hi);
                finish();
            }

        } else {
            if (userLoginDetailModel.getStatus().equals("failed")) {
                Toast toast = Toast.makeText(LoginActivity.this, userLoginDetailModel.getMessage(), Toast.LENGTH_SHORT);
                toast.show();

            }
        }
    }
}