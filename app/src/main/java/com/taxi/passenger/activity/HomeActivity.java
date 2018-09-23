package com.taxi.passenger.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.taxi.passenger.CalculateDistanceMvp.Model.CalculateDistancebean;
import com.taxi.passenger.CalculateDistanceMvp.Presenter.CalculationPresenterimp;
import com.taxi.passenger.CalculateDistanceMvp.View.CalculationView;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.taxi.passenger.R;
import com.taxi.passenger.TripsMvp.TripDetailActivity;
import com.taxi.passenger.adapter.AddressSuggestionAdapter;
import com.taxi.passenger.adapter.CabDetailAdapter;
import com.taxi.passenger.adapter.PickupDropLocationAdapter;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.apiclient.modelForGoogleApiMap.AddressFromGeocode;
import com.taxi.passenger.gpsLocation.GPSTracker;
import com.taxi.passenger.gpsLocation.LocationAddress;
import com.taxi.passenger.interfaces.NetworkInterfaces;
import com.taxi.passenger.model.response.AutoCompleteAddress;
import com.taxi.passenger.model.response.CabDetail;
import com.taxi.passenger.model.response.UserDetail;
import com.taxi.passenger.slidingMenu.SlidingMenu;
import com.taxi.passenger.utils.Common;
import com.taxi.passenger.utils.Constants;
import com.taxi.passenger.utils.Url;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback, CabDetailAdapter.OnCabDetailClickListener, PickupDropLocationAdapter.OnDraoppickupClickListener, NetworkInterfaces.GoogleApiMapResponce, NetworkInterfaces.googleAutocompleteAddress, AddressSuggestionAdapter.ItemClickListener, CalculationView {

    private TextView mTvHome, mTvReservation, mTvNow;
    private RelativeLayout mRlSlideMenu;
    private EditText mEtPickUpLocation;
    private EditText metDropLocation;
    private EditText mEtWriteComment;
    private RelativeLayout mRlLayoutNow;
    private RelativeLayout mRlLayoutReservation;
    private ImageView mIvPickUpClose;
    private ImageView mIvdropClose;
    private RecyclerView mRecyclePickupLocation;
    private RelativeLayout mRlPickupDragLocation;
    private LinearLayout mLayoutNoResults;
    private TextView mTvNotFound;
    private TextView mTvNoLocation;
    private TextView mTvPleaseCheck;
    private List<String> list;
    private SharedPreferences mUserPref;
    private SharedPreferences userPref;
    private Typeface openSansRegular, openSansBold, robotoRegular, robotoMedium, robotoBold;
    private SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
    private GPSTracker gpsTracker;
    private GoogleMap googleMap;
    private ArrayList<CabDetail> cabDetailArray;
    CalculationPresenterimp calculationPresenterimp;


    private UserDetail userDetailArray;
    private MarkerOptions marker;
    private LatLng mPickupLarLng;
    private LatLng mDropLarLng;
    private double dropLongitude;
    private double dropLatitude;
    private double pickupLongtude;
    private double pickupLatitude;

    ArrayList<HashMap<String, String>> locationArrayList;
    private ArrayList<LatLng> arrayPoints = null;

    private Dialog nowDialog;
    private Dialog cashDialog;
    private Dialog reservationDialog;
    private CabDetailAdapter cabDetailAdapter;

    private TextView mTvCarHeader;
    private TextView mTvCurrency, mTvFarBreakUp, mTvBook, mTvCancel;
    private TextView mTvCardescription;
    private TextView mTvFirstPrice;
    private TextView mTvFirstKm;
    private TextView mTvSecondPrice;
    private TextView mTvSecKm;
    private TextView mTvThdPrice, mTvLoaction;
    private RelativeLayout mRlOne;
    private RelativeLayout mRlTwo;
    private RelativeLayout mRlThree;
    private TextView mTvTotalPrice;
    private TextView mTvCash;
    private RelativeLayout mRlCash;
    private Spinner mspinnerPerson;
    private String person = "";
    private TextView mTvFirstCurrency;
    private TextView mTvSecondCurrency;
    private TextView mTvThdCurrency;
    private TextView mTvSpclCharNote;
    private LinearLayout mLayoutTimming;
    private RelativeLayout mrlFarBrkUp;

    private String carRate;
    private String fromIntailRate;
    private String rideTimeRate = "0";
    private String dayNight="day";
    private String transferType;
    private SlidingMenu slidingMenu;
    private ArrayList<Calendar> arrActualDates = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("E MMM, dd", Locale.getDefault());

    private Float distance = 0f;
    private int googleDuration = 0;
    private String truckIcon;
    private String truckType;
    private String cabId;
    private String areaId;
    private Float totlePrice;
    private float FirstKm;
    private int totalTime;
    private String carName = "";
    private String astTime = "";
    private String bothLocationString = "";
    private PickupDropLocationAdapter pickupDropLocationAdapter;
    private LinearLayoutManager pickupDragLayoutManager;
    private boolean ClickOkButton = false;
    private String PaymentType = "Cash";
    private Calendar myCalendar;
    private TextView mTvDate;
    private TextView mTvTime;
    private JSONObject startEndTimeObj;
    AutoCompleteAddress autoCompleteAddressResponce;
    private JSONArray countryArray;
    JSONArray startEndTimeArray;
    private JSONObject countryCurrencyObj;
    String cabDetail;
    private RelativeLayout mRlCashPayPal, mRlCashCash, mRlCashCreditCard, mRlCancel;
    private DatePickerDialog.OnDateSetListener date;
    private String bookingDateTime = "";
    private SimpleDateFormat bookingFormat;
    private int deviseWidth;
    private String transactionId ="";
    private boolean isSetData = false;
    private Common common = new Common();

    private boolean locationDistance = false;
    private Marker pickupMarker;
    private Marker dropMarker;
    private int cabPositon = 0;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(Constants.CONFIG_ENVIRONMENT)
            .clientId(Constants.CONFIG_CLIENT_ID);
    private String bookingType;
    private ArrayList<HashMap<String, String>>

            FixRateArray;
    private Dialog progressDialog;
    private RotateLoading cusRotateLoading;
    private RecyclerView mRecycleCabDetail;
    private RelativeLayout mLayoutBook;
    RelativeLayout relativeLayoutSelectSeatSpinner;
    private String userDetail;
    private String timeDetail;
    private String countryDetail;
    private ArrayAdapter<String> dataAdapter;

    private RecyclerView mRlRecyclerView;
    private AddressSuggestionAdapter addressSuggestionAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CardView mcardView;
    private int _hour, _minute;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mUserPref = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        progressDialog = new Dialog(HomeActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);
        nowDialog = new Dialog(HomeActivity.this, R.style.DialogUpDownAnim);
        nowDialog.setContentView(R.layout.now_dialog_layout);

        initViews();

        mRlRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRlRecyclerView.setLayoutManager(layoutManager);
        mTvCardescription.setSelected(true);
        userPref = PreferenceManager.getDefaultSharedPreferences(this);
        calculationPresenterimp = new CalculationPresenterimp(this);
        cabDetail = userPref.getString("cabDetail", "No name defined");
        userDetail = userPref.getString("userDetail", " no name");
        timeDetail = userPref.getString("timeDetail", " namame");
        countryDetail = userPref.getString("countryDetail", "");
        dataAdapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // JSONArray startEndTimeArray = new JSONArray(timeDetail);

       /* Log.d("HomeActivity cab", cabDetail);
        Log.d("HomeActivity user", userDetail);
        Log.d("HomeActivity  time", timeDetail);*/


        String bookinCancel = getIntent().getStringExtra("cancel_booking");
        if (bookinCancel != null && bookinCancel.equals("1")) {
            Common.showMkSucess(HomeActivity.this, getResources().getString(R.string.your_booking_cancel), "yes");
        }


        arrayPoints = new ArrayList<LatLng>();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        deviseWidth = displaymetrics.widthPixels;

        mRlLayoutNow.getLayoutParams().width = (int) (deviseWidth * 0.50);

        RelativeLayout.LayoutParams resParam = new RelativeLayout.LayoutParams((int) (deviseWidth * 0.51), ViewGroup.LayoutParams.WRAP_CONTENT);
        resParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        resParam.addRule(RelativeLayout.ALIGN_PARENT_END);

        mRlLayoutReservation.setLayoutParams(resParam);

        bookingFormat = new SimpleDateFormat("h:mm a, d, MMM yyyy,EEE");

        initTypeFace();
        setTypeFace();

        pickupDragLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRecyclePickupLocation.setLayoutManager(pickupDragLayoutManager);

        /*get Current Location And Set Edittext*/
        pickupLatitude = getIntent().getDoubleExtra("pickupLatitude", 0.0);
        pickupLongtude = getIntent().getDoubleExtra("pickupLongtude", 0.0);

        gpsTracker = new GPSTracker(HomeActivity.this);
        if (pickupLongtude != 0.0 && pickupLatitude != 0.0) {
            bothLocationString = "pickeup";
            if (Common.isNetworkAvailable(HomeActivity.this)) {
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(pickupLatitude, pickupLongtude,
                        getApplicationContext(), new GeocoderHandler());

                mPickupLarLng = new LatLng(pickupLatitude, pickupLongtude);
                ClickOkButton = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MarkerAdd();
                    }
                }, 1000);
            } else {
                Toast.makeText(HomeActivity.this, getString(R.string.no_network_connection), Toast.LENGTH_LONG).show();
            }
        } else {

            if (gpsTracker.checkLocationPermission()) {

                pickupLatitude = gpsTracker.getLatitude();
                pickupLongtude = gpsTracker.getLongitude();
                mPickupLarLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                ClickOkButton = true;

                bothLocationString = "pickeup";

                WebService.getInstance().letsDoLanLongGeocode(pickupLatitude, pickupLongtude, "noida", this);

                if (Common.isNetworkAvailable(HomeActivity.this)) {
                    LocationAddress locationAddress = new LocationAddress();
                   /* locationAddress.getAddressFromLocation(pickupLatitude, pickupLongtude,
                            getApplicationContext(), new GeocoderHandler());*/

                    mPickupLarLng = new LatLng(pickupLatitude, pickupLongtude);
                    ClickOkButton = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MarkerAdd();
                        }
                    }, 1000);
                } else {
                    Toast.makeText(HomeActivity.this, "No Network", Toast.LENGTH_LONG).show();
                }

            } else {
                gpsTracker.showSettingsAlert();
            }
        }

        Log.d("gpsTracker", "gpsTracker =" + gpsTracker.canGetLocation() + "==" + gpsTracker.checkLocationPermission());


        metDropLocation.addTextChangedListener(new TextWatcher() {

            int startPosition = 0;
            int endPosition = 0;

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    if (autoCompleteAddressResponce != null) {
                        autoCompleteAddressResponce.getPredictions().clear();
                        addressSuggestionAdapter.notifyDataSetChanged();
                    }
                }
                Log.e("OnText Changed", "OnTextChanged start = " + start + "before = " + before + "count = " + count);
                startPosition = start;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count == 0) {
                    mRlRecyclerView.setVisibility(View.GONE);
                    mcardView.setVisibility(View.GONE);

                    if (autoCompleteAddressResponce != null) {

                        autoCompleteAddressResponce.getPredictions().clear();
                        addressSuggestionAdapter.notifyDataSetChanged();
                    }

                }

            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0 && autoCompleteAddressResponce != null) {
                    autoCompleteAddressResponce.getPredictions().clear();
                    addressSuggestionAdapter.notifyDataSetChanged();
                }
                if (s.toString().length() == 0) {
                    mRlRecyclerView.setVisibility(View.GONE);
                    mcardView.setVisibility(View.GONE);

                }
                if (!isSetData) {
                    endPosition = s.length();
                    Log.e("afterTextChanged", "afterTextChanged endposition = " + endPosition);
                    if (endPosition > startPosition) {
                        hitForAddressSuggestionService(s);
                    }
                } else {
                    if (autoCompleteAddressResponce != null) {
                        autoCompleteAddressResponce.getPredictions().clear();
                    }
                    mRlRecyclerView.setVisibility(View.GONE);
                    mcardView.setVisibility(View.GONE);


                    isSetData = false;
                }
            }
        });

        /*Pickup Location autocomplate start*/
        //LocationAutocompleate(mEtPickUpLocation, "pickeup");
        EditorActionListener(mEtPickUpLocation, "pickeup");
        AddTextChangeListener(mEtPickUpLocation, "pickeup");
        AddSetOnClickListener(mEtPickUpLocation, "pickeup");
        /*Pickup Location autocomplate end*/

        /*Drop Location autocomplate start*/
        //LocationAutocompleate(metDropLocation, "drop");
        EditorActionListener(metDropLocation, "drop");
        //    AddTextChangeListener(metDropLocation, "drop");
        //AddSetOnClickListener(metDropLocation, "drop");
        /*Drop Location autocomplate end*/

        /*Slide Menu Start*/

        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setBehindOffsetRes(R.dimen.slide_menu_width);
        slidingMenu.setFadeDegree(0.20f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.left_menu);

        common.SlideMenuDesign(slidingMenu, HomeActivity.this, "home");

        mRlSlideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });

        /*Slide Menu End*/

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*Get Day Night Time*/


        try {
            startEndTimeArray = new JSONArray(timeDetail);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int si = 0; si < startEndTimeArray.length(); si++) {
            try {
                startEndTimeObj = startEndTimeArray.getJSONObject(si);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        try {

            String currentLocalTime = currentTime.format(new Date());
            Date StarDateFrm = null;
            try {
                if (!startEndTimeObj.get("day_start_time").equals(""))
                    StarDateFrm = currentTime.parse(startEndTimeObj.getString("day_start_time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Date EndDateFrm = null;
            if (!startEndTimeObj.getString("day_end_time").equals(""))
                EndDateFrm = currentTime.parse(startEndTimeObj.getString("day_end_time"));

            Date CurDateFrm = currentTime.parse(currentLocalTime);

            if (StarDateFrm != null && EndDateFrm != null) {
                if (CurDateFrm.before(StarDateFrm) || CurDateFrm.after(EndDateFrm)) {
                    Log.d("get time", "get time = before");
                    dayNight = "night";
                } else {
                    dayNight = "day";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*Now Image Click popup start*/
        //nowDialog = new Dialog(HomeActivity.this,android.R.style.Theme_Translucent_NoTitleBar);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            //noinspection ConstantConditions
            nowDialog.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        mspinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                person = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        relativeLayoutSelectSeatSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mspinnerPerson.performClick();
            }
        });


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
            mTvCurrency.setText(countryCurrencyObj.getString("currency"));
            mTvFirstCurrency.setText(countryCurrencyObj.getString("currency"));
            mTvSecondCurrency.setText(countryCurrencyObj.getString("currency"));
            mTvThdCurrency.setText(countryCurrencyObj.getString("currency"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecycleCabDetail.setLayoutManager(categoryLayoutManager);

        cabDetailArray = new ArrayList<CabDetail>();


        mLayoutBook.getLayoutParams().width = (int) (deviseWidth * 0.50);
        mLayoutBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEtPickUpLocation.getText().toString().trim().length() == 0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.enter_pickup));
                    return;
                } else if (metDropLocation.getText().toString().trim().length() == 0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.enter_drop));
                    return;
                }

                nowDialog.cancel();

                mRlLayoutReservation.setVisibility(View.VISIBLE);
                if (carRate != null && fromIntailRate != null && rideTimeRate != null)
                    totlePrice = Common.getTotalPrice(carRate, FirstKm, distance, fromIntailRate, rideTimeRate, totalTime);
                else
                    totlePrice = 0f;
                Log.d("total price", "total price = " + totlePrice);


                if (totlePrice != 0f) {
                    mRlRecyclerView.setVisibility(View.GONE);
                    Intent bi = new Intent(HomeActivity.this, TripDetailActivity.class);
                    bi.putExtra("pickup_point", mEtPickUpLocation.getText().toString().trim());
                    bi.putExtra("drop_point", userPref.getString("metDropLocation", ""));
                    bi.putExtra("distance", distance);
                    bi.putExtra("truckIcon", truckIcon);
                    bi.putExtra("truckType", truckType);
                    bi.putExtra("cabId", cabId);
                    bi.putExtra("areaId", areaId);
                    bi.putExtra("booking_date", bookingDateTime);
                    bi.putExtra("totlePrice", totlePrice);
                    bi.putExtra("pickupLatitude", pickupLatitude);
                    bi.putExtra("pickupLongtude", pickupLongtude);
                    bi.putExtra("dropLatitude", dropLatitude);
                    bi.putExtra("dropLongitude", dropLongitude);
                    bi.putExtra("comment", mEtWriteComment.getText().toString().trim());
                     bi.putExtra("dayNight", dayNight);
                    bi.putExtra("transferType", transferType);
                    bi.putExtra("PaymentType", PaymentType);
                    bi.putExtra("person", person);
                    bi.putExtra("transactionId", transactionId);
                    bi.putExtra("bookingType", bookingType);
                    bi.putExtra("astTime", astTime);
                    startActivity(bi);

                } else {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.not_valid_total_price));
                }
            }
        });

        RelativeLayout layout_cancle = nowDialog.findViewById(R.id.layout_cancle);
        RelativeLayout.LayoutParams CanParam = new RelativeLayout.LayoutParams((int) (deviseWidth * 0.51), ViewGroup.LayoutParams.WRAP_CONTENT);
        CanParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        CanParam.addRule(RelativeLayout.ALIGN_PARENT_END);
        layout_cancle.setLayoutParams(CanParam);

        layout_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowDialog.cancel();
                mRlLayoutReservation.setVisibility(View.VISIBLE);
            }
        });


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


        try {
            JSONArray CabDetailAry = new JSONArray(cabDetail);


            //noinspection ConstantConditions
            if (CabDetailAry != null && CabDetailAry.length() > 0) {
                Log.d("cabDetailArray", "cabDetailArray = " + cabDetailArray.size());
                if (cabDetailArray != null && cabDetailArray.size() > 0) {

                    CabDetail cabDetails = cabDetailArray.get(0);
                    mTvCarHeader.setText(cabDetails.getCartype().toUpperCase());
                    carName = cabDetails.getCartype().toUpperCase();
                    mTvCardescription.setText(cabDetails.getDescription());

                    JSONArray userDetailOnSignup = new JSONArray(userDetail);


                    //JSONObject cabObj = CabDetailAry.getJSONObject((userDetailOnSignup));


                    if (dayNight.equals("day")) {
                        carRate = cabDetails.getCarRate();
                        fromIntailRate = cabDetails.getFromintailrate();
                        if (cabDetails.getRideTimeRate() != null) {
                            rideTimeRate = cabDetails.getRideTimeRate();
                        }
                    } else if (dayNight.equals("night")) {
                        carRate = cabDetails.getNightIntailrate();
                        fromIntailRate = cabDetails.getNightFromintailrate();
                        if (cabDetails.getNightRideTimeRate() != null && !cabDetails.getNightRideTimeRate().equals("0")) {
                            rideTimeRate = cabDetails.getNightRideTimeRate();
                        }
                    }
                    mTvFirstPrice.setText(carRate);
                    FirstKm = Float.parseFloat(cabDetails.getIntialkm());
                    mTvFirstKm.setText(getResources().getString(R.string.first) + " " + FirstKm + " " + getResources().getString(R.string.km));
                    mTvSecondPrice.setText(fromIntailRate + "/" + getResources().getString(R.string.km));
                    mTvSecKm.setText(getResources().getString(R.string.after) + " " + FirstKm + " " + getResources().getString(R.string.km));

                    if (cabDetails.getRideTimeRate() != null || cabDetails.getNightRideTimeRate() != null && !cabDetails.getNightRideTimeRate().equals("0")) {
                        mRlThree.setVisibility(View.VISIBLE);
                        mTvThdPrice.setText(rideTimeRate + "/" + getResources().getString(R.string.min));
                    } else {
                        mRlThree.setVisibility(View.GONE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1.5f;
                        mRlOne.setLayoutParams(params);
                        mRlTwo.setLayoutParams(params);
                    }

                    truckIcon = cabDetails.getIcon();
                    truckType = cabDetails.getCartype();
                    cabId = cabDetails.getId();
                    areaId = cabDetails.getAreaId();
                    transferType = cabDetails.getTransfertype();

                    cabDetailAdapter = new CabDetailAdapter(HomeActivity.this, cabDetailArray);
                    mRecycleCabDetail.setAdapter(cabDetailAdapter);
                    cabDetailAdapter.setOnCabDetailItemClickListener(HomeActivity.this);
                    cabDetailAdapter.updateItems();

                    list = new ArrayList<String>();
                    for (int si = 0; si < Integer.parseInt(cabDetails.getSeatCapacity()); si++) {
                        int seat = si + 1;
                        list.add(seat + "");
                    }

                    dataAdapter = new ArrayAdapter<String>(HomeActivity.this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mspinnerPerson.setAdapter(dataAdapter);

                    Log.d("Fix Price", "Fix Price = " + cabDetails.getFixPrice());

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray CabDetailAry = new JSONArray(cabDetail);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mRlLayoutNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("length ", "length = " + mEtPickUpLocation.getText().toString().length());
                if (mEtPickUpLocation.getText().toString().length() == 0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.enter_pickup));
                    return;
                } else if (metDropLocation.getText().toString().length() == 0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.enter_drop));
                    return;
                } else if (!locationDistance) {
                    //Common.showMkError(HomeActivity.this, getResources().getString(R.string.location_long));
                    return;
                } else if (distance == 0.0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.location_short));
                    return;
                }

                bookingDateTime = bookingFormat.format(Calendar.getInstance().getTime());

                bookingType = "Now";
                mRlLayoutReservation.setVisibility(View.GONE);


                nowDialog.show();

                //noinspection ConstantConditions
                if (fromIntailRate != null && fromIntailRate != null && rideTimeRate != null)
                    totlePrice = Common.getTotalPrice(fromIntailRate, FirstKm, distance, fromIntailRate, rideTimeRate, totalTime);
                else
                    totlePrice = 0f;
                Log.d("totlePrice", "totlePrice = " + totlePrice);
                mTvTotalPrice.setText(String.valueOf(totlePrice));
            }
        });


    /*Cash Dialog Strat*/

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            //noinspection ConstantConditions
            try {
                cashDialog.getWindow().setStatusBarColor(Color.TRANSPARENT);
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, getString(R.string.issue), Toast.LENGTH_SHORT);
            }
        }

        mRlCashCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashDialog.cancel();
                nowDialog.show();
                PaymentType = "Cash";
            }
        });
        mRlCashPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentType = "Paypal";
                onBuyPressed();
            }
        });
        mRlCashCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentType = "Stripe";
                Intent si = new Intent(HomeActivity.this, StripeFormActivity.class);
                startActivityForResult(si, Constants.REQUEST_CODE_STRIPE);
            }
        });
        mRlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashDialog.cancel();
                nowDialog.show();
                PaymentType = "Cash";
            }
        });

        mRlCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowDialog.cancel();
                cashDialog.show();
            }
        });

    /*Cash Dialog End*/

        /*Now Image Click popup end*/

        /*Reservation Image Click popup start*/

        myCalendar = Calendar.getInstance();
        try {
            date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };

        } catch (Exception e) {

        }


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            //noinspection ConstantConditions
            try {
                reservationDialog.getWindow().setStatusBarColor(Color.TRANSPARENT);
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, getString(R.string.tech_issue), Toast.LENGTH_SHORT);
            }

        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        RelativeLayout layout_res = reservationDialog.findViewById(R.id.layout_res);
        layout_res.getLayoutParams().height = (int) (dm.heightPixels * 0.40);

        RelativeLayout layout_select_date = reservationDialog.findViewById(R.id.layout_select_date);
        layout_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar aftoneMont = Calendar.getInstance();
                aftoneMont.add(Calendar.MONTH, 1);

                DatePickerDialog dpd = new DatePickerDialog(HomeActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date d = null;
                try {
                    String formattedDate = sdf.format(Calendar.getInstance().getTime());
                    d = sdf.parse(formattedDate);

                    //noinspection ConstantConditions
                    dpd.getDatePicker().setMinDate(d.getTime());
                    dpd.getDatePicker().setMaxDate(aftoneMont.getTimeInMillis());
                    dpd.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        RelativeLayout layout_select_time = reservationDialog.findViewById(R.id.layout_select_time);
        TextView txt_time = reservationDialog.findViewById(R.id.txt_time);
        if (txt_time.getText().toString().equals("")) {
            _hour = myCalendar.get(Calendar.HOUR_OF_DAY);
            _minute = myCalendar.get(Calendar.MINUTE);
        } else {
            String timeText = txt_time.getText().toString();

            String[] timeset = timeText.split(":");
            _hour = Integer.parseInt(timeset[0]);
            _minute = Integer.parseInt(timeset[1]);
        }
        layout_select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(HomeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int selectedMinute) {

                        String status = "AM";
                        int hour_of_12_hour_format;

                        if (hourOfDay > 11) {
                            status = "PM";
                        }
                        if (hourOfDay > 11) {
                            hour_of_12_hour_format = hourOfDay - 12;
                        } else {
                            hour_of_12_hour_format = hourOfDay;
                        }

                        if (String.valueOf(selectedMinute).length() < 2) {
                            mTvTime.setText(hour_of_12_hour_format + " : " + "0" + selectedMinute + " " + status);

                            _hour = hourOfDay;
                            _minute = selectedMinute;
                        } else {
                            mTvTime.setText(hour_of_12_hour_format + ":" + selectedMinute + " " + status);
                        }
                    }
                }, _hour, _minute, false);//Yes 24 hour time

                mTimePicker.setTitle(getResources().getString(R.string.select_time_res));

                mTimePicker.show();
            }
        });


        RelativeLayout layout_done = reservationDialog.findViewById(R.id.layout_done);
        layout_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("mTvDate ", "mTvDate = " + mTvDate.getText().toString());
                if (mTvDate.getText().toString().length() == 0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.please_enter_date));
                    return;
                } else if (mTvTime.getText().length() == 0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.please_enter_time));
                    return;
                }

                SimpleDateFormat currentDateFormate = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");
                String DateTimeString = mTvDate.getText() + " " + mTvTime.getText();
                String SeletedtDate = "";
                Date SeletDate;
                try {
                    SeletDate = currentDateFormate.parse(DateTimeString);
                    SeletedtDate = currentDateFormate.format(SeletDate.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String currentDate = currentDateFormate.format(Calendar.getInstance().getTime());
                boolean dateVal = CheckDates(DateTimeString, currentDate);

                /**//*After One Month Validation*//**/
                boolean afterOneMonth = false;
                Calendar onMonCal = Calendar.getInstance();
                onMonCal.add(Calendar.MONTH, 1);
                String curOneMonDate = currentDateFormate.format(onMonCal.getTime());
                SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");
                Log.d("curOneMonDate", "curOneMonDate = " + curOneMonDate + "==" + DateTimeString);
                try {
                    Date CrtDate = dfDate.parse(curOneMonDate);
                    Date SelDate = dfDate.parse(DateTimeString);
                    if (SelDate.after(CrtDate)) {
                        Log.d("After", "curOneMonDate After One");
                        afterOneMonth = true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    SimpleDateFormat selectedDateFormate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date parshDate = selectedDateFormate.parse(DateTimeString);
                    bookingDateTime = bookingFormat.format(parshDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("date Error", "DateTimeString Error = " + e.getMessage());
                }
                //SimpleDateFormat bookingFormat = new SimpleDateFormat("h:mm a, d, MMM yyyy,EEE");
                Log.d("DateTimeString", "DateTimeString one= " + DateTimeString);
                Log.d("DateTimeString", "DateTimeString two= " + currentDate);
                Log.d("DateTimeString", "DateTimeString three= " + dateVal);
                Log.d("DateTimeString", "DateTimeString for= " + bookingDateTime);
                if (afterOneMonth) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.time_is_large));
                    return;
                } else if (!dateVal) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.date_time_not_valid));
                    return;
                }
                try {

                    Date ResCurDateFrm = currentTime.parse(mTvTime.getText().toString());
                    Date ResStarDateFrm = null;
                    if (!Common.StartDayTime.equals(""))
                        ResStarDateFrm = currentTime.parse(Common.StartDayTime);

                    Date ResEndDateFrm = null;
                    if (!Common.StartDayTime.equals(""))
                        ResEndDateFrm = currentTime.parse(Common.EndDayTime);

                    if (ResStarDateFrm != null && ResEndDateFrm != null) {
                        if (ResCurDateFrm.before(ResStarDateFrm) || ResCurDateFrm.after(ResEndDateFrm)) {
                            Log.d("get time", "get time = before");
                            dayNight = "night";
                        } else {
                            dayNight = "day";
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Log.d("dayNight", "dayNight = " + dayNight);
                CabDetail ResCabDetails = cabDetailArray.get(cabPositon);
                if (dayNight.equals("day")) {
                    carRate = ResCabDetails.getCarRate();
                    fromIntailRate = ResCabDetails.getFromintailrate();
                    if (ResCabDetails.getRideTimeRate() != null) {
                        rideTimeRate = ResCabDetails.getRideTimeRate();
                    }
                } else if (dayNight.equals("night")) {
                    carRate = ResCabDetails.getNightIntailrate();
                    fromIntailRate = ResCabDetails.getNightFromintailrate();
                    if (ResCabDetails.getNightRideTimeRate() != null && !ResCabDetails.getNightRideTimeRate().equals("0")) {
                        rideTimeRate = ResCabDetails.getNightRideTimeRate();
                    }
                }
                if (ResCabDetails.getRideTimeRate() != null || ResCabDetails.getNightRideTimeRate() != null && !ResCabDetails.getNightRideTimeRate().equals("0")) {
                    mRlThree.setVisibility(View.VISIBLE);
                    mTvThdPrice.setText(rideTimeRate + "/" + getResources().getString(R.string.min));
                }
                mTvFirstPrice.setText(carRate);
                mTvSecondPrice.setText(fromIntailRate + "/" + getResources().getString(R.string.km));

                mRlLayoutReservation.setVisibility(View.GONE);
                nowDialog.show();
                reservationDialog.cancel();

            }
        });

        mRlLayoutReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("length ", "length = " + mEtPickUpLocation.getText().toString().length());
                if (mEtPickUpLocation.getText().toString().length() == 0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.enter_pickup));
                    return;
                } else if (metDropLocation.getText().toString().length() == 0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.enter_drop));
                    return;
                } else if (!locationDistance) {
                    //
                    // Common.showMkError(HomeActivity.this, getResources().getString(R.string.location_long));
                    return;
                } else if (distance == 0.0) {
                    Common.showMkError(HomeActivity.this, getResources().getString(R.string.location_short));
                    return;
                }

                bookingType = "Reservation";
                reservationDialog.show();
            }
        });

        /*Reservation Image Click popup end*/

        mIvPickUpClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtPickUpLocation.setText("");
                mPickupLarLng = null;
                pickupLatitude = 0.0;
                pickupLongtude = 0.0;
                MarkerAdd();
            }
        });


    }

    private void hitForAddressSuggestionService(CharSequence s) {
        String s1 = s.toString();
        if (autoCompleteAddressResponce != null) {
            autoCompleteAddressResponce.getPredictions().clear();
        }
        if (!s1.trim().equals("")) {
            if (s1.length() > 2) {
                WebService.getInstance().letsAutoCompleteAddress(s.toString(), pickupLatitude, pickupLongtude, HomeActivity.this);
            } else {
                mRlRecyclerView.setVisibility(View.GONE);
                mcardView.setVisibility(View.GONE);
            }
        } else {
            mRlRecyclerView.setVisibility(View.GONE);
            mcardView.setVisibility(View.GONE);
        }
    }

    private void setTypeFace() {
        try {
            mTvHome.setTypeface(openSansBold);
            mTvNotFound.setTypeface(openSansBold);
            mTvLoaction.setTypeface(robotoBold);
            mTvReservation.setTypeface(robotoBold);
            mTvNow.setTypeface(robotoBold);

            mEtPickUpLocation.setTypeface(openSansRegular);
            metDropLocation.setTypeface(openSansRegular);
            mEtWriteComment.setTypeface(robotoRegular);
            mTvNoLocation.setTypeface(robotoRegular);
            mTvPleaseCheck.setTypeface(robotoRegular);


            mTvCancel.setTypeface(robotoBold);
            mTvCarHeader.setTypeface(robotoMedium);
            mTvCurrency.setTypeface(robotoRegular);
            mTvFarBreakUp.setTypeface(robotoBold);
            mTvBook.setTypeface(robotoBold);
            mTvCardescription.setTypeface(robotoRegular);
            mTvFirstPrice.setTypeface(robotoRegular);
            mTvFirstKm.setTypeface(robotoRegular);
            mTvSecondPrice.setTypeface(robotoRegular);
            mTvSecKm.setTypeface(robotoRegular);
            mTvThdPrice.setTypeface(robotoRegular);
            mTvSpclCharNote.setTypeface(robotoRegular);

            mTvTotalPrice.setTypeface(robotoRegular);
            mTvSecondCurrency.setTypeface(robotoBold);
            mTvThdCurrency.setTypeface(robotoBold);
            mTvFirstCurrency.setTypeface(robotoBold);
            mTvCash.setTypeface(robotoRegular);

        } catch (NullPointerException e) {

        }


    }

    private void initTypeFace() {
        openSansBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        robotoRegular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        robotoMedium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        robotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

    }

    private void initViews() {
        mRecycleCabDetail = nowDialog.findViewById(R.id.recycle_cab_detail);
        mLayoutBook = nowDialog.findViewById(R.id.layout_book);
        relativeLayoutSelectSeatSpinner = nowDialog.findViewById(R.id.select_seat_capacity_layout);
        mTvHome = nowDialog.findViewById(R.id.txt_home);
        mRlSlideMenu = findViewById(R.id.layout_slidemenu);
        mEtPickUpLocation = findViewById(R.id.edt_pickup_location);
        metDropLocation = findViewById(R.id.edt_drop_location);
        mEtWriteComment = findViewById(R.id.edt_write_comment);
        mRlLayoutNow = findViewById(R.id.layout_now);
        mRlLayoutReservation = findViewById(R.id.layout_reservation);
        mIvPickUpClose = findViewById(R.id.img_pickup_close);

        mRecyclePickupLocation = findViewById(R.id.recycle_pickup_location);
        mRlPickupDragLocation = findViewById(R.id.layout_pickup_drag_location);
        mLayoutNoResults = findViewById(R.id.layout_no_result);
        mTvNotFound = findViewById(R.id.txt_not_found);
        mTvNoLocation = findViewById(R.id.no_location);
        mTvPleaseCheck = findViewById(R.id.please_check);
        mTvLoaction = findViewById(R.id.txt_locatons);
        mTvReservation = findViewById(R.id.txt_reservation);
        mTvNow = findViewById(R.id.txt_now);
        cusRotateLoading = progressDialog.findViewById(R.id.rotateloading_register);

        mTvCarHeader = nowDialog.findViewById(R.id.txt_car_header);
        mTvCurrency = nowDialog.findViewById(R.id.txt_currency);
        mTvFarBreakUp = nowDialog.findViewById(R.id.txt_far_breakup);
        mTvBook = nowDialog.findViewById(R.id.txt_book);
        mTvCancel = nowDialog.findViewById(R.id.txt_cancel);
        mTvCardescription = nowDialog.findViewById(R.id.txt_car_descriptin);
        mTvFirstPrice = nowDialog.findViewById(R.id.txt_first_price);
        mTvFirstKm = nowDialog.findViewById(R.id.txt_first_km);
        mTvSecondPrice = nowDialog.findViewById(R.id.txt_sec_pric);
        mTvSecKm = nowDialog.findViewById(R.id.txt_sec_km);
        mTvThdPrice = nowDialog.findViewById(R.id.txt_thd_price);
        mRlOne = nowDialog.findViewById(R.id.layout_one);
        mRlTwo = nowDialog.findViewById(R.id.layout_two);
        mRlThree = nowDialog.findViewById(R.id.layout_three);
        mTvTotalPrice = nowDialog.findViewById(R.id.txt_total_price);
        mTvCash = nowDialog.findViewById(R.id.txt_cash);
        mspinnerPerson = nowDialog.findViewById(R.id.spinner_person);
        mTvFirstCurrency = nowDialog.findViewById(R.id.txt_first_currency);
        mTvSecondCurrency = nowDialog.findViewById(R.id.txt_secound_currency);
        mTvThdCurrency = nowDialog.findViewById(R.id.txt_thd_currency);
        mLayoutTimming = nowDialog.findViewById(R.id.layout_timming);
        mrlFarBrkUp = nowDialog.findViewById(R.id.layout_far_breakup);
        mTvSpclCharNote = nowDialog.findViewById(R.id.txt_specailChr_note);

        reservationDialog = new Dialog(HomeActivity.this, R.style.DialogUpDownAnim);
        reservationDialog.setContentView(R.layout.reservation_dialog_layout);
        mTvDate = reservationDialog.findViewById(R.id.txt_date);
        mTvTime = reservationDialog.findViewById(R.id.txt_time);

        cashDialog = new Dialog(HomeActivity.this, R.style.DialogUpDownAnim);
        cashDialog.setContentView(R.layout.cash_dialog_layout);
        mRlCashPayPal = cashDialog.findViewById(R.id.layout_cash_paypal);
        mRlCancel = cashDialog.findViewById(R.id.layout_cash_cancel);
        mRlCashCreditCard = cashDialog.findViewById(R.id.layout_cash_credit_card);
        mRlCash = nowDialog.findViewById(R.id.layout_cash);
        mRlCashCash = cashDialog.findViewById(R.id.layout__cash_cash);

        mRlRecyclerView = findViewById(R.id.recyclerviewForPlace);
        mcardView = findViewById(R.id.card_view);
    }

    public void onBuyPressed() {
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(HomeActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, Constants.REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(Math.round(totlePrice)), "USD", carName,
                paymentIntent);
    }

    public static boolean CheckDates(String startDate, String currentDate) {
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        boolean b = false;
        try {
            if (dfDate.parse(startDate).after(dfDate.parse(currentDate))) {
                b = true;//If start date is before end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return b;
    }


    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        mTvDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        Log.d("Map Ready", "Map Ready" + gpsTracker.getLatitude() + "==" + gpsTracker.getLongitude());
    }

    /**
     * When the map is not ready the CameraUpdateFactory cannot be used. This should be called on
     * all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (googleMap == null) {
            Toast.makeText(this, "Google Map not ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*public void CaculationDirationIon() throws JSONException {
        String CaculationLocUrl = "";


        CaculationLocUrl = "http://maps.googleapis.com/maps/api/directions/json?sensor=true&mode=driving&origin=" + pickupLatitude + "," + pickupLongtude + "&destination=" + dropLatitude + "," + dropLongitude;
        Log.d("CaculationLocUrl", "CaculationLocUrl = " + CaculationLocUrl);
        Ion.with(HomeActivity.this)
                .load(CaculationLocUrl)
                .setTimeout(10000)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception error, JsonObject result) {
                        // do stuff with the result or error

                        progressDialog.cancel();
                        cusRotateLoading.stop();

                        Log.d("Login result", "Login result = " + result + "==" + error);
                        if (error == null) {
                            try {
                                JSONObject resObj = new JSONObject(result.toString());
                                if (resObj.getString("status").toLowerCase().equals("ok")) {


                                    JSONArray routArray = new JSONArray(resObj.getString("routes"));
                                    JSONObject routObj = routArray.getJSONObject(0);
                                    Log.d("geoObj", "DrowLocUrl geoObj one= " + routObj);
                                    JSONArray legsArray = new JSONArray(routObj.getString("legs"));
                                    JSONObject legsObj = legsArray.getJSONObject(0);

                                    JSONObject disObj = new JSONObject(legsObj.getString("distance"));
                                    if (disObj.getInt("value") > 1000)
                                        distance = (float) disObj.getInt("value") / 1000;
                                    else if (disObj.getInt("value") > 100)
                                        distance = (float) disObj.getInt("value") / 100;
                                    else if (disObj.getInt("value") > 10)
                                        distance = (float) disObj.getInt("value") / 10;
                                    else if (disObj.getInt("value") == 0)
                                        distance = (float) disObj.getInt("value");
                                    Log.d("distance", "distance = " + distance);
                                    Log.d("dis", "dis = " + distance);

                                    JSONObject duration = new JSONObject(legsObj.getString("duration"));

                                    astTime = duration.getString("text");
                                    String[] durTextSpi = astTime.split(" ");
                                    Log.d("durTextSpi", "min  = durTextSpi = " + durTextSpi.length);
                                    int hours = 0;
                                    int mintus = 0;
                                    if (durTextSpi.length == 4) {
                                        hours = Integer.parseInt(durTextSpi[0]) * 60;
                                        mintus = Integer.parseInt(durTextSpi[2]);
                                    } else if (durTextSpi.length == 2) {
                                        if (durTextSpi[1].contains("mins"))
                                            mintus = Integer.parseInt(durTextSpi[0]);
                                        else
                                            mintus = Integer.parseInt(durTextSpi[0]);
                                    }
                                    Log.d("hours", "hours = " + hours + "==" + mintus);
                                    totalTime = mintus + hours;

                                    googleDuration = duration.getInt("value");


                                    try {
                                        if (cabDetailArray.size() > 0) {
                                            CabDetail cabDetails = cabDetailArray.get(0);
                                            if (!cabDetails.getFixPrice().equals("")) {
                                                mLayoutTimming.setVisibility(View.GONE);
                                                mrlFarBrkUp.setVisibility(View.INVISIBLE);
                                                totlePrice = Float.parseFloat(cabDetails.getFixPrice());
                                                mTvTotalPrice.setText(Math.round(totlePrice) + "");
                                            } else {
                                                Log.d("fromIntailRate", "fromIntailRate = " + carRate + "==" + FirstKm + "==" + distance + "==" + fromIntailRate + "==" + rideTimeRate + "==" + totalTime);
                                                if (carRate != null && fromIntailRate != null && rideTimeRate != null)
                                                    totlePrice = Common.getTotalPrice(carRate, FirstKm, distance, fromIntailRate, rideTimeRate, totalTime);
                                                else
                                                    totlePrice = 0f;

                                                Log.d("totlePrice", "totlePrice = " + totlePrice);

                                                mLayoutTimming.setVisibility(View.VISIBLE);
                                                mrlFarBrkUp.setVisibility(View.VISIBLE);
                                                mTvTotalPrice.setText(Math.round(totlePrice) + "");
                                            }
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        System.out.println("Array Index Out of Bound Exception");
                                    }
                                    locationDistance = true;


                                } else {
                                    locationDistance = false;
                                    //Toast.makeText(HomeActivity.this, "Press again to confirm", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Common.ShowHttpErrorMessage(HomeActivity.this, error.getMessage());
                        }
                    }
                });

        MarkerAdd();

        SetNowDialogCabValue();
    }*/

    @Override
    public void onGeocodeSuccess(AddressFromGeocode addressFromGeocode) {
        Log.d("HomeActivity ", addressFromGeocode.getResults().get(0).getFormattedAddress());
        if (addressFromGeocode.getResults().get(0).getFormattedAddress() != null) {
            if (addressFromGeocode.getResults().get(0).getFormattedAddress().equals("Unable connect to Geocoder")) {
                Toast.makeText(HomeActivity.this, getString(R.string.no_network_connection), Toast.LENGTH_LONG).show();
            } else {
                Log.d("locationAddress", "locationAddress = " + addressFromGeocode.getResults().get(0).getFormattedAddress() + "==" + bothLocationString);
                if (bothLocationString.equals("pickeup") && mEtPickUpLocation != null) {
                    mEtPickUpLocation.setText(addressFromGeocode.getResults().get(0).getFormattedAddress());
                    metDropLocation.requestFocus();
                } else if (bothLocationString.equals("drop") && metDropLocation != null)
                    metDropLocation.setText(addressFromGeocode.getResults().get(0).getFormattedAddress());
            }
        } else {
            nowDialog.cancel();
            mRlLayoutReservation.setVisibility(View.VISIBLE);


        }
        MarkerAdd();
    }


    @Override
    public void onGeocodeFailure() {
        Log.d("HomeActivity vinod", "failure");
    }

    @Override
    public void onSuccessAutoSearchAddress(AutoCompleteAddress autoCompleteAddress) {
        Log.e("vinod", "Location search success");
        if (metDropLocation.getText().length() > 2) {
            autoCompleteAddressResponce = autoCompleteAddress;
            mRlRecyclerView.setVisibility(View.VISIBLE);
            mcardView.setVisibility(View.VISIBLE);
            addressSuggestionAdapter = new AddressSuggestionAdapter(HomeActivity.this, autoCompleteAddressResponce, this);
            mRlRecyclerView.setAdapter(addressSuggestionAdapter);
            addressSuggestionAdapter.notifyDataSetChanged();
        } else {
            mRlRecyclerView.setVisibility(View.GONE);
            mcardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailureAutoSearchAddress() {
        Log.e("vinod", "Location search cancelled");
        mRlRecyclerView.setVisibility(View.GONE);
        mcardView.setVisibility(View.GONE);
    }

    @Override
    public void setData(String address) {
        isSetData = true;
        if (autoCompleteAddressResponce != null) {
            autoCompleteAddressResponce.getPredictions().clear();
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        try {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.d("Home Activity", "NullPointer EXCEPTION");
        }
        metDropLocation.setText(address);
        mRlRecyclerView.setVisibility(View.GONE);
        metDropLocation.setSelection(address.length());
        if (Common.isNetworkAvailable(HomeActivity.this)) {
            bothLocationString = "drop";
            LocationAddress.getAddressFromLocation(metDropLocation.getText().toString(), getApplicationContext(), new GeocoderHandlerLatitude());
            SharedPreferences.Editor dropLocation = userPref.edit();
            Log.d("metDropLocation", "................................................");
            Log.d("metDropLocation", metDropLocation.getText().toString());
            dropLocation.putString("metDropLocation", metDropLocation.getText().toString());
            dropLocation.commit();
            //   WebService.getInstance().).letsFindAddressDetail(metDropLocation.getText().toString(),HomeActivity.this);
        } else {
            Toast.makeText(HomeActivity.this, "No Network", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponceDistance(CalculateDistancebean calculateDistancebean) {

        Log.d("hiii abhilasha", calculateDistancebean.getStatus());


        if (calculateDistancebean.getStatus().toLowerCase().equals("ok")) {

            int dist = calculateDistancebean.getRoutes().get(0).getLegs().get(0).getDistance().getValue();


            if (dist > 1000)
                distance = (float) (dist / 1000);
            else if (dist > 100)
                distance = (float) dist / 100;
            else if (dist > 10)
                distance = (float) dist / 10;
            else if (dist == 0)
                distance = (float) dist;
            Log.d("distance", "distance = " + distance);
            Log.d("dis", "dis = " + distance);


            int dura = calculateDistancebean.getRoutes().get(0).getLegs().get(0).getDuration().getValue();

            astTime = calculateDistancebean.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                String[] durTextSpi = astTime.split(" ");
            Log.d("durTextSpi", "min  = durTextSpi = " + durTextSpi.length);
            int hours = 0;
            int mintus = 0;
            if (durTextSpi.length == 4) {
                hours = Integer.parseInt(durTextSpi[0]) * 60;
                mintus = Integer.parseInt(durTextSpi[2]);
            } else if (durTextSpi.length == 2) {
                if (durTextSpi[1].contains("mins"))
                    mintus = Integer.parseInt(durTextSpi[0]);
                else
                    mintus = Integer.parseInt(durTextSpi[0]);
            }
            Log.d("hours", "hours = " + hours + "==" + mintus);
            totalTime = mintus + hours;

            googleDuration = dura;


            try {
                if (cabDetailArray.size() > 0) {
                    CabDetail cabDetails = cabDetailArray.get(0);
                    if (!cabDetails.getFixPrice().equals("")) {
                        mLayoutTimming.setVisibility(View.GONE);
                        mrlFarBrkUp.setVisibility(View.INVISIBLE);
                        totlePrice = Float.parseFloat(cabDetails.getFixPrice());
                        mTvTotalPrice.setText(Math.round(totlePrice) + "");
                    } else {
                        Log.d("fromIntailRate", "fromIntailRate = " + carRate + "==" + FirstKm + "==" + distance + "==" + fromIntailRate + "==" + rideTimeRate + "==" + totalTime);
                        if (carRate != null && fromIntailRate != null && rideTimeRate != null)
                            totlePrice = Common.getTotalPrice(carRate, FirstKm, distance, fromIntailRate, rideTimeRate, totalTime);
                        else
                            totlePrice = 0f;

                        Log.d("totlePrice", "totlePrice = " + totlePrice);

                        mLayoutTimming.setVisibility(View.VISIBLE);
                        mrlFarBrkUp.setVisibility(View.VISIBLE);
                        mTvTotalPrice.setText(Math.round(totlePrice) + "");
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Array Index Out of Bound Exception");
            }
            locationDistance = true;
        } else {
            Common.ShowHttpErrorMessage(HomeActivity.this, calculateDistancebean.getStatus());
        }
        MarkerAdd();

        try {
            SetNowDialogCabValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideProgressHideProgress() {

    }


    public class CaculationDiration extends AsyncTask<String, Integer, String> {

        private String content = null;

        String DrowLocUrl = "";

        public CaculationDiration() {
            try {
                DrowLocUrl = "http://maps.googleapis.com/maps/api/directions/json?sensor=true&mode=driving&origin=" + URLEncoder.encode(mEtPickUpLocation.getText().toString(), "UTF-8") + "&destination=" + URLEncoder.encode(metDropLocation.getText().toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpParams HttpParams = client.getParams();
                HttpConnectionParams.setConnectionTimeout(HttpParams, 60 * 60 * 1000);
                HttpConnectionParams.setSoTimeout(HttpParams, 60 * 60 * 1000);
                Log.d("DrowLocUrl", "DrowLocUrl = " + DrowLocUrl);
                HttpGet getMethod = new HttpGet(DrowLocUrl);
                //getMethod.setEntity(entity);
                client.execute(getMethod, new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {

                        HttpEntity httpEntity = httpResponse.getEntity();
                        content = EntityUtils.toString(httpEntity);
                        Log.d("Result >>>", "DrowLocUrl Result One" + content);

                        return null;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Indiaries", "DrowLocUrl Result error" + e);
                return e.getMessage();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            boolean isStatus = Common.ShowHttpErrorMessage(HomeActivity.this, result);
            if (isStatus) {
                try {
                    JSONObject resObj = new JSONObject(result);
                    if (resObj.getString("status").toLowerCase().equals("ok")) {


                        JSONArray routArray = new JSONArray(resObj.getString("routes"));
                        JSONObject routObj = routArray.getJSONObject(0);
                        Log.d("geoObj", "DrowLocUrl geoObj one= " + routObj);
                        JSONArray legsArray = new JSONArray(routObj.getString("legs"));
                        JSONObject legsObj = legsArray.getJSONObject(0);

                        JSONObject disObj = new JSONObject(legsObj.getString("distance"));
                        if (disObj.getInt("value") > 1000)
                            distance = (float) disObj.getInt("value") / 1000;
                        else if (disObj.getInt("value") > 100)
                            distance = (float) disObj.getInt("value") / 100;
                        else if (disObj.getInt("value") > 10)
                            distance = (float) disObj.getInt("value") / 10;
                        else if (disObj.getInt("value") == 0)
                            distance = (float) disObj.getInt("value");
                        Log.d("distance", "distance = " + distance);
                        Log.d("dis", "dis = " + distance);

                        JSONObject duration = new JSONObject(legsObj.getString("duration"));

                        String durText = duration.getString("text");
                        String[] durTextSpi = durText.split(" ");
                        Log.d("durTextSpi", "min  = durTextSpi = " + durTextSpi.length);
                        int hours = 0;
                        int mintus = 0;
                        if (durTextSpi.length == 4) {
                            hours = Integer.parseInt(durTextSpi[0]) * 3600;
                            mintus = Integer.parseInt(durTextSpi[2]);
                        } else if (durTextSpi.length == 2) {
                            if (durTextSpi[1].contains("mins"))
                                mintus = Integer.parseInt(durTextSpi[0]);
                            else
                                mintus = Integer.parseInt(durTextSpi[0]) * 3600;
                        }

                        totalTime = mintus + hours;

                        googleDuration = duration.getInt("value");
                        Log.d("fromIntailRate", "fromIntailRate = " + carRate + "==" + FirstKm + "==" + distance + "==" + fromIntailRate + "==" + rideTimeRate + "==" + totalTime);
                        if (carRate != null && fromIntailRate != null && rideTimeRate != null)
                            totlePrice = Common.getTotalPrice(carRate, FirstKm, distance, fromIntailRate, rideTimeRate, totalTime);
                        else
                            totlePrice = 0f;

                        Log.d("totlePrice", "totlePrice = " + totlePrice);

                        mTvTotalPrice.setText(Math.round(totlePrice) + "");
                        locationDistance = true;
                    } else {
                        locationDistance = false;
                        //  Toast.makeText(HomeActivity.this, getResources().getString(R.string.location_long), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void CarDetailTab(int position) {
        cabPositon = position;

        if (cabDetailArray != null && cabDetailArray.size() > 0) {
            CabDetail cabDetails = cabDetailArray.get(position);


            if (dayNight.equals("day")) {
                carRate = cabDetails.getCarRate();
                fromIntailRate = cabDetails.getFromintailrate();
                Log.d("rideTimeRate", "rideTimeRate one day= " + cabDetails.getRideTimeRate());
                if (cabDetails.getRideTimeRate() != null) {
                    rideTimeRate = cabDetails.getRideTimeRate();
                }
            } else if (dayNight.equals("night")) {
                carRate = cabDetails.getNightIntailrate();
                fromIntailRate = cabDetails.getNightFromintailrate();
                Log.d("rideTimeRate", "rideTimeRate one night= " + cabDetails.getRideTimeRate());
                if (cabDetails.getNightRideTimeRate() != null) {
                    rideTimeRate = cabDetails.getNightRideTimeRate();
                }
            }

            Log.d("rideTimeRate", "rideTimeRate two= " + rideTimeRate);

            mTvCarHeader.setText(cabDetails.getCartype().toUpperCase());
            carName = cabDetails.getCartype().toUpperCase();
            mTvCardescription.setText(cabDetails.getDescription());
            mTvFirstPrice.setText("$" + carRate);
            FirstKm = Float.parseFloat(cabDetails.getIntialkm());
            mTvFirstKm.setText("First " + FirstKm + " km");
            mTvSecondPrice.setText("$ " + fromIntailRate + "/km");
            mTvSecKm.setText("After " + FirstKm + " km");

            truckIcon = cabDetails.getIcon();
            truckType = cabDetails.getCartype();
            cabId = cabDetails.getId();
            areaId = cabDetails.getAreaId();
            transferType = cabDetails.getTransfertype();

            if (cabDetails.getRideTimeRate() != null || cabDetails.getNightRideTimeRate() != null && !cabDetails.getNightRideTimeRate().equals("0")) {

                mRlThree.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                mRlOne.setLayoutParams(params);
                mRlTwo.setLayoutParams(params);
                mRlThree.setLayoutParams(params);

                mRlThree.setVisibility(View.VISIBLE);
                mTvThdPrice.setText(rideTimeRate + "/min");
            } else {
                mRlThree.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.5f;
                mRlOne.setLayoutParams(params);
                mRlTwo.setLayoutParams(params);
            }

            for (int i = 0; i < cabDetailArray.size(); i++) {
                CabDetail allcabDetails = cabDetailArray.get(i);
                Log.d("position", "position = " + position + "==" + i);
                if (i == position) {
                    allcabDetails.setIsSelected(true);
                } else {
                    allcabDetails.setIsSelected(false);
                }
            }
            cabDetailAdapter.notifyDataSetChanged();

            list = new ArrayList<String>();
            for (int si = 0; si < Integer.parseInt(cabDetails.getSeatCapacity()); si++) {
                int seat = si + 1;
                list.add(seat + "");
            }
            dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mspinnerPerson.setAdapter(dataAdapter);

            if (!cabDetails.getFixPrice().equals("")) {
                mLayoutTimming.setVisibility(View.GONE);
                mrlFarBrkUp.setVisibility(View.INVISIBLE);
                mTvTotalPrice.setText(cabDetails.getFixPrice());
                totlePrice = Float.parseFloat(cabDetails.getFixPrice());
            } else {
                mLayoutTimming.setVisibility(View.VISIBLE);
                mrlFarBrkUp.setVisibility(View.VISIBLE);
                Log.d("fromIntailRate", "fromIntailRate = " + carRate + "==" + fromIntailRate + "==" + rideTimeRate);
                if (carRate != null && fromIntailRate != null && rideTimeRate != null)
                    totlePrice = Common.getTotalPrice(carRate, FirstKm, distance, fromIntailRate, rideTimeRate, totalTime);
                Log.d("totlePrice", "totlePrice = " + totlePrice);
                mTvTotalPrice.setText(Math.round(totlePrice) + "");
            }

        }
    }


    public void SetNowDialogCabValue() throws JSONException {
        Log.d("cabDetail Home Activity", cabDetail);
        JSONArray CabDetailAry = new JSONArray(cabDetail);
        Log.d("CabDetailAry", "" + CabDetailAry);
        if (CabDetailAry != null && CabDetailAry.length() > 0) {
            Log.d("cabDetailArray", "cabDetailArray = " + cabDetailArray.size());
            if (cabDetailArray != null && cabDetailArray.size() > 0) {

                CabDetail cabDetails = cabDetailArray.get(0);
                mTvCarHeader.setText(cabDetails.getCartype().toUpperCase());
                carName = cabDetails.getCartype().toUpperCase();
                mTvCardescription.setText(cabDetails.getDescription());


                // JSONArray   userDetail= new JSONArray(userDetails);
                if (dayNight.equals("day")) {
                    carRate = cabDetails.getCarRate();
                    fromIntailRate = cabDetails.getFromintailrate();
                    if (cabDetails.getRideTimeRate() != null) {
                        rideTimeRate = cabDetails.getRideTimeRate();
                    }
                } else if (dayNight.equals("night")) {
                    carRate = cabDetails.getNightIntailrate();
                    fromIntailRate = cabDetails.getNightFromintailrate();
                    if (cabDetails.getNightRideTimeRate() != null && !cabDetails.getNightRideTimeRate().equals("0")) {
                        rideTimeRate = cabDetails.getNightRideTimeRate();
                    }
                }
                mTvFirstPrice.setText(carRate);
                FirstKm = Float.parseFloat(cabDetails.getIntialkm());
                mTvFirstKm.setText(getResources().getString(R.string.first) + " " + FirstKm + " " + getResources().getString(R.string.km));
                mTvSecondPrice.setText(fromIntailRate + "/" + getResources().getString(R.string.km));
                mTvSecKm.setText(getResources().getString(R.string.after) + " " + FirstKm + " " + getResources().getString(R.string.km));

                if (cabDetails.getRideTimeRate() != null || cabDetails.getNightRideTimeRate() != null && !cabDetails.getNightRideTimeRate().equals("0")) {
                    mRlThree.setVisibility(View.VISIBLE);
                    mTvThdPrice.setText(rideTimeRate + "/" + getResources().getString(R.string.min));
                } else {
                    mRlThree.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.MATCH_PARENT);
                    params.weight = 1.5f;
                    mRlOne.setLayoutParams(params);
                    mRlTwo.setLayoutParams(params);
                }

                truckIcon = cabDetails.getIcon();
                truckType = cabDetails.getCartype();
                cabId = cabDetails.getId();
                areaId = cabDetails.getAreaId();
                transferType = cabDetails.getTransfertype();

                cabDetailAdapter = new CabDetailAdapter(HomeActivity.this, cabDetailArray);
                mRecycleCabDetail.setAdapter(cabDetailAdapter);
                cabDetailAdapter.setOnCabDetailItemClickListener(HomeActivity.this);
                cabDetailAdapter.updateItems();

                list = new ArrayList<String>();
                for (int si = 0; si < Integer.parseInt(cabDetails.getSeatCapacity()); si++) {
                    int seat = si + 1;
                    list.add(seat + "");
                }
                dataAdapter = new ArrayAdapter<String>(HomeActivity.this,
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mspinnerPerson.setAdapter(dataAdapter);

                Log.d("Fix Price", "Fix Price = " + cabDetails.getFixPrice());

            }

        }

    }


    private class GeocoderHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            if (locationAddress != null) {
                if (locationAddress.equals("Unable connect to Geocoder")) {
                    Toast.makeText(HomeActivity.this, getString(R.string.no_network_connection), Toast.LENGTH_LONG).show();
                } else {
                    Log.d("locationAddress", "locationAddress = " + locationAddress + "==" + bothLocationString);
                    if (bothLocationString.equals("pickeup") && mEtPickUpLocation != null) {


                        mEtPickUpLocation.setText(locationAddress);
                        metDropLocation.requestFocus();


                    } else if (bothLocationString.equals("drop") && metDropLocation != null)


                        metDropLocation.setText(locationAddress);
                    SharedPreferences.Editor dropLocation = userPref.edit();
                    Log.d("metDropLocation", "................................................");
                    Log.d("metDropLocation", metDropLocation.getText().toString());
                    dropLocation.putString("metDropLocation", metDropLocation.getText().toString());
                    dropLocation.commit();
                 /*   SharedPreferences.Editor dropLocation = userPref.edit();
                    dropLocation.putString("metDropLocation", locationAddress);
                    dropLocation.commit();*/


                }
            } else {
                nowDialog.cancel();
                mRlLayoutReservation.setVisibility(View.VISIBLE);
                // Toast.makeText(HomeActivity.this, getResources().getString(R.string.location_long), Toast.LENGTH_LONG).show();

            }
        }
    }

    private class GeocoderHandlerLatitude extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;


                default:
                    locationAddress = null;
            }
            Log.d("locationAddress", "locationAddress = " + locationAddress);
            if (locationAddress != null) {

                if (locationAddress.equals("Unable connect to Geocoder")) {
                    Toast.makeText(HomeActivity.this, getString(R.string.no_network_connection), Toast.LENGTH_LONG).show();
                } else {
                    String[] LocationSplit = locationAddress.split("\\,");
                    Log.d("locationAddress", "locationAddress = " + locationAddress + "==" + Double.parseDouble(LocationSplit[0]) + "==" + Double.parseDouble(LocationSplit[1]));
                    if (bothLocationString.equals("pickeup")) {
                        pickupLatitude = Double.parseDouble(LocationSplit[0]);
                        pickupLongtude = Double.parseDouble(LocationSplit[1]);
                        mPickupLarLng = new LatLng(Double.parseDouble(LocationSplit[0]), Double.parseDouble(LocationSplit[1]));
                    } else if (bothLocationString.equals("drop")) {
                        dropLongitude = Double.parseDouble(LocationSplit[1]);
                        dropLatitude = Double.parseDouble(LocationSplit[0]);

                        mDropLarLng = new LatLng(Double.parseDouble(LocationSplit[0]), Double.parseDouble(LocationSplit[1]));
                        if (metDropLocation.getText().length() > 0 && mEtPickUpLocation.getText().length() > 0) {
                            if (checkReady() && Common.isNetworkAvailable(HomeActivity.this)) {
                                new CaculationDiration().execute();
                                //   CaculationDirationIon();
                                calculationPresenterimp.calculateHitSetParameter(dropLongitude, dropLatitude, pickupLongtude, pickupLatitude);
                            } else {
                                Common.showInternetInfo(HomeActivity.this, "");
                            }
                        }
                    }

/*
                    if (metDropLocation.getText().length() > 0 && mEtPickUpLocation.getText().length() > 0) {
                        if (checkReady() && Common.isNetworkAvailable(HomeActivity.this)) {

                            //      PickupFixRateCall();

                        } else {
                            Common.showInternetInfo(HomeActivity.this, "");
                        }
                    } else {
                        MarkerAdd();
                    }*/
                }
            } else {
                Toast.makeText(HomeActivity.this, getString(R.string.notvaild_address), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        common.SlideMenuDesign(slidingMenu, HomeActivity.this, "home");
    }

    /*Add marker function*/
    public void MarkerAdd() {

        if (checkReady()) {

            if (marker != null)
                googleMap.clear();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            if (mPickupLarLng != null) {
                marker = new MarkerOptions()
                        .position(mPickupLarLng)
                        .title(getString(R.string.pick_up_location_text))

                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_location_icon));
                pickupMarker = googleMap.addMarker(marker);
                pickupMarker.setDraggable(true);
                builder.include(marker.getPosition());


            }

            if (mDropLarLng != null) {
                marker = new MarkerOptions()
                        .position(mDropLarLng)
                        .title("Drop Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location_icon));

                dropMarker = googleMap.addMarker(marker);
                dropMarker.setDraggable(true);
                builder.include(marker.getPosition());
            }

            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon))

            if (mDropLarLng != null || mPickupLarLng != null) {
                LatLngBounds bounds = builder.build();

                //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                Log.d("areBoundsTooSmall", "areBoundsTooSmall = " + areBoundsTooSmall(bounds, 300));
                if (areBoundsTooSmall(bounds, 300)) {
                    //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10));
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10);
                    googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {

                        @Override
                        public void onFinish() {
                            if (pickupMarker != null)
                                BounceAnimationMarker(pickupMarker, mPickupLarLng);
                            if (dropMarker != null)
                                BounceAnimationMarker(dropMarker, mDropLarLng);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });


                } else {

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {

                        @Override
                        public void onFinish() {
                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -1.0);
                            googleMap.animateCamera(zout);
                            BounceAnimationMarker(pickupMarker, mPickupLarLng);
                            BounceAnimationMarker(dropMarker, mDropLarLng);
                        }

                        @Override
                        public void onCancel() {
//                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -1.0);
//                            googleMap.animateCamera(zout);
                        }
                    });

                }
            }


            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker pickMarker) {

                    Log.d("bothLocationString", "bothLocationString pickup= " + bothLocationString + "==" + marker.getTitle() + "==" + mEtPickUpLocation.getText().toString());
                    if (marker.getTitle().equals("Pick Up Location"))
                        bothLocationString = "pickeup";
                    else if (marker.getTitle().equals("Drop Location"))
                        bothLocationString = "drop";
                    Log.d("bothLocationString", "bothLocationString pickup= " + bothLocationString + "==" + marker.getTitle() + "==" + mEtPickUpLocation.getText().toString());
                    Log.d("bothLocationString", "bothLocationString drop= " + bothLocationString + "==" + marker.getTitle() + "==" + metDropLocation.getText().toString());

                    return false;
                }
            });


            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    if (marker.getTitle().equals("Pick Up Location"))
                        bothLocationString = "pickeup";
                    else if (marker.getTitle().equals("Drop Location"))
                        bothLocationString = "drop";
                    Log.d("bothLocationString", "bothLocationString pickup= " + bothLocationString + "==" + marker.getTitle() + "==" + mEtPickUpLocation.getText().toString());
                    Log.d("bothLocationString", "bothLocationString drop= " + bothLocationString + "==" + marker.getTitle() + "==" + metDropLocation.getText().toString());
                    Log.d("latitude", "latitude one = " + marker.getPosition().latitude);
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    Log.d("latitude", "latitude two= " + marker.getPosition().latitude);
                }

                @Override
                public void onMarkerDragEnd(Marker mrk) {

                    Log.d("latitude", "latitude three = " + mrk.getPosition().latitude + "==" + mrk.getPosition().longitude);
                    if (Common.isNetworkAvailable(HomeActivity.this)) {
                        ClickOkButton = true;
                        LocationAddress locationAddress = new LocationAddress();
                        locationAddress.getAddressFromLocation(mrk.getPosition().latitude, mrk.getPosition().longitude,
                                getApplicationContext(), new GeocoderHandler());

                        Log.d("bothLocationString", "bothLocationString = " + bothLocationString);

                    } else {
                        Toast.makeText(HomeActivity.this, getString(R.string.no_network_connection), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

    public void BounceAnimationMarker(final Marker animationMarker, final LatLng animationLatLng) {
        if (animationLatLng != null) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = googleMap.getProjection();
            Point startPoint = proj.toScreenLocation(animationLatLng);
            startPoint.offset(0, -100);
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 1500;
            final Interpolator interpolator = new BounceInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * animationLatLng.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * animationLatLng.latitude + (1 - t) * startLatLng.latitude;
                    animationMarker.setPosition(new LatLng(lat, lng));
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }
    }

    private boolean areBoundsTooSmall(LatLngBounds bounds, int minDistanceInMeter) {
        float[] result = new float[1];
        Location.distanceBetween(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude, result);
        return result[0] < minDistanceInMeter;
    }

    public void EditorActionListener(final EditText locationEditext, final String clickText) {

        locationEditext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Log.d("Edit text", "Edit text = " + v.getText().toString());

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    Log.d("locationEditext", "locationEditext = " + locationEditext.getText().toString());
                    if (locationEditext.getText().toString().length() > 0) {

                        if (clickText.equals("pickeup")) {
                            if (Common.isNetworkAvailable(HomeActivity.this)) {
                                bothLocationString = "pickeup";
                                LocationAddress.getAddressFromLocation(mEtPickUpLocation.getText().toString(), getApplicationContext(), new GeocoderHandlerLatitude());
                            } else {
                                Toast.makeText(HomeActivity.this, "No Network", Toast.LENGTH_LONG).show();
                            }
                        } else if (clickText.equals("drop")) {
                            if (Common.isNetworkAvailable(HomeActivity.this)) {
                                bothLocationString = "drop";
                                LocationAddress.getAddressFromLocation(metDropLocation.getText().toString(), getApplicationContext(), new GeocoderHandlerLatitude());

                                SharedPreferences.Editor dropLocation = userPref.edit();
                                dropLocation.putString("metDropLocation", metDropLocation.getText().toString());
                                dropLocation.commit();
                                //   WebService.getI
                                // nstance().).letsFindAddressDetail(metDropLocation.getText().toString(),HomeActivity.this);
                            } else {
                                Toast.makeText(HomeActivity.this, "No Network", Toast.LENGTH_LONG).show();
                            }
                        }
                        mRlPickupDragLocation.setVisibility(View.GONE);
//                        if (metDropLocation.getText().length() > 0 && mEtPickUpLocation.getText().length() > 0) {
//                            if (checkReady() && Common.isNetworkAvailable(HomeActivity.this)) {
//                                new CaculationDiration().execute();
//                                CaculationDirationIon();
//                            } else {
//                                Common.showInternetInfo(HomeActivity.this, "");
//                            }
//                        }

                    } else {
                        mPickupLarLng = null;
                        pickupLatitude = 0.0;
                        pickupLongtude = 0.0;
                        Toast.makeText(HomeActivity.this, "Please Enter Location", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
    }

    //drop
    public void AddTextChangeListener(final EditText locationEditext, final String clickText) {
        locationEditext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                /// LocationAutocompleate( locationEditext, clickText)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.d("clickText", "clickText = " + clickText);
                if (s.length() != 0) {

                    if (clickText.equals("drop")) {
                        // mIvdropClose.setVisibility(View.VISIBLE);
                    } else if (clickText.equals("pickeup")) {
                        //mIvPickUpClose.setVisibility(View.VISIBLE);
                    }
                    Log.d("ClickOkButton", "ClickOkButton = " + ClickOkButton);
                    if (!ClickOkButton) {
                        // mRlPickupDragLocation.setVisibility(View.VISIBLE);
                        Log.d("ClickOkButton", "ClickOkButton = " + s.toString());
                        //new getPickupDropAddress(s.toString()).execute();
                        //  getPickupDropAddressIon(s.toString());
                        WebService.getInstance().letsAutoCompleteAddress(s.toString(), pickupLatitude, pickupLongtude, HomeActivity.this);

                    }
                } else {
                    if (clickText.equals("drop")) {
                        //
                        // mIvdropClose.setVisibility(View.GONE);
                        mDropLarLng = null;
                        dropLongitude = 0.0;
                        dropLatitude = 0.0;
                    } else if (clickText.equals("pickeup")) {
                        mIvPickUpClose.setVisibility(View.GONE);
                        mPickupLarLng = null;
                        pickupLatitude = 0.0;
                        pickupLongtude = 0.0;
                    }
                    mRlPickupDragLocation.setVisibility(View.GONE);

                    MarkerAdd();
                }

            }
        });

    }

    public void AddSetOnClickListener(EditText locationEditext, final String ClickValue) {

        locationEditext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ClickOkButton = false;
                bothLocationString = ClickValue;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (ClickValue.equals("drop")) {
                    params.setMargins(0, (int) getResources().getDimension(R.dimen.height_175), 0, 0);
                } else if (ClickValue.equals("pickeup")) {
                    params.setMargins(0, (int) getResources().getDimension(R.dimen.height_130), 0, 0);
                }
                mRlPickupDragLocation.setLayoutParams(params);
                return false;
            }
        });

        locationEditext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickOkButton = false;
                bothLocationString = ClickValue;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (ClickValue.equals("drop")) {
                    params.setMargins(0, (int) getResources().getDimension(R.dimen.height_175), 0, 0);
                } else if (ClickValue.equals("pickeup")) {
                    params.setMargins(0, (int) getResources().getDimension(R.dimen.height_130), 0, 0);
                }
                mRlPickupDragLocation.setLayoutParams(params);
            }
        });
    }



    @Override
    public void PickupDropClick(int position) {

        if (locationArrayList != null && locationArrayList.size() > 0) {
            HashMap<String, String> picDrpHash = locationArrayList.get(position);
            Log.d("bothLocationString", "bothLocationString = " + bothLocationString);
            if (!bothLocationString.equals("")) {
                if (bothLocationString.equals("pickeup")) {
                    mEtPickUpLocation.setText(picDrpHash.get("location name"));
                    if (Common.isNetworkAvailable(HomeActivity.this)) {
                        Log.d("Location name", "Location name = " + mEtPickUpLocation.getText().toString());
                        bothLocationString = "pickeup";
                        LocationAddress.getAddressFromLocation(picDrpHash.get("location name"), getApplicationContext(), new GeocoderHandlerLatitude());
                    } else {
                        Toast.makeText(HomeActivity.this, "No Network", Toast.LENGTH_LONG).show();
                    }
                } else if (bothLocationString.equals("drop")) {
                    metDropLocation.setText(picDrpHash.get("location name"));
                    if (Common.isNetworkAvailable(HomeActivity.this)) {
                        Log.d("Location name", "Location name = " + mEtPickUpLocation.getText().toString());
                        bothLocationString = "drop";
                        LocationAddress.getAddressFromLocation(picDrpHash.get("location name"), getApplicationContext(), new GeocoderHandlerLatitude());


                    } else {
                        Toast.makeText(HomeActivity.this, "No Network", Toast.LENGTH_LONG).show();
                    }
                }


            }
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        mRlPickupDragLocation.setVisibility(View.GONE);
        //mRecyclePickupLocation.setVisibility(View.GONE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTvHome = null;
        mRlSlideMenu = null;
        mEtPickUpLocation = null;
        metDropLocation = null;
        mEtWriteComment = null;
        mRlLayoutNow = null;
        mRlLayoutReservation = null;
        gpsTracker = null;
        googleMap = null;
        cabDetailArray = null;
        marker = null;
        mPickupLarLng = null;
        mDropLarLng = null;
        arrayPoints = null;
        nowDialog = null;
        reservationDialog = null;
        cabDetailAdapter = null;
        mTvCarHeader = null;
        mTvCardescription = null;
        mTvFirstPrice = null;
        mTvFirstKm = null;
        mTvSecondPrice = null;
        mTvSecKm = null;
        mTvThdPrice = null;
        mRlOne = null;
        mRlTwo = null;
        mRlThree = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("requestCode", "requestCode = " + requestCode);
        if (requestCode == 3) {
            if (data != null) {
                String userUpd = data.getStringExtra("update_user_profile");
                Log.d("requestCode", "requestCode = " + userUpd);
                if (userUpd.equals("1")) {
                    common.SlideMenuDesign(slidingMenu, HomeActivity.this, "home");
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.d("paypal data", "paypal data = " + confirm);
                        Log.e("Show", "Show" + confirm.toJSONObject().toString(4));
                        Log.e("Show", "Show" + confirm.getPayment().toJSONObject().toString(4));
                        cashDialog.cancel();
                        nowDialog.show();
                        JSONObject conObj = new JSONObject(confirm.toJSONObject().toString(4));
                        JSONObject ResObj = new JSONObject(conObj.getString("response"));
                        transactionId = ResObj.getString("id");
                        Log.d("stripe_id", "stripe_id = " + transactionId);
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         */
                        Toast.makeText(getApplicationContext(), "PaymentConfirmation info received" +
                                " from PayPal", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "an extremely unlikely failure" +
                                " occurred:", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "The user canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(getApplicationContext(), "An invalid Payment or PayPalConfiguration" +
                        " was submitted. Please see the docs.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == Constants.REQUEST_CODE_STRIPE) {
            if (data != null) {
                transactionId = data.getStringExtra("stripe_id");
                Log.d("stripe_id", "stripe_id = " + transactionId);
                cashDialog.cancel();
                nowDialog.show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            HomeActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
    }
}
