package com.taxi.passenger.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import com.victor.loading.rotate.RotateLoading;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;

import com.taxi.passenger.R;
import com.taxi.passenger.SignUpMvp.SignUpActivity;
import com.taxi.passenger.gpsLocation.GPSTracker;
import com.taxi.passenger.utils.Common;
import com.taxi.passenger.utils.Url;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;
@ReportsCrashes( mailTo = "abhilasha@yopmail.com", mode = ReportingInteractionMode.TOAST, resToastText = R.string.toast_crash)
public class MainActivity extends AppCompatActivity {

    ImageView mIvLogo, mIvBackground;
    ImageView mIvFacebook;
    ImageView mIvTwitter;
    TextView mTvSignInWith, mTvNewUser, TvSignIn;
    RelativeLayout mRlOptionMain, mRlNewUserSignUp, mRlSignIn;

    Typeface OpenSans_Regular,regularRoboto,Roboto_Bold;

    CallbackManager callbackManager;

    Dialog ProgressDialog;
    RotateLoading cusRotateLoading;

    TwitterLoginButton twitterLoginBtn;
    int socialFlg;
    SharedPreferences userPref;

    double PickupLongtude;
    double PickupLatitude;

    //Error Alert
    RelativeLayout rlMainView;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);


        ACRA.init(getApplication());

        setContentView(R.layout.activity_login_option);

        initTypeFace();

        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
        PickupLatitude = gpsTracker.getLatitude();
        PickupLongtude = gpsTracker.getLongitude();

        initViews();
        setTypeFace();

        userPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        ProgressDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = ProgressDialog.findViewById(R.id.rotateloading_register);

        callbackManager = CallbackManager.Factory.create();



        Picasso.with(MainActivity.this)
                .load(R.drawable.facebook_btn)
                .into(mIvFacebook);


        Picasso.with(MainActivity.this)
                .load(R.drawable.twitter_btn)
                .into(mIvTwitter);

        mRlNewUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent si = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(si);
                finish();

            }
        });

        mRlSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent li = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(li);
                finish();
            }
        });


        mIvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean loggedIn = AccessToken.getCurrentAccessToken() != null;

                if(loggedIn){
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    // App code
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    Log.d("object", "object = " + object + "=="+response);

                                    if(object!=null){
                                        //facebook get data
                                        try {
                                            String FbEmail = "";
                                            String FbName = "";
                                            if(object.has("email"))
                                                FbEmail = object.getString("email");
                                            if(object.has("name"))
                                                FbName = object.getString("name");

                                            //CheckFacebookUser(Url.FACEBOOK_LOGIN_URL,object.getString("id"),"",FbEmail,FbName);
                                            new CheckFacebookUserHttp(Url.FACEBOOK_LOGIN_URL,object.getString("id"),"",FbEmail,FbName).execute();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(MainActivity.this, getString(R.string.error_somthing_went_gone),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields","id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();


                }else{

                    callbackManager = CallbackManager.Factory.create();

                    LoginManager.getInstance().logInWithPublishPermissions(MainActivity.this, Collections.singletonList("publish_actions"));

                    LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(final LoginResult loginResult) {

                            Log.d("loginResult", "loginResult = " + loginResult);
                            // App code
                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(
                                                JSONObject object,
                                                GraphResponse response) {
                                            Log.d("object", "object = " + object + "==" + response);

                                            if (object != null) {
                                                //facebook get data
                                                //object.getString("id")

                                                try {
                                                    String FbEmail = "";
                                                    String FbName = "";
                                                    if(object.has("email"))
                                                        FbEmail = object.getString("email");
                                                    if(object.has("name"))
                                                        FbName = object.getString("name");

                                                    String FacebookSocialUrl = Url.FACEBOOK_LOGIN_URL +"?facebook_id="+object.getString("id");
                                                    //CheckFacebookUser(FacebookSocialUrl,object.getString("id"),"",FbEmail,FbName);
                                                    new CheckFacebookUserHttp(Url.FACEBOOK_LOGIN_URL,object.getString("id"),"",FbEmail,FbName).execute();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                Toast.makeText(MainActivity.this, getString(R.string.error_somthing_went_gone), Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            Log.d("cancel", "cancel = ");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.d("fb error", "fb error = " + exception.getMessage());
                            System.out.println("exception >>" + exception.getMessage());
                        }
                    });
                }

            }
        });

        mIvTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isNetworkAvailable(MainActivity.this)){
                    try{

                        socialFlg = 2;
                        twitterLoginBtn = new TwitterLoginButton(MainActivity.this);

                        twitterLoginBtn.setCallback(new Callback<TwitterSession>() {
                            @Override
                            public void success(Result<TwitterSession> result) {
                       Log.d("twitter", "twitter = " + result.toString());
                            }
                            @Override
                            public void failure(TwitterException exception) {
                                // Do something on failure
                                Log.d("twitter", "twitter erro = " + exception.getMessage());
                            }
                        });

                        TwitterAuthClient authClient = new TwitterAuthClient();
                        authClient.authorize(MainActivity.this, new Callback<TwitterSession>() {
                            @Override
                            public void success(Result<TwitterSession> twitterSessionResult) {
                                TwitterSession user = twitterSessionResult.data;
                                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                                TwitterAuthToken authToken = session.getAuthToken();

                                String token = authToken.token;
                                String secret = authToken.secret;

                                new CheckFacebookUserHttp(Url.TWITTER_LOGIN_URL,"",String.valueOf(session.getUserId()),"",session.getUserName()).execute();
//                                TwitterApiClient twitterApiClient = Twitter.getApiClient();
//                                StatusesService twapiclient = twitterApiClient.getStatusesService();
//                                twapiclient.userTimeline(user.getUserId(), null, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {
//                                    @Override
//                                    public void success(Result<List<Tweet>> listResult) {
//
//                                        String twitterPrfImageUrl = listResult.data.get(0).user.profileImageUrl;
//                                        twitterPrfImageUrl = twitterPrfImageUrl.replace("_normal","_bigger");
//
//                                        String twitterUsername="";
//                                        if(!listResult.data.get(0).user.name.equals(""))
//                                            twitterUsername = listResult.data.get(0).user.name;
//
//                                        String twitterId = String.valueOf(listResult.data.get(0).user.id);
//
////                                        RequestParams socialParams = new RequestParams();
////                                        socialParams.put("twitter_id", twitterId);
////
////                                        //String twitterUrl = Url.twitterUrl + "?sign=" + ss.sign + "&salt=" + ss.salt + "&twitter_id=" + listResult.data.get(0).user.id + "&username=" +listResult.data.get(0).user.name+"&device_token="+common.device_token;
////                                        Log.d("Twitter Url","Twitter Url = "+Url.TWITTER_LOGIN_URL+"?"+socialParams);
////                                        CheckFacebookUser(Url.TWITTER_LOGIN_URL, socialParams, "", twitterId, "", twitterUsername);
//                                        new CheckFacebookUserHttp(Url.TWITTER_LOGIN_URL,"",twitterId,"",twitterUsername).execute();
//                                    }
//
//                                    @Override
//                                    public void failure(TwitterException e) {
//
//                                    }
//                                });


                            }
                            @Override
                            public void failure(TwitterException e) {
                                System.out.println("Twitter Auth is failure");
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Common.showInternetInfo(MainActivity.this,"");
                }
            }
        });


        mRlOptionMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rlMainView.getVisibility() == View.VISIBLE){
                    if(!isFinishing()){
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
            }
        });
    }

    private void setTypeFace() {
        mTvNewUser.setTypeface(Roboto_Bold);
        TvSignIn.setTypeface(Roboto_Bold);
        mTvSignInWith.setTypeface(OpenSans_Regular);

    }

    private void initViews() {
        //Error Alert
        rlMainView=(RelativeLayout)findViewById(R.id.rlMainView);
        tvTitle=findViewById(R.id.tvTitle);

        mRlOptionMain = (RelativeLayout)findViewById(R.id.layout_option_main);
        mIvLogo = (ImageView)findViewById(R.id.img_logo);

        mIvFacebook = (ImageView)findViewById(R.id.img_facebook);
        mIvTwitter = (ImageView)findViewById(R.id.img_twitter);
        mTvSignInWith = findViewById(R.id.txt_sign_in_with);
        mRlNewUserSignUp = (RelativeLayout)findViewById(R.id.layout_new_user_signup);
        mRlSignIn = (RelativeLayout)findViewById(R.id.layout_signin);
        mTvNewUser = findViewById(R.id.txt_new_user_signup);
        TvSignIn = findViewById(R.id.txt_signin);
        mIvBackground = (ImageView)findViewById(R.id.img_background);

    }

    private void initTypeFace() {
        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        regularRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

    }

    class CheckFacebookUserHttp extends AsyncTask<String, Integer, String>{

        HttpEntity entity;
        String SocialUrl;
        String facebook_id;
        String twitter_id;
        String FbEmail;
        String FbName;
        private String content =  null;

        public CheckFacebookUserHttp(String SocialUrl,String f_id, String twitter_id, String FbEmail, String FbName){
            this.SocialUrl = SocialUrl;
            facebook_id = f_id;
            this.twitter_id = twitter_id;
            this.FbEmail = FbEmail;
            this.FbName = FbName;

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            if(!facebook_id.equals(""))
                entityBuilder.addTextBody("facebook_id", facebook_id);
            else if(!twitter_id.equals(""))
                entityBuilder.addTextBody("twitter_id", twitter_id);
            entity = entityBuilder.build();
        }

        protected void onPreExecute() {
            ProgressDialog.show();
            cusRotateLoading.start();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpParams HttpParams = client.getParams();
                HttpConnectionParams.setConnectionTimeout(HttpParams, 60 * 60 * 1000);
                HttpConnectionParams.setSoTimeout(HttpParams, 60 * 60 * 1000);
                Log.d("SocialUrl","SocialUrl = "+SocialUrl+"=="+facebook_id+"=="+twitter_id);
                HttpPost post = new HttpPost(SocialUrl);
                post.setEntity(entity);
                client.execute(post, new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {

                        HttpEntity httpEntity = httpResponse.getEntity();
                        content = EntityUtils.toString(httpEntity);
                        Log.d("Result >>>","Result One"+ content);

                        return null;
                    }
                });

            } catch(Exception e)
            {
                e.printStackTrace();
                Log.d("Indiaries", "Result error" + e);
                return e.getMessage();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            cusRotateLoading.stop();
            ProgressDialog.cancel();

            boolean isStatus = Common.ShowHttpErrorMessage(MainActivity.this,result);
            if(isStatus) {
                try {
                    JSONObject resObj = new JSONObject(result);
                    Log.d("Social Register resObj", "Social Register resObj = " + resObj);
                    if (resObj.getString("status").equals("failed")) {

                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage(getResources().getString(R.string.facebook_popup_string))
                                .setPositiveButton(getResources().getString(R.string.register), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent ri = new Intent(MainActivity.this, SignUpActivity.class);
                                        ri.putExtra("facebook_id", facebook_id);
                                        ri.putExtra("twitter_id", twitter_id);
                                        ri.putExtra("facebook_email", FbEmail);
                                        ri.putExtra("facebook_name", FbName);
                                        startActivity(ri);
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();

                    } else if (resObj.getString("status").equals("success")) {
/*
                        JSONArray CabDetAry = new JSONArray(resObj.getString("cabDetails"));
                        Common.CabDetail = (List<CabDetail>) CabDetAry;*/

                        /*set Start Currency*/

                        JSONArray currencyArray = new JSONArray(resObj.getString("country_detail"));
                        for (int ci = 0; ci < currencyArray.length(); ci++) {
                            JSONObject startEndTimeObj = currencyArray.getJSONObject(ci);
                           /* Common.Currency = startEndTimeObj.getString("currency");
                            Common.Country = startEndTimeObj.getString("country");*/
                        }

                /*set Start And End Time*/
                        JSONArray startEndTimeArray = new JSONArray(resObj.getString("time_detail"));
                        for (int si = 0; si < startEndTimeArray.length(); si++) {
                            JSONObject startEndTimeObj = startEndTimeArray.getJSONObject(si);
                         /*   Common.StartDayTime = startEndTimeObj.getString("day_start_time");
                            Common.EndDayTime = startEndTimeObj.getString("day_end_time");*/
                        }

                /*passenger Detail*/
                        JSONObject userDetilObj = new JSONObject(resObj.getString("userdetail"));

                        SharedPreferences.Editor id = userPref.edit();
                        id.putString("id", userDetilObj.getString("id"));
                        id.commit();

                        SharedPreferences.Editor name = userPref.edit();
                        name.putString("name", userDetilObj.getString("name"));
                        name.commit();

                        SharedPreferences.Editor passwordPre = userPref.edit();
                        passwordPre.putString("password", "");
                        passwordPre.commit();

                        SharedPreferences.Editor username = userPref.edit();
                        username.putString("username", userDetilObj.getString("username"));
                        username.commit();

                        SharedPreferences.Editor mobile = userPref.edit();
                        mobile.putString("mobile", userDetilObj.getString("mobile"));
                        mobile.commit();

                        SharedPreferences.Editor email = userPref.edit();
                        email.putString("email", userDetilObj.getString("email"));
                        email.commit();

                        SharedPreferences.Editor isLogin = userPref.edit();
                        isLogin.putString("isLogin", "1");
                        isLogin.commit();

                        SharedPreferences.Editor userImage = userPref.edit();
                        userImage.putString("userImage", userDetilObj.getString("image"));
                        userImage.commit();

                        SharedPreferences.Editor dob = userPref.edit();
                        dob.putString("date_of_birth", userDetilObj.getString("dob"));
                        dob.commit();


                        SharedPreferences.Editor facebook_id = userPref.edit();
                        facebook_id.putString("facebook_id", userDetilObj.getString("facebook_id"));
                        facebook_id.commit();

                        SharedPreferences.Editor twitter_id = userPref.edit();
                        twitter_id.putString("twitter_id", userDetilObj.getString("twitter_id"));
                        twitter_id.commit();

                        SharedPreferences.Editor gender = userPref.edit();
                        gender.putString("gender", userDetilObj.getString("gender"));
                        gender.commit();

                        //Common.showMkSucess(MainActivity.this, resObj.getString("message").toString(), "no");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent hi = new Intent(MainActivity.this, HomeActivity.class);
                                hi.putExtra("pickupLatitude", PickupLatitude);
                                hi.putExtra("pickupLongtude", PickupLongtude);
                                startActivity(hi);
                                finish();
                            }
                        }, 500);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/*    public void CheckFacebookUser(String SocialUrl,final String facebook_id, final String twitter_id, final String FbEmail, final String FbName){

        ProgressDialog.show();
        cusRotateLoading.start();
        Log.d("Social Url ", "Social Url = " + SocialUrl + "?facebook_id=" + facebook_id);

        Ion.with(MainActivity.this)
            .load(SocialUrl)
            .setTimeout(10000)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception error, JsonObject result) {
                    // do stuff with the result or error

                    if (error == null) {

                        ProgressDialog.cancel();
                        cusRotateLoading.stop();
                        Log.d("Social Register resObj", "Social Register resObj = " + result.toString());
                        try {
                            JSONObject resObj = new JSONObject(result.toString());
                            Log.d("Social Register resObj", "Social Register resObj = " + resObj);
                            if (resObj.getString("status").equals("failed")) {

                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage(getResources().getString(R.string.facebook_popup_string))
                                        .setPositiveButton(getResources().getString(R.string.register), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent ri = new Intent(MainActivity.this, SignUpActivity.class);
                                                ri.putExtra("facebook_id", facebook_id);
                                                ri.putExtra("twitter_id", twitter_id);
                                                ri.putExtra("facebook_email", FbEmail);
                                                ri.putExtra("facebook_name", FbName);
                                                startActivity(ri);
                                            }
                                        })
                                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .show();

                            } else if (resObj.getString("status").equals("success")) {

                                Common.showMkSucess(MainActivity.this, resObj.getString("message"), "yes");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent hi = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(hi);
                                        finish();
                                    }
                                }, 2000);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ProgressDialog.cancel();
                        cusRotateLoading.stop();

                        Common.ShowHttpErrorMessage(MainActivity.this, error.getMessage());
                    }
                }
            });

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(socialFlg == 2) {
            Log.d("requestCode","requestCode = "+requestCode);
            twitterLoginBtn.onActivityResult(requestCode, resultCode, data);
            socialFlg = 0;
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
