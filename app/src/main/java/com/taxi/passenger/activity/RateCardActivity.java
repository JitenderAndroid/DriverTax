package com.taxi.passenger.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.taxi.passenger.R;
import com.taxi.passenger.adapter.CarTypeAdapter;
import com.taxi.passenger.model.response.CabDetail;
import com.taxi.passenger.slidingMenu.SlidingMenu;
import com.taxi.passenger.utils.CircleTransform;
import com.taxi.passenger.utils.Common;
import com.taxi.passenger.utils.Url;

public class RateCardActivity extends AppCompatActivity implements CarTypeAdapter.OnCarTypeClickListener, View.OnClickListener {

    private RelativeLayout mRlSlideMenu;
    private RelativeLayout mRlCategory;
    private ImageView mIvTruckIcon;
    private String cabDetail;

    private JSONArray countryArray;
    private TextView mTvRateCard, mTvCategory, mTvTruckTypeVal, mTvLoadingCapacity, mTvLoadingCapacityValue, mTvstandardRateDay, mTvFixKm, mtvFixDayCurrency, mTvFixDayPrice, mTvAfterKm, mtvafterDayCurrency, mTvAftDayPrice, mTvAfterDayPerKm, mtvSatndardRateNight, mTvNightFixKm, mTvFixNightCurrency, mTvFixNightPrice, mTvAfterNightKm, mTvAfternightCurrency, mTvAfterNightPrice, mTvAfterNightPerKm, mtvExtraCharges, mTvRideTimeChrDay, mTvRideTimeDayCurrency, mTvRideTimeDayPrice, mTvRideTimeDayPerKm, mTvWaitTimeDay, mTvRideTimeChrNight, mTvRideTimeNightCurrency, mTvRideTimeNightPrice, mTvRideTimeChrPerKm, mTvWaitTimeNight, mTvPerTimeCharges, mTvPerTimeChargesDes, mTvServiceTex, mTvServiceTexDes, mTvTollTex;

    private SlidingMenu slidingMenu;
    private Typeface openSansBold, openSansRegular, robotRegular, robotoMedium;
    private Dialog CarTypeDialog;
    private SharedPreferences userPref;
    private RecyclerView recycle_car_type;
    private RecyclerView.LayoutManager CarTypeLayoutManager;
    private CarTypeAdapter carTypeAdapter;
    private ArrayList<CabDetail> cabDetailArray;
    private JSONObject countryCurrencyObj;

    private Common common = new Common();
    private String countryDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_card);
        userPref = PreferenceManager.getDefaultSharedPreferences(this);

        cabDetail = userPref.getString("cabDetail", "No name defined");
        countryDetail = userPref.getString("countryDetail", "");

        cabDetailArray = new ArrayList<CabDetail>();


        try {
            countryArray = new JSONArray(countryDetail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int si = 0; si < countryArray.length(); si++) {
            try {
                countryCurrencyObj = countryArray.getJSONObject(si);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        try {
            JSONArray CabDetailAry = new JSONArray(cabDetail);
            for (int ci = 0; ci < CabDetailAry.length(); ci++) {

                CabDetail cabDetails = new CabDetail();

                try {
                    JSONObject cabObj = CabDetailAry.getJSONObject((ci));
                    cabDetails.setId(cabObj.getString("cab_id"));
                    cabDetails.setCartype(cabObj.getString("cartype"));
                    cabDetails.setTransfertype(cabObj.getString("transfertype"));
                    cabDetails.setIntialkm(cabObj.getString("intialkm"));
                    cabDetails.setCarRate(cabObj.getString("car_rate"));
                    cabDetails.setFromintialkm(cabObj.getString("fromintialkm"));
                    cabDetails.setStandardrate(cabObj.getString("standardrate"));
                    cabDetails.setFromintailrate(cabObj.getString("fromintailrate"));
                    cabDetails.setExtrahour(cabObj.getString("extrahour"));
                    cabDetails.setExtrakm(cabObj.getString("extrakm"));
                    cabDetails.setFromstandardrate(cabObj.getString("fromstandardrate"));
                    cabDetails.setNightFromintialkm(cabObj.getString("night_fromintialkm"));
                    cabDetails.setNightFromintailrate(cabObj.getString("night_fromintailrate"));
                    cabDetails.setIcon(cabObj.getString("icon"));
                    cabDetails.setDescription(cabObj.getString("description"));
                    cabDetails.setNightIntailrate(cabObj.getString("night_intailrate"));
                    cabDetails.setNightStandardrate(cabObj.getString("night_standardrate"));
                    cabDetails.setRideTimeRate(cabObj.getString("ride_time_rate"));
                    cabDetails.setNightRideTimeRate(cabObj.getString("night_ride_time_rate"));
                    cabDetails.setSeatCapacity(cabObj.getString("seat_capacity"));
                    if (cabObj.has("fix_price")) {
                        cabDetails.setFixPrice(cabObj.getString("fix_price"));
                    } else {
                        cabDetails.setFixPrice("");
                    }
                    if (cabObj.has("area_id")) {
                        cabDetails.setAreaId(cabObj.getString("area_id"));
                    } else {
                        cabDetails.setFixPrice("");
                    }

                    if (ci == 0)
                        cabDetails.setIsSelected(true);
                    else
                        cabDetails.setIsSelected(false);

                    cabDetailArray.add(cabDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        initAssets();
        initViews();
        setTypeFace();

        /*Cab Detail*/

        CabDetailView(0);

        mRlCategory.setOnClickListener(this);

        /*Slide Menu Start*/
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffsetRes(R.dimen.slide_menu_width);
        slidingMenu.setFadeDegree(0.20f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.left_menu);

        common.SlideMenuDesign(slidingMenu, RateCardActivity.this, "rate card");

        mRlSlideMenu.setOnClickListener(this);
    }

    private void setTypeFace() {
        mTvCategory.setTypeface(robotoMedium);
        mTvTruckTypeVal.setTypeface(robotoMedium);
        mTvLoadingCapacity.setTypeface(robotoMedium);
        mTvLoadingCapacityValue.setTypeface(robotoMedium);
        mTvFixKm.setTypeface(robotoMedium);
        mtvFixDayCurrency.setTypeface(robotoMedium);
        mTvFixDayPrice.setTypeface(robotoMedium);
        mTvAfterKm.setTypeface(robotoMedium);
        mtvafterDayCurrency.setTypeface(robotoMedium);
        mTvAftDayPrice.setTypeface(robotoMedium);
        mTvAfterDayPerKm.setTypeface(robotoMedium);
        mTvNightFixKm.setTypeface(robotoMedium);
        mTvFixNightCurrency.setTypeface(robotoMedium);
        mTvFixNightPrice.setTypeface(robotoMedium);
        mTvAfterNightKm.setTypeface(robotoMedium);
        mTvAfternightCurrency.setTypeface(robotoMedium);
        mTvAfterNightPrice.setTypeface(robotoMedium);
        mTvAfterNightPerKm.setTypeface(robotoMedium);
        mTvRideTimeChrDay.setTypeface(robotoMedium);
        mTvRideTimeDayCurrency.setTypeface(robotoMedium);
        mTvRideTimeDayPrice.setTypeface(robotoMedium);
        mTvRideTimeDayPerKm.setTypeface(robotoMedium);
        mTvWaitTimeDay.setTypeface(robotoMedium);
        mTvRideTimeChrNight.setTypeface(robotoMedium);
        mTvRideTimeNightCurrency.setTypeface(robotoMedium);
        mTvRideTimeNightPrice.setTypeface(robotoMedium);
        mTvRideTimeChrPerKm.setTypeface(robotoMedium);
        mTvPerTimeCharges.setTypeface(robotoMedium);
        mTvServiceTex.setTypeface(robotoMedium);

        mTvRateCard.setTypeface(openSansBold);
        mTvstandardRateDay.setTypeface(robotRegular);
        mtvSatndardRateNight.setTypeface(robotRegular);
        mtvExtraCharges.setTypeface(robotRegular);
        mTvTollTex.setTypeface(robotRegular);
        mTvServiceTexDes.setTypeface(robotRegular);
        mTvPerTimeChargesDes.setTypeface(robotRegular);
        mTvWaitTimeNight.setTypeface(robotRegular);
        mTvWaitTimeDay.setTypeface(robotRegular);


    }

    private void initViews() {
        mRlSlideMenu = findViewById(R.id.layout_slidemenu);
        mTvRateCard = findViewById(R.id.txt_rate_card);
        mTvCategory = findViewById(R.id.txt_cateogry);
        mRlCategory = findViewById(R.id.layout_category);

        mTvTruckTypeVal = findViewById(R.id.txt_truck_typ_val);
        mTvLoadingCapacity = findViewById(R.id.txt_loading_capacity);
        mTvLoadingCapacityValue = findViewById(R.id.txt_loading_capacity_value);
        mTvstandardRateDay = findViewById(R.id.txt_standars_rate_day);
        mTvFixKm = findViewById(R.id.txt_day_fir_km);
        mtvFixDayCurrency = findViewById(R.id.txt_fir_day_currency);
        mTvFixDayPrice = findViewById(R.id.txt_fir_day_price);
        mTvAfterKm = findViewById(R.id.txt_after_km);
        mtvafterDayCurrency = findViewById(R.id.txt_aft_day_currency);
        mTvAftDayPrice = findViewById(R.id.txt_aft_day_price);
        mTvAfterDayPerKm = findViewById(R.id.txt_aft_day_per_km);
        mtvSatndardRateNight = findViewById(R.id.txt_standars_rate_night);
        mTvNightFixKm = findViewById(R.id.txt_night_fir_km);
        mTvFixNightCurrency = findViewById(R.id.txt_fir_night_currency);
        mTvFixNightPrice = findViewById(R.id.txt_fir_nigth_price);
        mTvAfterNightKm = findViewById(R.id.txt_after_night_km);
        mTvAfternightCurrency = findViewById(R.id.txt_aft_night_currency);
        mTvAfterNightPrice = findViewById(R.id.txt_aft_night_price);
        mTvAfterNightPerKm = findViewById(R.id.txt_aft_night_per_km);
        mtvExtraCharges = findViewById(R.id.txt_extra_charges);
        mTvRideTimeChrDay = findViewById(R.id.txt_ride_time_chr_day);
        mTvRideTimeDayCurrency = findViewById(R.id.txt_ride_time_day_currency);
        mTvRideTimeDayPrice = findViewById(R.id.txt_ride_time_day_price);
        mTvRideTimeDayPerKm = findViewById(R.id.txt_ride_time_day_per_km);
        mTvWaitTimeDay = findViewById(R.id.txt_wait_time_day);
        mTvRideTimeChrNight = findViewById(R.id.txt_ride_time_chr_night);
        mTvRideTimeNightCurrency = findViewById(R.id.txt_ride_time_night_currency);
        mTvRideTimeNightPrice = findViewById(R.id.txt_ride_time_night_price);
        mTvRideTimeChrPerKm = findViewById(R.id.txt_ride_time_night_per_km);
        mTvWaitTimeNight = findViewById(R.id.txt_wait_time_night);
        mTvPerTimeCharges = findViewById(R.id.txt_per_time_charges);
        mTvPerTimeChargesDes = findViewById(R.id.txt_per_time_charges_des);
        mTvServiceTex = findViewById(R.id.txt_service_tex);
        mTvServiceTexDes = findViewById(R.id.txt_service_tex_des);
        mTvTollTex = findViewById(R.id.txt_toll_tex);
        mIvTruckIcon = findViewById(R.id.img_truck_icon);

    }

    private void initAssets() {
        openSansBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        openSansRegular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        robotRegular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        robotoMedium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");

    }


    public void CabDetailView(int position) {


        try {
            JSONArray CabDetailAry = new JSONArray(cabDetail);
            for (int ci = 0; ci < CabDetailAry.length(); ci++) {

                try {

                    JSONObject cabDetailObj = CabDetailAry.getJSONObject((position));


                    Picasso.with(RateCardActivity.this)
                            .load(Uri.parse(Url.CAR_IMAGE_URL + cabDetailObj.getString("icon").toString()))
                            .placeholder(R.drawable.truck_slide_icon)
                            .transform(new CircleTransform())
                            .into(mIvTruckIcon);
                    Log.d("Truck Image", "" + cabDetailObj.getString("icon").toString());
                    mTvTruckTypeVal.setText(cabDetailObj.getString("cartype").toString());
                    mTvLoadingCapacityValue.setText(cabDetailObj.getString("seat_capacity").toString());

            /*Day*/
                    mtvFixDayCurrency.setText(countryCurrencyObj.getString("currency"));
                    mTvFixKm.setText(getResources().getString(R.string.first) + " " + cabDetailObj.getString("intialkm").toString() + " " + getResources().getString(R.string.km));
                    mTvFixDayPrice.setText(cabDetailObj.getString("car_rate").toString());
                    mTvAfterKm.setText(getResources().getString(R.string.after) + " " + cabDetailObj.getString("intialkm").toString() + " " + getResources().getString(R.string.km));
                    mtvafterDayCurrency.setText(Common.Currency);
                    mTvAftDayPrice.setText(cabDetailObj.getString("fromintailrate").toString());

            /*Night*/
                    mTvNightFixKm.setText(getResources().getString(R.string.first) + " " + cabDetailObj.getString("intialkm").toString() + " " + getResources().getString(R.string.km));
                    mTvFixNightCurrency.setText(countryCurrencyObj.getString("currency"));
                    mTvFixNightPrice.setText(cabDetailObj.getString("night_intailrate").toString());
                    mTvAfterNightKm.setText(getResources().getString(R.string.after) + " " + cabDetailObj.getString("intialkm").toString() + " " + getResources().getString(R.string.km));
                    mTvAfternightCurrency.setText(countryCurrencyObj.getString("currency"));
                    mTvAfterNightPrice.setText(cabDetailObj.getString("night_fromintailrate").toString());

            /*Extra Charge*/
                    mTvRideTimeDayCurrency.setText(countryCurrencyObj.getString("currency"));
                    mTvRideTimeDayPrice.setText(cabDetailObj.getString("ride_time_rate").toString());
                    mTvRideTimeNightCurrency.setText(countryCurrencyObj.getString("currency"));
                    mTvRideTimeNightPrice.setText(cabDetailObj.getString("night_ride_time_rate").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SelectCarType(int position) {

        CabDetailView(position);
        CarTypeDialog.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

        common.SlideMenuDesign(slidingMenu, RateCardActivity.this, "rate card");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.layout_category) {

                /*Car Type Dialog Start*/
            CarTypeDialog = new Dialog(RateCardActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            CarTypeDialog.setContentView(R.layout.cartype_dialog);
            recycle_car_type = CarTypeDialog.findViewById(R.id.recycle_car_type);

            CarTypeLayoutManager = new LinearLayoutManager(RateCardActivity.this);
            recycle_car_type.setLayoutManager(CarTypeLayoutManager);

            carTypeAdapter = new CarTypeAdapter(RateCardActivity.this, cabDetailArray);
            carTypeAdapter.updateItems();
            carTypeAdapter.setOnCarTypeItemClickListener(RateCardActivity.this);
            recycle_car_type.setAdapter(carTypeAdapter);

            CarTypeDialog.show();
                /*Car Type Dialog End*/
        } else if (v.getId() == R.id.layout_slidemenu) {
            slidingMenu.toggle();

        }
        {

        }

    }


    @Override
    public void onBackPressed() {
        Intent in = new Intent(RateCardActivity.this, HomeActivity.class);
        startActivity(in);
        finish();
    }
}
