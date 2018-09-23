package com.taxi.passenger.activity;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.google.firebase.FirebaseApp;


import com.taxi.passenger.LoginMvp.Presenter.LoginPresenterImp;

import com.taxi.passenger.R;

import com.taxi.passenger.gpsLocation.GPSTracker;

import com.taxi.passenger.utils.Common;


import static com.taxi.passenger.utils.Common.isNetworkAvailable;


public class SplashActivity extends AppCompatActivity {


    ImageView mIvSplashScreen;
    ImageView mIvLocation;
    double PickupLongtude;
    double PickupLatitude;
    SharedPreferences userPref;
    ProgressBar progressbarLogin;
    Common common = new Common();
    GPSTracker gpsTracker;
    TranslateAnimation translation;
    private String token;
    private LoginPresenterImp loginPresenterImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        mIvSplashScreen = (ImageView) findViewById(R.id.img_splash_screen);
        mIvLocation = (ImageView) findViewById(R.id.img_location);
        progressbarLogin = (ProgressBar) findViewById(R.id.progressbarLogin);

        gpsTracker = new GPSTracker(SplashActivity.this);
        userPref = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);


        if (userPref.getString("isLogin", "").equals("1")) {
            if (isNetworkAvailable(SplashActivity.this)) {

                PickupLatitude = gpsTracker.getLatitude();
                PickupLongtude = gpsTracker.getLongitude();

                if (!userPref.getString("id", "").equals("")) {

                    Intent hi = new Intent(SplashActivity.this, HomeActivity.class);
                    hi.putExtra("PickupLatitude", PickupLatitude);
                    hi.putExtra("PickupLongtude", PickupLongtude);
                    startActivity(hi);
                    finish();

                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

            }
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }


}
