package com.taxi.passenger.ChangePasswordMvp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import com.taxi.passenger.ChangePasswordMvp.Presenter.ChangePasswordPresenterImp;
import com.taxi.passenger.ChangePasswordMvp.View.ChangePasswordView;
import com.taxi.passenger.R;
import com.taxi.passenger.activity.HomeActivity;
import com.taxi.passenger.activity.MainActivity;
import com.taxi.passenger.slidingMenu.SlidingMenu;
import com.taxi.passenger.utils.Common;

import static com.taxi.passenger.utils.Common.showMKPanelError;

public class ChangePasswordActivity extends AppCompatActivity implements ChangePasswordView{
  Activity  activity;
    private TextView mTvChangePassword, mTvChangePasswordLogo, mTvChangePasswordButton;
    private EditText mEtCurrentPassword;
    private EditText mEtNewPassword;
    private EditText mEtConfirmPassword;
    private RelativeLayout mRlChangePasswordButton;
    private RelativeLayout mRlMenu;
    private Typeface OpenSans_Regular, OpenSans_Bold, regularRoboto, Roboto_Bold;
    private SlidingMenu slidingMenu;
    private SharedPreferences mUserPref;
    private Dialog ProgressDialog;
    private RotateLoading cusRotateLoading;
    //Error Alert
    private RelativeLayout mRlMainView;
    private TextView mTvTitle;
    ChangePasswordPresenterImp changePasswordPresenterImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ProgressDialog = new Dialog(ChangePasswordActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        initViews();
         changePasswordPresenterImp= new ChangePasswordPresenterImp(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int) getResources().getDimension(R.dimen.height_50), 0, 0);
        mRlMainView.setLayoutParams(params);

        mUserPref = PreferenceManager.getDefaultSharedPreferences(ChangePasswordActivity.this);

        initAssets();
        setTypeFace();

        mRlChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("password", "Login password = " + mUserPref.getString("password", ""));
                if (mEtCurrentPassword.getText().toString().trim().length() == 0) {
                    showMKPanelError(ChangePasswordActivity.this, getResources().getString(R.string.please_enter_current_password), mRlMainView, mTvTitle, regularRoboto);
                    mEtCurrentPassword.requestFocus();
                    return;
                } else if (!mEtCurrentPassword.getText().toString().trim().equals(mUserPref.getString("password", ""))) {
                    showMKPanelError(ChangePasswordActivity.this, getResources().getString(R.string.please_current_password), mRlMainView, mTvTitle, regularRoboto);
                    mEtCurrentPassword.requestFocus();
                    return;
                } else if (mEtNewPassword.getText().toString().trim().length() == 0) {
                    showMKPanelError(ChangePasswordActivity.this, getResources().getString(R.string.please_enter_new_password), mRlMainView, mTvTitle, regularRoboto);
                    mEtNewPassword.requestFocus();
                    return;
                } else if (mEtNewPassword.getText().toString().trim().length() < 6 || mEtNewPassword.getText().toString().trim().length() > 32) {
                    showMKPanelError(ChangePasswordActivity.this, getResources().getString(R.string.password_new_length), mRlMainView, mTvTitle, regularRoboto);
                    mEtNewPassword.requestFocus();
                    return;
                } else if (mEtConfirmPassword.getText().toString().trim().length() == 0) {
                    showMKPanelError(ChangePasswordActivity.this, getResources().getString(R.string.please_enter_confirm_password), mRlMainView, mTvTitle, regularRoboto);
                    mEtConfirmPassword.requestFocus();
                    return;
                } else if (!mEtNewPassword.getText().toString().equals(mEtConfirmPassword.getText().toString())) {
                    showMKPanelError(ChangePasswordActivity.this, getResources().getString(R.string.password_new_confirm), mRlMainView, mTvTitle, regularRoboto);
                    mEtConfirmPassword.requestFocus();
                    return;
                }

                if (Common.isNetworkAvailable(ChangePasswordActivity.this)) {

                    changePasswordPresenterImp.getBody(mEtNewPassword.getText().toString().trim(),Integer.parseInt(mUserPref.getString("id", "")));

                }
            }
        });

        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffsetRes(R.dimen.slide_menu_width);
        slidingMenu.setFadeDegree(0.20f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.left_menu);

        Common common = new Common();
        common.SlideMenuDesign(slidingMenu, ChangePasswordActivity.this, "change password");

        mRlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });

        Common.ValidationGone(ChangePasswordActivity.this, mRlMainView, mEtCurrentPassword);
        Common.ValidationGone(ChangePasswordActivity.this, mRlMainView, mEtNewPassword);
        Common.ValidationGone(ChangePasswordActivity.this, mRlMainView, mEtConfirmPassword);


    }

    private void setTypeFace() {
        mTvChangePassword.setTypeface(OpenSans_Bold);
        mEtNewPassword.setTypeface(OpenSans_Regular);
        mEtConfirmPassword.setTypeface(OpenSans_Regular);
        mEtCurrentPassword.setTypeface(OpenSans_Regular);
        mTvChangePasswordLogo.setTypeface(Roboto_Bold);
        mTvChangePasswordButton.setTypeface(Roboto_Bold);
    }

    private void initAssets() {
        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        regularRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

    }

    private void initViews() {
        mRlMainView =findViewById(R.id.rlMainView);
        mTvTitle =  findViewById(R.id.tvTitle);
        mTvChangePassword =  findViewById(R.id.txt_change_password);
        mEtCurrentPassword =  findViewById(R.id.edit_current_pass);
        mEtNewPassword =  findViewById(R.id.edit_new_pass);
        mEtConfirmPassword =  findViewById(R.id.edit_con_pass);
        mRlChangePasswordButton =  findViewById(R.id.layout_change_password_button);
        mRlMenu =findViewById(R.id.layout_menu);
        mTvChangePasswordLogo =  findViewById(R.id.txt_change_password_logo);
        mTvChangePasswordButton =  findViewById(R.id.txt_change_pass);
        cusRotateLoading = ProgressDialog.findViewById(R.id.rotateloading_register);

    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent( ChangePasswordActivity.this,HomeActivity.class);
        startActivity(in);
        finish();

    }

    @Override
    public void showProgress() {
        ProgressDialog.show();
        cusRotateLoading.start();
    }

    @Override
    public void hideProgress() {

        ProgressDialog.cancel();
        cusRotateLoading.stop();

    }

    @Override
    public void onSuccessMsg(String s, String msg) {
        Toast toast = Toast.makeText(ChangePasswordActivity.this, msg ,Toast.LENGTH_LONG);
        toast.show();

        if (s.equals("success")) {
            Common.showMkSucess(ChangePasswordActivity.this, getResources().getString(R.string.password_change_sucess), "yes");
            Intent in= new Intent(ChangePasswordActivity.this,HomeActivity.class);
            startActivity(in);

            SharedPreferences.Editor newPass = mUserPref.edit();
            newPass.putString("password", mEtNewPassword.getText().toString().trim());
            newPass.commit();

        } else if (s.equals("false")) {


            SharedPreferences.Editor editor = mUserPref.edit();
            editor.clear();
            editor.commit();

            Intent logInt = new Intent(ChangePasswordActivity.this, MainActivity.class);

            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            logInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logInt);
            finish();
        }

    }
}
