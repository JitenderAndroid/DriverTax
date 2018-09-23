package com.taxi.passenger.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.taxi.passenger.DeleteCabMvp.Model.DeleteCabBean;
import com.taxi.passenger.DeleteCabMvp.Presenter.DeleteCabPresenterImp;
import com.taxi.passenger.DeleteCabMvp.View.DeleteCabView;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import com.taxi.passenger.Facebook.FBBean;
import com.taxi.passenger.Facebook.FB_Callback;
import com.taxi.passenger.Facebook.FacebookLoginClass;
import com.taxi.passenger.R;
import com.taxi.passenger.utils.AllTripFeed;
import com.taxi.passenger.utils.CircleTransform;
import com.taxi.passenger.utils.Common;
import com.taxi.passenger.utils.Url;

public class BookingDetailActivity extends AppCompatActivity implements FB_Callback,DeleteCabView
    {
    private TextView mTvBookingId, mTvCancelRequest, mTvBookingIdVal, mTvPickUpPoint, mTvPickupPointValue, mTvBookingDate, mTvDropPoint, mTvDropPointVal;
    private ImageView mIvCarImage;
    private TextView mTvDistance, mTvdistanceValue, mTvDistanceKm, mTvTotalPrice, mTvTotalPriceDol, mTvTotalPriceVal, mTvBookingDetail, mTvTrackTruck;
    private RelativeLayout mRlBackArrow;
    private RelativeLayout mRlTracktruck;
    private LinearLayout mRlCarDetail;
    private LinearLayout mRlDriverDetail;
    private TextView mTvTruckTypeVal, mTvTextCancel;
    private RelativeLayout mRlLayoutCancelRequestButton;
    private ImageView mIvDriverImage;
    private TextView mTvDriverImage, mTvDrvTrcTyp, mTvNumPlate, mTvMobileNumber, mTvLicNumber;
    private ScrollView scrollView;
    private LinearLayout mRlAccepted, mLlForAccepted;
    private RelativeLayout mRlAcceptedCall;
    private LinearLayout mRlCompleted, mRlCancelUser, mRlOnTrip, mRlDriverUnavailable, mRlCancelDriver;
    private RelativeLayout mRlPending, mRlFooterCancleLayout;
    private TextView mTvTravelTime;
    private TextView mTvTravelTimeVal, mTvTo, mTvVehicleDetail, mTvPaymentDetail;
    private RelativeLayout mRlAcceptedShareETA, mRlCompletedETA, mRlCompletedEtaChild, mRlShareOnTrip, mRlShareDriverUnavailabel, mRlShareCancelDriver;
    private Typeface openSansRegular, robotoRegular, robotoMedium, robotoBold, openSansSemibold;
    private AllTripFeed allTripFeed;
    private SharedPreferences userPref;
    private Dialog ProgressDialog;
    private RotateLoading cusRotateLoading;
    private CheckBox chkDriveLate, chkChangedMind, chkAnotherCab, chkDeniedDuty;
    private String DriverPhNo = "";
    private BroadcastReceiver receiver;
    private CallbackManager callbackManager;
    private Dialog ShareDialog;
    private String ShareDesc;
    private RelativeLayout layout_cancel_ride;
    boolean canClick = false;
        private DeleteCabPresenterImp deleteCabPresenterImp;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_booking_detail);

        callbackManager = CallbackManager.Factory.create();
        ProgressDialog = new Dialog(BookingDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);

        initViews();

        userPref = PreferenceManager.getDefaultSharedPreferences(BookingDetailActivity.this);

        initAssets();
        setTypeFace();


        Intent i = getIntent();
        allTripFeed = (AllTripFeed) i.getExtras().getSerializable("allTripFeed");
        deleteCabPresenterImp= new DeleteCabPresenterImp(this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pickup_date_time = "";
        Log.d("BookingDetailActivity", allTripFeed.getPickupDateTime());
        try {
            Date parceDate = simpleDateFormat.parse(allTripFeed.getPickupDateTime());
            SimpleDateFormat parceDateFormat = new SimpleDateFormat("h:mm a,dd,MMM yyyy");
            pickup_date_time = parceDateFormat.format(parceDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mTvBookingIdVal.setText(allTripFeed.getBookingId());
        mTvPickupPointValue.setText(allTripFeed.getPickupArea());
        mTvDropPointVal.setText(allTripFeed.getDropArea());
        mTvBookingDate.setText(pickup_date_time);
        mTvdistanceValue.setText(allTripFeed.getKm());
        mTvTotalPriceVal.setText(allTripFeed.getAmount());
        Log.d("TaxiType", "TaxiType = " + allTripFeed.getTaxiType());
        mTvTruckTypeVal.setText(allTripFeed.getTaxiType());
        mTvTravelTimeVal.setText(allTripFeed.getApproxTime());


        if (allTripFeed.getDriverDetail() != null && allTripFeed.getDriverDetail().equals("null")) {
            mRlCarDetail.setVisibility(View.VISIBLE);
            mRlDriverDetail.setVisibility(View.GONE);
            Log.d("allTripFeed", "allTripFeed = " + Url.CAR_IMAGE_URL + allTripFeed.getCarIcon());
            Picasso.with(BookingDetailActivity.this)
                    .load(Uri.parse(Url.CAR_IMAGE_URL + allTripFeed.getCarIcon()))
                    .placeholder(R.drawable.truck_icon)
                    .transform(new CircleTransform())
                    .into(mIvCarImage);
            mRlTracktruck.setEnabled(true);//false
        } else {
            mRlCarDetail.setVisibility(View.GONE);
            mRlDriverDetail.setVisibility(View.VISIBLE);
            mTvDrvTrcTyp.setText(allTripFeed.getTaxiType());
            if (allTripFeed.getStatus().equals("9"))
                mRlTracktruck.setEnabled(true);//false
            else
                mRlTracktruck.setEnabled(true);
            try {
                if(allTripFeed.getDriverDetail()!=null){
                JSONObject drvObj = new JSONObject(allTripFeed.getDriverDetail());
                mTvDriverImage.setText(drvObj.getString("name"));

                mTvNumPlate.setText(drvObj.getString("car_no"));
                DriverPhNo = drvObj.getString("phone");
                mTvMobileNumber.setText(DriverPhNo);
                mTvLicNumber.setText(drvObj.getString("license_plate"));

                Picasso.with(BookingDetailActivity.this)
                        .load(Uri.parse(Url.DriverImageUrl + drvObj.getString("image")))
                        .placeholder(R.drawable.avatar_placeholder)
                        .transform(new CircleTransform())
                        .into(mIvDriverImage);
            } }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("Status", "Status = " + allTripFeed.getStatus());

        if (allTripFeed.getStatus().equals("1")) {
            mRlPending.setVisibility(View.VISIBLE);
        } else if (allTripFeed.getStatus().equals("3")) {
            mRlAccepted.setVisibility(View.VISIBLE);
            // mRlFooterCancleLayout.setVisibility(View.VISIBLE);
            mRlFooterCancleLayout.setBackgroundColor(getResources().getColor(R.color.yellow_cancel));

        } else if (allTripFeed.getStatus().equals("9")) {
            mRlCompleted.setVisibility(View.VISIBLE);

        } else if (allTripFeed.getStatus().equals("4")) {
            mRlTracktruck.setVisibility(View.GONE);
            mTvTotalPriceVal.setText("0.00");
            mTvTextCancel.setText(" Trip Cancelled ");
            mRlPending.setClickable(false);

            mRlFooterCancleLayout.setVisibility(View.GONE);
            mRlLayoutCancelRequestButton.setClickable(false);
            mTvTextCancel.setClickable(false);
            mRlCancelUser.setVisibility(View.GONE);
        } else if (allTripFeed.getStatus().equals("8")) {
            mRlOnTrip.setVisibility(View.VISIBLE);
        } else if (allTripFeed.getStatus().equals("6")) {
            mRlDriverUnavailable.setVisibility(View.VISIBLE);
        } else if (allTripFeed.getStatus().equals("5")) {
            mRlPending.setVisibility(View.VISIBLE);
        }

        mRlLayoutCancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog CancelBookingDialog = new Dialog(BookingDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                CancelBookingDialog.setContentView(R.layout.cancel_booking_dialog);
                CancelBookingDialog.show();

                chkDriveLate = CancelBookingDialog.findViewById(R.id.chk_drive_late);
                chkDriveLate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBoxCheck("driver_late");
                        canClick=true;
                    }
                });


                RelativeLayout layout_driver_late = CancelBookingDialog.findViewById(R.id.layout_driver_late);
                layout_driver_late.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBoxCheck("driver_late");
                        canClick=true;
                    }
                });

                chkChangedMind = CancelBookingDialog.findViewById(R.id.chk_changed_mind);
                chkChangedMind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBoxCheck("changed_mind");
                        canClick=true;
                    }
                });

                RelativeLayout layout_change_mind = CancelBookingDialog.findViewById(R.id.layout_change_mind);
                layout_change_mind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBoxCheck("changed_mind");
                        canClick=true;
                    }
                });

                chkAnotherCab = CancelBookingDialog.findViewById(R.id.chk_another_cab);
                chkAnotherCab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBoxCheck("another_cab");
                    }
                });
                RelativeLayout layout_another_cab = CancelBookingDialog.findViewById(R.id.layout_another_cab);
                layout_another_cab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBoxCheck("another_cab");
                        canClick=true;
                    }
                });
                chkDeniedDuty = CancelBookingDialog.findViewById(R.id.chk_denied_duty);
                chkDeniedDuty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBoxCheck("denied_duty");
                        canClick=true;
                    }
                });
                RelativeLayout layout_denied_dute = CancelBookingDialog.findViewById(R.id.layout_denied_dute);
                layout_denied_dute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBoxCheck("denied_duty");
                        canClick=true;
                    }
                });

                RelativeLayout layout_dont_cancel = CancelBookingDialog.findViewById(R.id.layout_dont_cancel);
                layout_dont_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CancelBookingDialog.cancel();
                    }
                });

                layout_cancel_ride = CancelBookingDialog.findViewById(R.id.layout_cancel_ride);

                    layout_cancel_ride.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CancelBookingDialog.cancel();
                            if (canClick){
                               DeleteCab();
                            }
                            mRlLayoutCancelRequestButton.setClickable(false);
                        }
                    });

                }

        });


        mRlBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRlTracktruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent di = new Intent(BookingDetailActivity.this, TrackTruckActivity.class);
                di.putExtra("allTripFeed", allTripFeed);
                startActivity(di);

            }
        });

        /*Footer click event*/
        mRlAcceptedCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + DriverPhNo));
                        startActivity(callIntent);
                    }
                }, 100);
            }
        });

        /*Share Layout Start*/
        mRlAcceptedShareETA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog();
            }
        });

        mRlCompletedETA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog();
            }
        });
        mRlCompletedEtaChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog();
            }
        });

        mRlShareOnTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog();
            }
        });

        mRlShareDriverUnavailabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog();
            }
        });

        mRlShareCancelDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog();
            }
        });

        ShareDesc = "Name : " + userPref.getString("name", "") + ",";
        ShareDesc += "Pickup Address : " + allTripFeed.getPickupArea() + ",";
        ShareDesc += "Drop Address : " + allTripFeed.getDropArea() + ",";

    /*Share Layout End*/
    }

    private void setTypeFace() {
        mTvBookingDetail.setTypeface(openSansRegular);
        mTvTrackTruck.setTypeface(robotoBold);
        mTvTo.setTypeface(robotoBold);
        mTvVehicleDetail.setTypeface(robotoBold);
        mTvPaymentDetail.setTypeface(robotoBold);
        mTvCancelRequest.setTypeface(robotoBold);

        mTvBookingId.setTypeface(robotoRegular);
        mTvPickUpPoint.setTypeface(robotoRegular);
        mTvBookingDate.setTypeface(robotoRegular);
        mTvDropPoint.setTypeface(robotoRegular);
        mTvDistanceKm.setTypeface(robotoRegular);
        mTvTotalPriceDol.setTypeface(robotoRegular);

        mTvPickupPointValue.setTypeface(openSansRegular);
        mTvBookingDate.setTypeface(openSansRegular);
        mTvDropPointVal.setTypeface(openSansRegular);
        mTvDistance.setTypeface(openSansRegular);
        mTvdistanceValue.setTypeface(openSansRegular);
        mTvTotalPrice.setTypeface(openSansRegular);
        mTvTotalPriceVal.setTypeface(openSansRegular);
        mTvTruckTypeVal.setTypeface(openSansRegular);
        mTvTravelTime.setTypeface(openSansRegular);
        mTvTravelTimeVal.setTypeface(openSansRegular);

        mTvDriverImage.setTypeface(robotoRegular);
        mTvDrvTrcTyp.setTypeface(robotoRegular);
        mTvNumPlate.setTypeface(robotoRegular);
        mTvMobileNumber.setTypeface(robotoRegular);
        mTvLicNumber.setTypeface(robotoRegular);

    }

    private void initAssets() {
        openSansRegular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        robotoRegular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        robotoMedium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        robotoBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        openSansSemibold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold_0.ttf");

    }

    private void initViews() {
        mTvCancelRequest =  findViewById(R.id.txt_cancel_request);
        mTvBookingId =  findViewById(R.id.txt_booking_id);
        mTvBookingIdVal =  findViewById(R.id.txt_booking_id_val);
        mTvPickUpPoint =  findViewById(R.id.txt_pickup_point);
        mTvPickupPointValue =  findViewById(R.id.txt_pickup_point_val);
        mTvBookingDate =  findViewById(R.id.txt_booking_date);
        mTvDropPoint =  findViewById(R.id.txt_drop_point);
        mTvDropPointVal =  findViewById(R.id.txt_drop_point_val);
        mIvCarImage = (ImageView) findViewById(R.id.img_car_image);
        mTvDistance =  findViewById(R.id.txt_distance);
        mTvdistanceValue =  findViewById(R.id.txt_distance_val);
        mTvDistanceKm =  findViewById(R.id.txt_distance_km);
        mTvTotalPrice =  findViewById(R.id.txt_total_price);
        mTvTotalPriceDol =  findViewById(R.id.txt_total_price_dol);
        mTvTotalPriceVal =  findViewById(R.id.txt_total_price_val);
        mTvBookingDetail =  findViewById(R.id.txt_booking_detail);
        mTvTrackTruck =  findViewById(R.id.txt_track_truck);
        mRlBackArrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);
        mRlTracktruck = (RelativeLayout) findViewById(R.id.layout_track_truck);
        mRlCarDetail = (LinearLayout) findViewById(R.id.layout_car_detail);
        mRlDriverDetail = (LinearLayout) findViewById(R.id.layout_driver_detail);
        mTvTruckTypeVal =  findViewById(R.id.txt_truct_type_val);
        mTvTextCancel =  findViewById(R.id.txt_cancel_request);
        mRlLayoutCancelRequestButton = (RelativeLayout) findViewById(R.id.layout_cancel_request_button);
        mIvDriverImage = (ImageView) findViewById(R.id.img_driver_image);
        mTvDriverImage =  findViewById(R.id.txt_driver_name);
        mTvDrvTrcTyp =  findViewById(R.id.txt_drv_trc_typ);
        mTvNumPlate =  findViewById(R.id.txt_num_plate);
        mTvMobileNumber =  findViewById(R.id.txt_mobile_num);
        mTvLicNumber =  findViewById(R.id.txt_lic_num);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        mRlAccepted = (LinearLayout) findViewById(R.id.layout_accepted);
        mRlAcceptedCall = (RelativeLayout) findViewById(R.id.layout_accepted_call);
        mRlCompleted = (LinearLayout) findViewById(R.id.layout_completed);
        mRlCancelUser = (LinearLayout) findViewById(R.id.layout_cancel_user);
        mRlOnTrip = (LinearLayout) findViewById(R.id.layout_on_trip);
        mRlDriverUnavailable = (LinearLayout) findViewById(R.id.layout_driver_unavailabel);
        mRlCancelDriver = (LinearLayout) findViewById(R.id.layout_cancel_driver);
        mRlPending = (RelativeLayout) findViewById(R.id.layout_pending);
        mRlFooterCancleLayout = (RelativeLayout) findViewById(R.id.layout_footer);
        mTvTravelTime =  findViewById(R.id.txt_travel_time);
        mTvTravelTimeVal =  findViewById(R.id.txt_travel_time_val);
        mTvTo =  findViewById(R.id.txt_to);
        mTvVehicleDetail =  findViewById(R.id.txt_vehicle_detail);
        mTvPaymentDetail =  findViewById(R.id.txt_payment_detail);
        cusRotateLoading = ProgressDialog.findViewById(R.id.rotateloading_register);

        mRlAcceptedShareETA = (RelativeLayout) findViewById(R.id.layout_accepted_share_eta);
        mRlCompletedETA = (RelativeLayout) findViewById(R.id.layout_completed_eta);
        mRlCompletedEtaChild = (RelativeLayout) findViewById(R.id.layout_completed_eta_chield);
        mRlShareOnTrip = (RelativeLayout) findViewById(R.id.layout_share_on_trip);
        mRlShareDriverUnavailabel = (RelativeLayout) findViewById(R.id.layout_share_driver_unavailabel);
        mRlShareCancelDriver = (RelativeLayout) findViewById(R.id.layout_share_cancel_driver);


    }

    public void CheckBoxCheck(String CheckString) {

        if (CheckString.equals("driver_late")) {
            chkDriveLate.setChecked(true);
            canClick = true;
            layout_cancel_ride.setBackground(getResources().getDrawable(R.color.black));

        } else
            chkDriveLate.setChecked(false);
            canClick = false;

        Log.d("CheckString", "CheckString = " + CheckString);
        if (CheckString.equals("changed_mind")) {
            chkChangedMind.setChecked(true);
            canClick = true;
            layout_cancel_ride.setBackground(getResources().getDrawable(R.color.black));

        } else
            chkChangedMind.setChecked(false);
            canClick = false;

        if (CheckString.equals("another_cab")) {
            chkAnotherCab.setChecked(true);
            canClick = true;
            layout_cancel_ride.setBackground(getResources().getDrawable(R.color.black));

        } else
            chkAnotherCab.setChecked(false);
            canClick = false;


        if (CheckString.equals("denied_duty")) {
            chkDeniedDuty.setChecked(true);

            canClick = true;
            layout_cancel_ride.setBackground(getResources().getDrawable(R.color.black));


        } else
            chkDeniedDuty.setChecked(false);

            canClick = false;
    }

    public void ShareDialog() {
        ShareDialog = new Dialog(BookingDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ShareDialog.setContentView(R.layout.camera_dialog_layout);

        TextView facebook_text = ShareDialog.findViewById(R.id.txt_open_camera);
        facebook_text.setText("Facebook Share");

        TextView twitter_text = ShareDialog.findViewById(R.id.txt_open_gallery);
        twitter_text.setText("Twitter Share");

        RelativeLayout layout_open_camera = ShareDialog.findViewById(R.id.layout_open_camera);
        layout_open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog.cancel();
                ShareFacebookLink(ShareDesc);
            }
        });

        RelativeLayout layout_open_gallery = ShareDialog.findViewById(R.id.layout_open_gallery);
        layout_open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog.cancel();
                ShareTwitterLink();

            }
        });

        RelativeLayout layout_open_cancel = ShareDialog.findViewById(R.id.layout_open_cancel);
        layout_open_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog.cancel();
            }
        });

        ShareDialog.show();
    }

    public void ShareFacebookLink(final String Description) {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        if (loggedIn) {
            FacebookLoginClass shareLink = new FacebookLoginClass(BookingDetailActivity.this, callbackManager);
            shareLink.postStatusUpdate("TaxiPassenger", Description, Url.AppLogUrl, "");
        } else {

            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().logInWithPublishPermissions(BookingDetailActivity.this, Collections.singletonList("publish_actions"));

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
                                                FacebookLoginClass shareLink = new FacebookLoginClass(BookingDetailActivity.this, callbackManager);
                                                shareLink.postStatusUpdate("TaxiPassenger", Description, Url.AppLogUrl, "");

                                            } else {
                                                Toast.makeText(BookingDetailActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

    public void ShareTwitterLink() {
        String twitterUrl = Url.SocialShareUrl + "?uid=" + userPref.getString("id", "id");
        TweetComposer.Builder builder = null;


        builder = new TweetComposer.Builder(BookingDetailActivity.this)
                .text(ShareDesc)
                //.url(new URL(twitterUrl));

                .image(Uri.parse(Url.AppLogUrl));


        Intent intent = builder.createIntent();
        intent.setType("text/plain");
        startActivityForResult(intent, 111);
    }

    public void DeleteCab() {

        ProgressDialog.show();
        cusRotateLoading.start();
        int position = 0;
        deleteCabPresenterImp.setParameters(  allTripFeed.getBookingId() , userPref.getString("id", ""),position);
     /*   Ion.with(BookingDetailActivity.this)
                .load(Url.DELETE_CAB_URL + "?booking_id=" + allTripFeed.getBookingId() + "&uid=" + userPref.getString("id", ""))
                .setTimeout(10000)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception error, JsonObject result) {
                        // do stuff with the result or error
                        Log.d("Login result", "Login result = " + result + "==" + error);
                        if (error == null) {

                            ProgressDialog.cancel();
                            cusRotateLoading.stop();

                            try {
                                JSONObject resObj = new JSONObject(result.toString());
                                if (resObj.getString("status").equals("success")) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent homeInt = new Intent(BookingDetailActivity.this, HomeActivity.class);
                                            homeInt.putExtra("cancel_booking", "1");
                                            startActivity(homeInt);
                                            finish();
                                        }
                                    }, 1000);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ProgressDialog.cancel();
                            cusRotateLoading.stop();

                            Common.ShowHttpErrorMessage(BookingDetailActivity.this, error.getMessage());
                        }
                    }
                });*/

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        IntentFilter filter = new IntentFilter("com.taxi.passenger.BookingDetailActivity");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Common.is_pusnotification = 1;
            }
        };
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTvBookingId = null;
        mTvBookingIdVal = null;
        mTvPickUpPoint = null;
        mTvPickupPointValue = null;
        mTvBookingDate = null;
        mTvDropPoint = null;
        mTvDropPointVal = null;
        mIvCarImage = null;
        mTvDistance = null;
        mTvdistanceValue = null;
        mTvDistanceKm = null;
        mTvTotalPrice = null;
        mTvTotalPriceDol = null;
        mTvTotalPriceVal = null;
        mTvBookingDetail = null;
        mTvTrackTruck = null;
        mRlBackArrow = null;
        mRlTracktruck = null;

        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras().containsKey("extra_is_retweet")) {
                    boolean isReTweet = data.getExtras().getBoolean("extra_is_retweet");
                    if (isReTweet) {
                        Toast.makeText(BookingDetailActivity.this, "Duplicate Tweet. This tweet has been posted very recently", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(BookingDetailActivity.this, "Your post was shared successfully.", Toast.LENGTH_LONG).show();
                    }
                    System.out.println("Image ID>>>>" + data.getExtras().getString("image_id"));
                }

            }
        }
    }

    @Override
    public void onLoginSuccess(FBBean beanObject) {

    }

    @Override
    public void onLoginFailure(String message) {
        Toast.makeText(BookingDetailActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostSuccess(String postID, String message) {
        Toast.makeText(BookingDetailActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostFailure(String message) {
        Toast.makeText(BookingDetailActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLogout() {

    }


        @Override
        public void hideProgressDeleteTrip() {
            ProgressDialog.cancel();
            cusRotateLoading.stop();
        }

        @Override
        public void onResponce(DeleteCabBean deleteCabBean, int position) {
            if (deleteCabBean.getStatus().equals("success")) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent homeInt = new Intent(BookingDetailActivity.this, HomeActivity.class);
                        homeInt.putExtra("cancel_booking", "1");
                        startActivity(homeInt);
                        finish();
                    }
                }, 1000);
            }

        }
    }
