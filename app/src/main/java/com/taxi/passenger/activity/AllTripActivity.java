package com.taxi.passenger.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.taxi.passenger.AllTripFeedMvp.Model.AllTrips;
import com.taxi.passenger.AllTripFeedMvp.Presenter.AllTripPresenterImp;
import com.taxi.passenger.AllTripFeedMvp.View.AllTripView;
import com.taxi.passenger.DeleteCabMvp.Model.DeleteCabBean;
import com.taxi.passenger.DeleteCabMvp.Presenter.DeleteCabPresenterImp;
import com.taxi.passenger.DeleteCabMvp.View.DeleteCabView;
import com.taxi.passenger.FilterAllTripMvp.Model.FilterTripBean;
import com.taxi.passenger.FilterAllTripMvp.Presenter.FilterPresenterImp;
import com.taxi.passenger.FilterAllTripMvp.View.FilterTripView;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.taxi.passenger.R;
import com.taxi.passenger.adapter.AllTripAdapter;
import com.taxi.passenger.interfaces.NetworkInterfaces;
import com.taxi.passenger.model.response.AccepetedRideDriverDetail;
import com.taxi.passenger.slidingMenu.SlidingMenu;
import com.taxi.passenger.utils.AllTripFeed;
import com.taxi.passenger.utils.Common;
import com.taxi.passenger.utils.Url;

public class AllTripActivity extends AppCompatActivity implements AllTripAdapter.OnAllTripClickListener,NetworkInterfaces.AcceptedRideDriverDetailedInterfaces,AllTripView,DeleteCabView,FilterTripView {


    private RelativeLayout mRlSlideMenu;
    private TextView mTvAllTrip;
    private RelativeLayout mRlFilter;
    private RecyclerView mRecycleAllTrip;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout layoutBackground;
    private RelativeLayout mRlNoRecordFound;
    private LinearLayout mRlRecyclerView;

    private CheckBox mChAll;
    private CheckBox mChPending;
    private CheckBox mChCompleted;
    private CheckBox mChReject;
    private CheckBox mChUserReject;
    private CheckBox mChDriverAccept;
    private RelativeLayout mRlCompletedbookingCheck, mRlDriverAcceptCheck, mRlUserRejectCheck, mRlDriverRejectcheck;
    private boolean checkAllClick = false;
    private ImageView mIvCloseIcon;
    private RelativeLayout mRlCancel, mRlApply;
    private AllTripAdapter allTripAdapter;
    private DeleteCabPresenterImp deleteCabPresenterImp;
    private SharedPreferences mUserPref,userPref;

    private ArrayList<AllTripFeed> allTripArray;
    private RecyclerView.LayoutManager AllTripLayoutManager;

    private Typeface openSansBold, openSansRegular, robotoBold;

    private Dialog filterDialog;
    private String FilterString = "";

    private SlidingMenu slidingMenu;

    private Dialog progressDialog;
    private RotateLoading cusRotateLoading;
    String  msg;
    private CheckBox mChDriverLate;
    private CheckBox mChChangedMind;
    private CheckBox mChAnotherCab;
    private CheckBox mChDeniedDuty;
    private AllTripFeed selTripFeeds;
    private TextView mTvAll, mTvdriverAccept, mUserReject, mTvDriverUna, mTvCompletedBooking, mtvPendingBooking;
    private RelativeLayout mRlAllCheck, mRlPendingBookCheck;
    private Common common = new Common();
    private BroadcastReceiver receiver;
     private AllTripPresenterImp allTripPresenterImp;
    private FilterPresenterImp filterPresenterImp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trip);
        progressDialog = new Dialog(AllTripActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        /*Filter Dialog Start*/
        filterPresenterImp = new FilterPresenterImp(this);
        allTripPresenterImp = new AllTripPresenterImp(this);
        deleteCabPresenterImp= new DeleteCabPresenterImp(this);

        filterDialog = new Dialog(AllTripActivity.this, R.style.DialogSlideAnim);
        filterDialog.setContentView(R.layout.all_trip_filter_dialog);
        initviews();
        initAssets();
        setFonts();
        mTvAllTrip.setTypeface(openSansBold);

        userPref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


          mUserPref = PreferenceManager.getDefaultSharedPreferences(AllTripActivity.this);
          AllTripLayoutManager = new LinearLayoutManager(this);
          mRecycleAllTrip.setLayoutManager(AllTripLayoutManager);
         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Common.isNetworkAvailable(AllTripActivity.this)) {
                    progressDialog.show();
                    cusRotateLoading.start();
                    Log.d("check Value", "check value = " + mUserPref.getInt("pending booking", 5) + "==" + mUserPref.getInt("user reject", 5) + "==" + mUserPref.getInt("driver unavailable", 5) + "==" + mUserPref.getInt("complete booking", 5));
                    if (mUserPref.getBoolean("setFilter", false) == true) {
                        if (mUserPref.getInt("pending booking", 0) == 1) {
                            FilterString += 1 + ",";
                        }
                        if (mUserPref.getInt("complete booking", 0) == 9) {
                            FilterString += 9 + ",";
                        }
                        if (mUserPref.getInt("driver unavailable", 0) == 6) {
                            FilterString += 6 + ",";
                        }
                        if (mUserPref.getInt("user reject", 0) == 4) {
                            FilterString += 4 + ",";
                        }
                        if (mUserPref.getInt("driver accept", 0) == 3) {
                            FilterString += 3 + ",";
                        }

                        FilterString = FilterString.substring(0, (FilterString.length() - 1));
                        Log.d("FilterString", "FilterString = " + FilterString);

                        if (mUserPref.getInt("pending booking", 0) == 1 && mUserPref.getInt("complete booking", 0) == 9 && mUserPref.getInt("driver unavailable", 0) == 6 && mUserPref.getInt("user reject", 0) == 4 && mUserPref.getInt("driver accept", 0) == 3) {
                            SharedPreferences.Editor checkAll = mUserPref.edit();
                            checkAll.putString("check all", "check all");
                            checkAll.commit();
                            FilterString = "";
                        } else {
                            SharedPreferences.Editor checkAll = mUserPref.edit();
                            checkAll.putString("check all", "");
                            checkAll.commit();
                        }
                        FilterAllTrips(0, "filter");
                        FilterString = "";
                    } else {
                        getAllTrip(0);
                    }

                } else {
                    Common.showInternetInfo(AllTripActivity.this, "Network is not available");
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        }, 1000);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecycleAllTrip.setEnabled(false);
                if (Common.isNetworkAvailable(AllTripActivity.this)) {
                    if (mUserPref.getBoolean("setFilter", false) == true) {
                        if (mUserPref.getInt("pending booking", 6) == 1) {
                            FilterString += 1 + ",";
                        }
                        if (mUserPref.getInt("complete booking", 0) == 9) {
                            FilterString += 9 + ",";
                        }
                        if (mUserPref.getInt("driver unavailable", 0) == 6) {
                            FilterString += 6 + ",";
                        }
                        if (mUserPref.getInt("user reject", 0) == 4) {
                            FilterString += 4 + ",";
                        }
                        if (mUserPref.getInt("user reject", 0) == 4) {
                            FilterString += 4 + ",";
                        }
                        if (mUserPref.getInt("driver accept", 0) == 3) {
                            FilterString += 3 + ",";
                        }

                        FilterString = FilterString.substring(0, (FilterString.length() - 1));
                        Log.d("FilterString", "FilterString = " + FilterString);

                        if (mUserPref.getInt("pending booking", 0) == 1 && mUserPref.getInt("complete booking", 0) == 9 && mUserPref.getInt("driver unavailable", 0) == 6 && mUserPref.getInt("user reject", 0) == 4 && mUserPref.getInt("driver accept", 0) == 3) {
                            FilterString = "";
                        }

                        FilterAllTrips(0, "");
                        FilterString = "";
                    } else {
                        getAllTrip(0);
                    }
                } else {
                    //Network is not available
                    mRecycleAllTrip.setEnabled(true);
                    Common.showInternetInfo(AllTripActivity.this, "Network is not available");
                }
            }
        });


        mRlFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutBackground.setVisibility(View.VISIBLE);
                filterDialog.show();
            }
        });

        CheckBoxChecked(mRlAllCheck, mChAll, "all");
        CheckBoxChecked(mRlPendingBookCheck, mChPending, "pending book");
        CheckBoxChecked(mRlCompletedbookingCheck, mChCompleted, "completed book");
        CheckBoxChecked(mRlDriverRejectcheck, mChReject, "driver reject");
        CheckBoxChecked(mRlUserRejectCheck, mChUserReject, "user reject");
        CheckBoxChecked(mRlDriverAcceptCheck, mChDriverAccept, "drive accept");

        Log.d("check Value", "check value = " + mUserPref.getInt("pending booking", 5) + "==" + mUserPref.getInt("user reject", 5) + "==" + mUserPref.getInt("driver unavailable", 5) + "==" + mUserPref.getInt("complete booking", 5));
        if (mUserPref.getInt("user reject", 0) == 4)
            mChUserReject.setChecked(true);
        if (mUserPref.getInt("driver unavailable", 0) == 6)
            mChReject.setChecked(true);
        if (mUserPref.getInt("complete booking", 0) == 9)
            mChCompleted.setChecked(true);
        if (mUserPref.getInt("pending booking", 0) == 1)
            mChPending.setChecked(true);
        if (mUserPref.getInt("driver accept", 0) == 3) {
            mChDriverAccept.setChecked(true);
        }

        if (mUserPref.getInt("user reject", 0) == 4 && mUserPref.getInt("driver unavailable", 0) == 6 && mUserPref.getInt("complete booking", 0) == 9 && mUserPref.getInt("pending booking", 0) == 1 && mUserPref.getInt("driver accept", 0) == 3) {
            mChAll.setChecked(true);
        }
        mIvCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutBackground.setVisibility(View.GONE);
                filterDialog.cancel();
            }
        });

//        TextView txt_cancel_popup = filterDialog.findViewById(R.id.txt_cancel_popup);
//        txt_cancel_popup.setTypeface(RobotoBold);

        mRlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutBackground.setVisibility(View.GONE);
                filterDialog.cancel();
            }
        });

        mRlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                cusRotateLoading.start();

                layoutBackground.setVisibility(View.GONE);
                filterDialog.cancel();
                boolean setFilter = false;
                SharedPreferences.Editor pending_booking = mUserPref.edit();
                if (mChPending.isChecked()) {
                    FilterString = 1 + ",";
                    pending_booking.putInt("pending booking", 1);
                    setFilter = true;
                } else
                    pending_booking.putInt("pending booking", 0);
                pending_booking.apply();
                SharedPreferences.Editor complete_booking = mUserPref.edit();
                if (mChCompleted.isChecked()) {
                    FilterString += 9 + ",";
                    complete_booking.putInt("complete booking", 9);
                    setFilter = true;
                } else
                    complete_booking.putInt("complete booking", 0);
                complete_booking.apply();

                SharedPreferences.Editor user_reject = mUserPref.edit();
                if (mChUserReject.isChecked()) {
                    FilterString += 4 + ",";
                    user_reject.putInt("user reject", 4);
                    setFilter = true;
                } else
                    user_reject.putInt("user reject", 0);
                user_reject.apply();

                SharedPreferences.Editor driver_accept = mUserPref.edit();
                if (mChDriverAccept.isChecked()) {
                    FilterString += 3 + ",";
                    driver_accept.putInt("driver accept", 3);
                    setFilter = true;
                } else
                    driver_accept.putInt("driver accept", 0);
                driver_accept.apply();

                SharedPreferences.Editor driver_unavailable = mUserPref.edit();
                if (mChReject.isChecked()) {
                    FilterString += 6 + ",";
                    driver_unavailable.putInt("driver unavailable", 6);
                    setFilter = true;
                } else
                    driver_unavailable.putInt("driver unavailable", 0);
                driver_unavailable.apply();

                if (FilterString.length() > 0)
                    FilterString = FilterString.substring(0, (FilterString.length() - 1));

                SharedPreferences.Editor clickfilter = mUserPref.edit();
                clickfilter.putBoolean("setFilter", setFilter);
                clickfilter.apply();

                if (mUserPref.getInt("pending booking", 0) == 1 && mUserPref.getInt("complete booking", 0) == 9 && mUserPref.getInt("driver unavailable", 0) == 6 && mUserPref.getInt("user reject", 0) == 4 && mUserPref.getInt("driver accept", 0) == 3) {
                    FilterString = "";
                }

                FilterAllTrips(0, "filter");
                FilterString = "";

            }
        });

/**
 * Slide Menu Start
 */
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffsetRes(R.dimen.slide_menu_width);
        slidingMenu.setFadeDegree(0.20f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.left_menu);

        common.SlideMenuDesign(slidingMenu, AllTripActivity.this, "all trip");

        mRlSlideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });
    }

    private void setFonts() {
        mTvAll.setTypeface(openSansRegular);
        mtvPendingBooking.setTypeface(openSansRegular);
        mTvCompletedBooking.setTypeface(openSansRegular);
        mTvDriverUna.setTypeface(openSansRegular);
        mUserReject.setTypeface(openSansRegular);
        mTvdriverAccept.setTypeface(openSansRegular);
    }

    private void initAssets() {
        openSansBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        openSansRegular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        robotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
    }

    private void initviews() {
        mRlSlideMenu = (RelativeLayout) findViewById(R.id.layout_slidemenu);
        mTvAllTrip =  findViewById(R.id.txt_all_trip);
        mRlFilter = (RelativeLayout) findViewById(R.id.layout_filter);
        mRecycleAllTrip = (RecyclerView) findViewById(R.id.recycle_all_trip);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        layoutBackground = (RelativeLayout) findViewById(R.id.layout_background);
        mRlNoRecordFound = (RelativeLayout) findViewById(R.id.layout_no_recourd_found);
        mRlRecyclerView = (LinearLayout) findViewById(R.id.layout_recycleview);
        cusRotateLoading = progressDialog.findViewById(R.id.rotateloading_register);
        mTvAll = filterDialog.findViewById(R.id.txt_all);
        mtvPendingBooking = filterDialog.findViewById(R.id.txt_pending_booking);
        mTvCompletedBooking = filterDialog.findViewById(R.id.txt_com_booking);
        mTvDriverUna = filterDialog.findViewById(R.id.txt_drv_una);
        mUserReject = filterDialog.findViewById(R.id.txt_usr_rej);
        mTvdriverAccept = filterDialog.findViewById(R.id.txt_drv_accept);
        mChAll = filterDialog.findViewById(R.id.chk_all);
        mRlAllCheck = filterDialog.findViewById(R.id.layout_all_check);
        mRlPendingBookCheck = filterDialog.findViewById(R.id.layour_pen_book_check);
        mChPending = filterDialog.findViewById(R.id.chk_pen_book);
        mChCompleted = filterDialog.findViewById(R.id.chk_com_book);
        mRlCompletedbookingCheck = filterDialog.findViewById(R.id.layout_com_book_check);
        mRlDriverRejectcheck = filterDialog.findViewById(R.id.layout_drv_reject_check);
        mChUserReject = filterDialog.findViewById(R.id.chk_user_reject);
        mRlUserRejectCheck = filterDialog.findViewById(R.id.layout_user_reject_check);
        mChDriverAccept = filterDialog.findViewById(R.id.chk_drv_accept);
        mRlDriverAcceptCheck = filterDialog.findViewById(R.id.layout_drv_accept_check);
        mChReject = filterDialog.findViewById(R.id.chk_drv_reject);
        mRlCancel = filterDialog.findViewById(R.id.layout_calcel);
        mRlApply = filterDialog.findViewById(R.id.layout_apply);
        mIvCloseIcon = filterDialog.findViewById(R.id.img_close_icon);
    }

    public void CheckBoxChecked(RelativeLayout relativeLayout, final CheckBox checkBox, final String checkBoxValue) {

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("checkAllClick", "checkAllClick = " + checkAllClick + "==" + checkAllClick);
                if (checkBoxValue.equals("all")) {
                    if (checkAllClick) {
                        mChAll.setChecked(false);
                        mChPending.setChecked(false);
                        mChCompleted.setChecked(false);
                        mChReject.setChecked(false);
                        mChUserReject.setChecked(false);
                        mChDriverAccept.setChecked(false);
                        checkAllClick = false;
                    } else {
                        mChAll.setChecked(true);
                        mChPending.setChecked(true);
                        mChCompleted.setChecked(true);
                        mChReject.setChecked(true);
                        mChUserReject.setChecked(true);
                        mChDriverAccept.setChecked(true);
                        checkAllClick = true;
                    }
                } else {
                    if (mChPending.isChecked() && mChCompleted.isChecked() && mChReject.isChecked() && mChUserReject.isChecked() && mChDriverAccept.isChecked()) {
                        mChAll.setChecked(true);
                        checkAllClick = true;
                    } else {
                        mChAll.setChecked(false);
                        checkAllClick = false;
                    }
                }
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked())
                    checkBox.setChecked(false);
                else
                    checkBox.setChecked(true);
                Log.d("checkAllClick", "checkAllClick = " + checkAllClick + "==" + checkAllClick);
                if (checkBoxValue.equals("all")) {
                    if (checkAllClick) {
                        mChAll.setChecked(false);
                        mChPending.setChecked(false);
                        mChCompleted.setChecked(false);
                        mChReject.setChecked(false);
                        mChUserReject.setChecked(false);
                        mChDriverAccept.setChecked(false);
                        checkAllClick = false;
                    } else {
                        mChAll.setChecked(true);
                        mChPending.setChecked(true);
                        mChCompleted.setChecked(true);
                        mChReject.setChecked(true);
                        mChUserReject.setChecked(true);
                        mChDriverAccept.setChecked(true);
                        checkAllClick = true;
                    }
                } else {
                    if (mChPending.isChecked() && mChCompleted.isChecked() && mChReject.isChecked() && mChUserReject.isChecked() && mChDriverAccept.isChecked()) {
                        mChAll.setChecked(true);
                        checkAllClick = true;
                    } else {
                        mChAll.setChecked(false);
                        checkAllClick = false;
                    }

                }

            }
        });
    }

    public void getAllTrip(final int offset) {

        if (offset == 0) {
            allTripArray = new ArrayList<>();
        }

        Log.d("AllTrip", "getAllTrip: "+ mUserPref.getString("id", ""));
        Log.d("AllTrip", "getAllTrip: "+ String.valueOf(offset));
        Log.d("LOAD_TRIPS_URL", "LOAD_TRIPS_URL =" + Url.LOAD_TRIPS_URL + "==" + mUserPref.getString("id", "") + "==" + String.valueOf(offset));



        allTripPresenterImp.setParameters(mUserPref.getString("id", ""), String.valueOf(offset),offset);


    /*   Ion.with(AllTripActivity.this)
                .load(Url.LOAD_TRIPS_URL)
                .setTimeout(6000)
                //.setJsonObjectBody(json)
                .setMultipartParameter("user_id", mUserPref.getString("id", ""))
                .setMultipartParameter("off", String.valueOf(offset))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception error, JsonObject result) {
                        // do stuff with the result or error
                        Log.d("load_trips result", "load_trips result = " + result + "==" + error);
                        if (error == null) {

                            progressDialog.cancel();
                            cusRotateLoading.stop();
                            try {
                                JSONObject resObj = new JSONObject(result.toString());
                                Log.d("LOAD_TRIPS_URL", "LOAD_TRIPS_URL two= " + resObj);
                                swipeRefreshLayout.setRefreshing(false);
                                if (resObj.getString("status").equals("success")) {
                                    mRecycleAllTrip.setEnabled(true);
                                    JSONArray tripArray = new JSONArray(resObj.getString("all_trip"));
                                    for (int t = 0; t < tripArray.length(); t++) {
                                        JSONObject trpObj = tripArray.getJSONObject(t);
                                        AllTripFeed allTripFeed = new AllTripFeed();
                                        allTripFeed.setBookingId(trpObj.getString("id"));
                                        allTripFeed.setDropArea(trpObj.getString("drop_area"));
                                        allTripFeed.setPickupArea(trpObj.getString("pickup_area"));
                                        allTripFeed.setTaxiType(trpObj.getString("car_type"));
                                        allTripFeed.setPickupDateTime(trpObj.getString("pickup_date_time"));
                                        allTripFeed.setAmount(trpObj.getString("amount"));
                                        allTripFeed.setCarIcon(trpObj.getString("icon"));
                                        allTripFeed.setKm(trpObj.getString("km"));
                                        allTripFeed.setDriverDetail(trpObj.getString("driver_detail"));
                                        allTripFeed.setStatus(trpObj.getString("status"));
                                        allTripFeed.setApproxTime(trpObj.getString("approx_time"));
                                        allTripFeed.setOldLocationList(null);
                                        allTripFeed.setStartPickLatLng(trpObj.getString("pickup_lat"));
                                        allTripFeed.setEndPickLatLng(trpObj.getString("pickup_longs"));
                                        allTripFeed.setStartDropLatLng(trpObj.getString("drop_lat"));
                                        allTripFeed.setEndDropLatLng(trpObj.getString("drop_longs"));

                                        allTripArray.add(allTripFeed);
                                    }
                                    Log.d("LOAD_TRIPS_URL", "LOAD_TRIPS_URL three= " + allTripArray.size());
                                    if (allTripArray != null && allTripArray.size() > 0) {
                                        if (offset == 0) {
                                            mRlRecyclerView.setVisibility(View.VISIBLE);
                                            mRlNoRecordFound.setVisibility(View.GONE);
                                            allTripAdapter = new AllTripAdapter(AllTripActivity.this, allTripArray);
                                            mRecycleAllTrip.setAdapter(allTripAdapter);
                                            allTripAdapter.setOnAllTripItemClickListener(AllTripActivity.this);


                                            progressDialog.cancel();
                                            cusRotateLoading.stop();
                                        }
                                        allTripAdapter.updateItems();
                                        swipeRefreshLayout.setEnabled(true);
                                    }
                                } else if (resObj.getString("status").equals("false")) {
                                    //   Common.user_InActive = 1;
                                    Common.InActive_msg = resObj.getString("message");

                                    SharedPreferences.Editor editor = mUserPref.edit();
                                    editor.clear();
                                    editor.commit();

                                    Intent logInt = new Intent(AllTripActivity.this, MainActivity.class);
                                    logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    logInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(logInt);

                                } else {
                                    if (offset == 0) {
                                        progressDialog.cancel();
                                        cusRotateLoading.stop();
                                        mRlRecyclerView.setVisibility(View.GONE);
                                        mRlNoRecordFound.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(AllTripActivity.this, resObj.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            progressDialog.cancel();
                            cusRotateLoading.stop();

                            Common.ShowHttpErrorMessage(AllTripActivity.this, error.getMessage());
                        }
                    }
                });*/

    }

    public void FilterAllTrips(final int offset, final String filter) {

        if (offset == 0)
            allTripArray = new ArrayList<>();

        Log.d("LOAD_TRIPS_URL", "LOAD_TRIPS_URL FilterString= " + Url.LOAD_TRIP_FILTER_URL + "==" + FilterString + "==" + mUserPref.getString("id", "") + "==" + String.valueOf(offset));
        Log.d("AllTrip",mUserPref.getString("id", ""));
        Log.d("AllTrip",String.valueOf(offset));
        Log.d("AllTrip",String.valueOf(offset));
   filterPresenterImp.setParameters(mUserPref.getString("id", ""),String.valueOf(offset),FilterString,offset);
      /*  Ion.with(AllTripActivity.this)
                .load(Url.LOAD_TRIP_FILTER_URL)
                .setTimeout(6000)
                //.setJsonObjectBody(json)


                .setMultipartParameter("user_id", mUserPref.getString("id", ""))
                .setMultipartParameter("off", String.valueOf(offset))
                .setMultipartParameter("filter", FilterString)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception error, JsonObject result) {
                        progressDialog.cancel();
                        cusRotateLoading.stop();
                        if (error == null) {
                            try {
                                JSONObject resObj = new JSONObject(result.toString());
                                Log.d("LOAD_TRIPS_URL", "LOAD_TRIPS_URL filter two= " + resObj.getString("status") + "==" + resObj);
                                if (resObj.getString("status").equals("success")) {

                                    mRecycleAllTrip.setEnabled(true);

                                    JSONArray tripArray = new JSONArray(resObj.getString("all_trip"));
                                    for (int t = 0; t < tripArray.length(); t++) {
                                        JSONObject trpObj = tripArray.getJSONObject(t);
                                        AllTripFeed allTripFeed = new AllTripFeed();
                                        allTripFeed.setBookingId(trpObj.getString("id"));
                                        allTripFeed.setDropArea(trpObj.getString("drop_area"));
                                        allTripFeed.setPickupArea(trpObj.getString("pickup_area"));
                                        allTripFeed.setTaxiType(trpObj.getString("car_type"));
                                        allTripFeed.setPickupDateTime(trpObj.getString("book_create_date_time"));
                                        allTripFeed.setAmount(trpObj.getString("amount"));
                                        allTripFeed.setCarIcon(trpObj.getString("icon"));
                                        allTripFeed.setKm(trpObj.getString("km"));
                                        allTripFeed.setDriverDetail(trpObj.getString("driver_detail"));
                                        allTripFeed.setStatus(trpObj.getString("status"));
                                        allTripFeed.setApproxTime(trpObj.getString("approx_time"));
                                        allTripFeed.setOldLocationList(null);
                                        allTripFeed.setStartPickLatLng(trpObj.getString("pickup_lat"));
                                        allTripFeed.setEndPickLatLng(trpObj.getString("pickup_longs"));
                                        allTripFeed.setStartDropLatLng(trpObj.getString("drop_lat"));
                                        allTripFeed.setEndDropLatLng(trpObj.getString("drop_longs"));
                                        allTripArray.add(allTripFeed);
                                    }
                                    Log.d("LOAD_TRIPS_URL", "LOAD_TRIPS_URL three= " + allTripArray.size());
                                    if (allTripArray != null && allTripArray.size() > 0) {
                                        mRlRecyclerView.setVisibility(View.VISIBLE);
                                        mRlNoRecordFound.setVisibility(View.GONE);
                                        if (offset == 0) {
                                            allTripAdapter = new AllTripAdapter(AllTripActivity.this, allTripArray);
                                            mRecycleAllTrip.setAdapter(allTripAdapter);
                                            allTripAdapter.setOnAllTripItemClickListener(AllTripActivity.this);
                                            swipeRefreshLayout.setRefreshing(false);

                                        }
                                        allTripAdapter.updateItemsFilter(allTripArray);
                                        if (swipeRefreshLayout != null)
                                            swipeRefreshLayout.setEnabled(true);
                                    }
                                } else if (resObj.getString("status").equals("false")) {
                                 *//*   Common.user_InActive = 1;
                                    Common.InActive_msg = resObj.getString("message");*//*

                                    SharedPreferences.Editor editor = mUserPref.edit();
                                    editor.clear();
                                    editor.commit();

                                    Intent logInt = new Intent(AllTripActivity.this, MainActivity.class);
                                    logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    logInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(logInt);
                                } else if (resObj.getString("status").equals("failed")) {
                                    Log.d("allTripArray", "allTripArray = " + allTripArray.size());
                                    if (swipeRefreshLayout != null)
                                        swipeRefreshLayout.setEnabled(false);
                                    if (allTripAdapter != null)
                                        allTripAdapter.updateItemsFilter(allTripArray);

                                    if (offset == 0) {
                                        progressDialog.cancel();
                                        cusRotateLoading.stop();
                                        mRlRecyclerView.setVisibility(View.GONE);
                                        mRlNoRecordFound.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(AllTripActivity.this, resObj.getString("message"), Toast.LENGTH_LONG).show();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Common.ShowHttpErrorMessage(AllTripActivity.this, error.getMessage());
                        }
                    }

                });*/

    }

    @Override
    public void scrollToLoad(int position) {

        if (mUserPref.getBoolean("setFilter", true) == true && !mUserPref.getString("check all", "").equals("check all")) {
            if (mUserPref.getInt("pending booking", 0) == 1) {
                FilterString += 1 + ",";
            }
            if (mUserPref.getInt("complete booking", 0) == 9) {
                FilterString += 9 + ",";
            }
            if (mUserPref.getInt("driver unavailable", 0) == 6) {
                FilterString += 6 + ",";
            }
            if (mUserPref.getInt("user reject", 0) == 4) {
                FilterString += 4 + ",";
            }
            if (mUserPref.getInt("driver accept", 0) == 3) {
                FilterString += 3 + ",";
            }

            if (FilterString.length() > 0)
                FilterString = FilterString.substring(0, (FilterString.length() - 1));

            FilterAllTrips(position + 1, "");
            FilterString = "";
        } else {
            getAllTrip(position + 1);
        }

    }

    @Override
    public void clickDetailTrip(int position) {

        if (allTripArray.size() > 0) {
            Intent di = new Intent(AllTripActivity.this, BookingDetailActivity.class);
            di.putExtra("allTripFeed", allTripArray.get(position));
            startActivity(di);
        }
    }

    @Override
    public void tripCancel(final int position) {

        final Dialog CancelBookingDialog = new Dialog(AllTripActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        CancelBookingDialog.setContentView(R.layout.cancel_booking_dialog);
        CancelBookingDialog.show();

        mChDriverLate = CancelBookingDialog.findViewById(R.id.chk_drive_late);
        mChDriverLate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBoxCheck("driver_late");
            }
        });


        RelativeLayout layout_driver_late = CancelBookingDialog.findViewById(R.id.layout_driver_late);
        layout_driver_late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBoxCheck("driver_late");
            }
        });

        mChChangedMind = CancelBookingDialog.findViewById(R.id.chk_changed_mind);
        mChChangedMind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBoxCheck("changed_mind");
            }
        });

        RelativeLayout layout_change_mind = CancelBookingDialog.findViewById(R.id.layout_change_mind);
        layout_change_mind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBoxCheck("changed_mind");
            }
        });

        mChAnotherCab = CancelBookingDialog.findViewById(R.id.chk_another_cab);
        mChAnotherCab.setOnClickListener(new View.OnClickListener() {
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
            }
        });
        mChDeniedDuty = CancelBookingDialog.findViewById(R.id.chk_denied_duty);
        mChDeniedDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBoxCheck("denied_duty");
            }
        });
        RelativeLayout layout_denied_dute = CancelBookingDialog.findViewById(R.id.layout_denied_dute);
        layout_denied_dute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBoxCheck("denied_duty");
            }
        });

        RelativeLayout layout_dont_cancel = CancelBookingDialog.findViewById(R.id.layout_dont_cancel);
        layout_dont_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelBookingDialog.cancel();
            }
        });

        RelativeLayout layout_cancel_ride = CancelBookingDialog.findViewById(R.id.layout_cancel_ride);
        layout_cancel_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CancelBookingDialog.cancel();

                progressDialog.show();
                cusRotateLoading.start();

                selTripFeeds = allTripArray.get(position);

                deleteCabPresenterImp.setParameters( selTripFeeds.getBookingId(), mUserPref.getString("id", ""),position);

               Log.d("DELETE_CAB_URL", "DELETE_CAB_URL = " + Url.DELETE_CAB_URL + "?" + selTripFeeds.getBookingId() + "==" + mUserPref.getString("id", ""));
              /*  Ion.with(AllTripActivity.this)
                        .load(Url.DELETE_CAB_URL + "?booking_id=" + selTripFeeds.getBookingId() + "&uid=" + mUserPref.getString("id", ""))
                        .setTimeout(6000)
                        //.setJsonObjectBody(json)
//                        .setMultipartParameter("booking_id", selTripFeeds.getBookingId())
//                        .setMultipartParameter("uid", mUserPref.getString("id", ""))
                        .asJsonObject()
                         .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception error, JsonObject result) {
                                progressDialog.cancel();
                                cusRotateLoading.stop();
                                if (error == null) {
                                    try {
                                        JSONObject resObj = new JSONObject(result.toString());
                                        if (resObj.getString("status").equals("success")) {
                                            selTripFeeds.setStatus("4");
                                            allTripAdapter.notifyItemChanged(position);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent homeInt = new Intent(AllTripActivity.this, HomeActivity.class);
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
                                    progressDialog.cancel();
                                    cusRotateLoading.stop();
                                    Common.ShowHttpErrorMessage(AllTripActivity.this, error.getMessage());
                                }
                            }

                        });*/

            }
        });

    }

    public void CheckBoxCheck(String CheckString) {

        if (CheckString.equals("driver_late"))
            mChDriverLate.setChecked(true);
        else
            mChDriverLate.setChecked(false);

        Log.d("CheckString", "CheckString = " + CheckString);
        if (CheckString.equals("changed_mind"))
            mChChangedMind.setChecked(true);
        else
            mChChangedMind.setChecked(false);

        if (CheckString.equals("another_cab"))
            mChAnotherCab.setChecked(true);
        else
            mChAnotherCab.setChecked(false);

        if (CheckString.equals("denied_duty"))
            mChDeniedDuty.setChecked(true);
        else
            mChDeniedDuty.setChecked(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRlSlideMenu = null;
        mTvAllTrip = null;
        mRlFilter = null;
        mRecycleAllTrip = null;
        swipeRefreshLayout = null;
        layoutBackground = null;
        mRlNoRecordFound = null;
        mRlRecyclerView = null;
        mChAll = null;
        mChPending = null;
        mChCompleted = null;
        mChReject = null;
        mChUserReject = null;
        allTripAdapter = null;

        unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter("com.taxi.passenger.AllTripActivity");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (Common.isNetworkAvailable(AllTripActivity.this)) {

                    progressDialog.show();
                    cusRotateLoading.start();

                    if (mUserPref.getBoolean("setFilter", false) == true) {
                        if (mUserPref.getInt("pending booking", 6) == 1) {
                            FilterString += 1 + ",";
                        }
                        if (mUserPref.getInt("complete booking", 0) == 9) {
                            FilterString += 9 + ",";
                        }
                        if (mUserPref.getInt("driver unavailable", 0) == 6) {
                            FilterString += 6 + ",";
                        }
                        if (mUserPref.getInt("user reject", 0) == 4) {
                            FilterString += 4 + ",";
                        }
                        if (mUserPref.getInt("user reject", 0) == 4) {
                            FilterString += 4 + ",";
                        }
                        if (mUserPref.getInt("driver accept", 0) == 3) {
                            FilterString += 3 + ",";
                        }

                        FilterString = FilterString.substring(0, (FilterString.length() - 1));
                        Log.d("FilterString", "FilterString = " + FilterString);

                        if (mUserPref.getInt("pending booking", 0) == 1 && mUserPref.getInt("complete booking", 0) == 9 && mUserPref.getInt("driver unavailable", 0) == 6 && mUserPref.getInt("user reject", 0) == 4 && mUserPref.getInt("driver accept", 0) == 3) {
                            FilterString = "";
                        }

                        FilterAllTrips(0, "");
                        FilterString = "";
                    } else {
                        getAllTrip(0);
                    }
                } else {
                    //Network is not available
                    mRecycleAllTrip.setEnabled(true);
                    Common.showInternetInfo(AllTripActivity.this, "Network is not available");
                }
            }
        };
        registerReceiver(receiver, filter);


    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent( AllTripActivity.this,HomeActivity.class);
        startActivity(in);
        finish();

    }

    @Override
    public void onSuccess(AccepetedRideDriverDetail accepetedRideDriverDetail) {
        //booking_id =accepetedRideDriverDetail.getBookingId();
        Log.d("MyFireBaseMessagingSer ",""+accepetedRideDriverDetail.getBookingId());
        Log.d("MyFireBaseMessagingSer ",""+accepetedRideDriverDetail.getKey());
        Log.d("MyFireBaseMessagingSer ",""+accepetedRideDriverDetail.getKey());
        Log.d("MyFireBaseMessagingSer ",""+accepetedRideDriverDetail.getStatus());
    }

    @Override
    public void onFailure() {

    }










    @Override
    public void hideProgressAllTrip() {
        progressDialog.cancel();
        cusRotateLoading.stop();

    }

    @Override
    public void hideProgressDeleteTrip() {
        progressDialog.cancel();
        cusRotateLoading.stop();


    }

    @Override
    public void onResponce(DeleteCabBean deleteCabBean ,int position) {


        if (deleteCabBean.getStatus().equals("success")) {
            selTripFeeds.setStatus("4");
            allTripAdapter.notifyItemChanged(position);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeInt = new Intent(AllTripActivity.this, HomeActivity.class);
                    homeInt.putExtra("cancel_booking", "1");
                    startActivity(homeInt);
                    finish();
                }
            }, 1000);
        }



    }

    @Override
    public void onResponce(AllTrips mAllTripFeed,int offset) {

        swipeRefreshLayout.setRefreshing(false);
        if (mAllTripFeed.getStatus().equals("success")) {
            mRecycleAllTrip.setEnabled(true);

            for (int t = 0; t < mAllTripFeed.getAllTrip().size(); t++) {

                AllTripFeed allTripFeed = new AllTripFeed();
                allTripFeed.setBookingId(mAllTripFeed.getAllTrip().get(t).getId());
                allTripFeed.setDropArea(mAllTripFeed.getAllTrip().get(t).getDropArea());
                allTripFeed.setPickupArea(mAllTripFeed.getAllTrip().get(t).getPickupArea());
                allTripFeed.setTaxiType(mAllTripFeed.getAllTrip().get(t).getTaxiType());
                allTripFeed.setPickupDateTime(mAllTripFeed.getAllTrip().get(t).getPickupDateTime());
                allTripFeed.setAmount(mAllTripFeed.getAllTrip().get(t).getAmount());
                allTripFeed.setCarIcon(mAllTripFeed.getAllTrip().get(t).getIcon());
                allTripFeed.setKm(mAllTripFeed.getAllTrip().get(t).getKm());
                if(mAllTripFeed.getAllTrip().get(t).getDriverDetail()!=null){
                allTripFeed.setDriverDetail(mAllTripFeed.getAllTrip().get(t).getDriverDetail().getName());
                }
                allTripFeed.setStatus(mAllTripFeed.getAllTrip().get(t).getStatus());
                allTripFeed.setApproxTime(mAllTripFeed.getAllTrip().get(t).getApproxTime());
                allTripFeed.setOldLocationList(null);
                allTripFeed.setStartPickLatLng(mAllTripFeed.getAllTrip().get(t).getPickupLat());
                allTripFeed.setEndPickLatLng(mAllTripFeed.getAllTrip().get(t).getPickupLongs());
                allTripFeed.setStartDropLatLng(mAllTripFeed.getAllTrip().get(t).getDropLat());
                allTripFeed.setEndDropLatLng(mAllTripFeed.getAllTrip().get(t).getDropArea());

                allTripArray.add(allTripFeed);
            }
            Log.d("LOAD_TRIPS_URL", "LOAD_TRIPS_URL three= " + allTripArray.size());
            if (allTripArray != null && allTripArray.size() > 0) {
                if (offset == 0) {
                    mRlRecyclerView.setVisibility(View.VISIBLE);
                    mRlNoRecordFound.setVisibility(View.GONE);
                    allTripAdapter = new AllTripAdapter(AllTripActivity.this, allTripArray);
                    mRecycleAllTrip.setAdapter(allTripAdapter);
                    allTripAdapter.setOnAllTripItemClickListener(AllTripActivity.this);


                    progressDialog.cancel();
                    cusRotateLoading.stop();
                }
                allTripAdapter.updateItems();
                swipeRefreshLayout.setEnabled(true);
            }
        }
        else if (mAllTripFeed.getStatus().equals("false")) {
                                 /*   Common.user_InActive = 1;
                                    Common.InActive_msg = resObj.getString("message");*/

            SharedPreferences.Editor editor = mUserPref.edit();
            editor.clear();
            editor.commit();

            Intent logInt = new Intent(AllTripActivity.this, MainActivity.class);
            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            logInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logInt);
        }

    }

    @Override
    public void showProgressFilterTrip() {

    }

    @Override
    public void hideProgressFilterTrip() {
        progressDialog.cancel();
        cusRotateLoading.stop();
    }

    @Override
    public void onResponce(FilterTripBean mFilterTripBean, int offset) {
        if (mFilterTripBean.getStatus().equals("success")) {

            mRecycleAllTrip.setEnabled(true);


            for (int t = 0; t < mFilterTripBean.getAllTrip().size(); t++) {

                AllTripFeed allTripFeed = new AllTripFeed();
                allTripFeed.setBookingId(mFilterTripBean.getAllTrip().get(t).getId());
                allTripFeed.setDropArea(mFilterTripBean.getAllTrip().get(t).getDropArea());
                allTripFeed.setPickupArea(mFilterTripBean.getAllTrip().get(t).getPickupArea());
                allTripFeed.setTaxiType(mFilterTripBean.getAllTrip().get(t).getTaxiType());
                allTripFeed.setPickupDateTime(mFilterTripBean.getAllTrip().get(t).getBookCreateDateTime());
                allTripFeed.setAmount(mFilterTripBean.getAllTrip().get(t).getAmount());
                allTripFeed.setCarIcon(mFilterTripBean.getAllTrip().get(t).getIcon());
                allTripFeed.setKm(mFilterTripBean.getAllTrip().get(t).getKm());
                if(mFilterTripBean.getAllTrip().get(t).getDriverDetail()!=null){
                    allTripFeed.setDriverDetail(mFilterTripBean.getAllTrip().get(t).getDriverDetail().getName());
                }
                allTripFeed.setStatus(mFilterTripBean.getAllTrip().get(t).getStatus());
                allTripFeed.setApproxTime(mFilterTripBean.getAllTrip().get(t).getApproxTime());
                allTripFeed.setOldLocationList(null);
                allTripFeed.setStartPickLatLng(mFilterTripBean.getAllTrip().get(t).getPickupLat());
                allTripFeed.setEndPickLatLng(mFilterTripBean.getAllTrip().get(t).getPickupLongs());
                allTripFeed.setStartDropLatLng(mFilterTripBean.getAllTrip().get(t).getDropLat());
                allTripFeed.setEndDropLatLng(mFilterTripBean.getAllTrip().get(t).getDropLongs());
                allTripArray.add(allTripFeed);
            }
            Log.d("LOAD_TRIPS_URL", "LOAD_TRIPS_URL three= " + allTripArray.size());
            if (allTripArray != null && allTripArray.size() > 0) {
                mRlRecyclerView.setVisibility(View.VISIBLE);
                mRlNoRecordFound.setVisibility(View.GONE);
                if (offset == 0) {
                    allTripAdapter = new AllTripAdapter(AllTripActivity.this, allTripArray);
                    mRecycleAllTrip.setAdapter(allTripAdapter);
                    allTripAdapter.setOnAllTripItemClickListener(AllTripActivity.this);
                    swipeRefreshLayout.setRefreshing(false);

                }
                allTripAdapter.updateItemsFilter(allTripArray);
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setEnabled(true);
            }
        }
        else if (mFilterTripBean.getStatus().equals("false")) {
                                 /*   Common.user_InActive = 1;
                                    Common.InActive_msg = resObj.getString("message");*/

            SharedPreferences.Editor editor = mUserPref.edit();
            editor.clear();
            editor.commit();

            Intent logInt = new Intent(AllTripActivity.this, MainActivity.class);
            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            logInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logInt);
        }
        else if (mFilterTripBean.getStatus().equals("failed")) {
            Log.d("allTripArray", "allTripArray = " + allTripArray.size());
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setEnabled(false);
            if (allTripAdapter != null)
                allTripAdapter.updateItemsFilter(allTripArray);

            if (offset == 0) {
                progressDialog.cancel();
                cusRotateLoading.stop();
                mRlRecyclerView.setVisibility(View.GONE);
                mRlNoRecordFound.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(AllTripActivity.this,mFilterTripBean.getStatus(), Toast.LENGTH_LONG).show();
            }

        }

    }


}
