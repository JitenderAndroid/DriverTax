package com.taxi.passenger.TripsMvp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.taxi.passenger.R;

import com.taxi.passenger.TripsMvp.Model.Bookingdetail;
import com.taxi.passenger.TripsMvp.Model.TripBookBean;
import com.taxi.passenger.TripsMvp.Presenter.TripPresenterImp;
import com.taxi.passenger.TripsMvp.View.TripView;
import com.taxi.passenger.activity.AllTripActivity;
import com.taxi.passenger.utils.Url;

public class TripDetailActivity extends AppCompatActivity  implements TripView {

    TextView txt_car_name;
    TextView txt_pickup_point;
    TextView txt_pickup_point_val;
    TextView txt_drop_point;
    TextView txt_drop_point_val;
    TextView txt_truct_type;
    TextView txt_truct_type_val;
    ImageView img_car_image;
    TextView txt_distance;
    TextView txt_distance_val;
    TextView txt_distance_km;
    TextView txt_ast_time;
    TextView txt_ast_time_val;
    TextView txt_booking_date;
    TextView txt_booking_date_val;
    RelativeLayout layout_back_arrow;
    TextView txt_total_price;
    TextView txt_total_price_dol;
    TextView txt_total_price_val, txt_to;
    RelativeLayout layout_confirm_request;
    TextView txt_payment_type_val;
    TextView txt_payment_type, txt_vehicle_detail, txt_payment_detail, txt_confirm_request;
    Bookingdetail bookingdetail;
    Typeface OpenSans_Regular, Roboto_Regular, Roboto_Medium, Roboto_Bold, OpenSans_Semibold;

    String pickup_point;
    String drop_point;
    String truckIcon;
    String truckType;
    String CabId;
    String AreaId;
    Float distance;
    Float totlePrice;
    String booking_date;
    double PickupLatitude;
    double PickupLongtude;
    double DropLatitude;
    double DropLongtude;
    String DayNight;
    String comment;
    String pickup_date_time;
    String transfertype;
    String PaymentType;
    String person;
    String transaction_id;
    String BookingType;
    String ApproxTime;
    TripPresenterImp tripPresenterImp;
    SharedPreferences userPref;

    Dialog ProgressDialog;
    RotateLoading cusRotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        initViews();

        ProgressDialog = new Dialog(TripDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = ProgressDialog.findViewById(R.id.rotateloading_register);

        userPref = PreferenceManager.getDefaultSharedPreferences(TripDetailActivity.this);
        tripPresenterImp= new TripPresenterImp(this);
        pickup_point = getIntent().getExtras().getString("pickup_point");
        drop_point = getIntent().getExtras().getString("drop_point");
        truckIcon = getIntent().getExtras().getString("truckIcon");
        truckType = getIntent().getExtras().getString("truckType");
        CabId = getIntent().getExtras().getString("cabId");
        AreaId = getIntent().getExtras().getString("areaId");
        distance = getIntent().getExtras().getFloat("distance");
        totlePrice = getIntent().getExtras().getFloat("totlePrice");
        booking_date = getIntent().getExtras().getString("booking_date");
        PickupLatitude = getIntent().getExtras().getDouble("pickupLatitude");
        PickupLongtude = getIntent().getExtras().getDouble("pickupLongtude");
        DropLatitude = getIntent().getExtras().getDouble("dropLatitude");
        DropLongtude = getIntent().getExtras().getDouble("dropLongitude");
        comment = getIntent().getExtras().getString("comment");
        DayNight = getIntent().getExtras().getString("dayNight");
        transfertype = getIntent().getExtras().getString("transferType");
        PaymentType = getIntent().getExtras().getString("PaymentType");
        person = getIntent().getExtras().getString("person");
        transaction_id = getIntent().getExtras().getString("transactionId");
        BookingType = getIntent().getExtras().getString("bookingType");
        ApproxTime = getIntent().getExtras().getString("astTime");

        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        Roboto_Regular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Roboto_Medium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold_0.ttf");

        txt_car_name.setTypeface(OpenSans_Regular);

        txt_pickup_point.setTypeface(Roboto_Regular);
        txt_booking_date.setTypeface(Roboto_Regular);
        txt_drop_point.setTypeface(Roboto_Regular);
        txt_truct_type.setTypeface(Roboto_Regular);
        txt_distance_km.setTypeface(Roboto_Regular);
        txt_total_price_dol.setTypeface(Roboto_Regular);
       // txt_total_price_dol.setText(Common.Currency);
        txt_payment_type.setTypeface(Roboto_Regular);
        txt_to.setTypeface(Roboto_Bold);
        txt_vehicle_detail.setTypeface(Roboto_Bold);
        txt_payment_detail.setTypeface(Roboto_Bold);
        txt_confirm_request.setTypeface(Roboto_Bold);

        txt_pickup_point_val.setTypeface(OpenSans_Regular);
        txt_pickup_point_val.setText(pickup_point);
        txt_booking_date_val.setTypeface(OpenSans_Regular);
        txt_booking_date_val.setText(booking_date);
        txt_drop_point_val.setTypeface(OpenSans_Regular);
        txt_drop_point_val.setText(drop_point);
        txt_truct_type_val.setTypeface(OpenSans_Regular);
        txt_truct_type_val.setText(truckType.toUpperCase());
        txt_distance.setTypeface(OpenSans_Regular);
        txt_distance_val.setTypeface(OpenSans_Regular);
        txt_distance_val.setText(distance + "");
        txt_ast_time.setTypeface(OpenSans_Regular);
        txt_ast_time_val.setTypeface(OpenSans_Regular);
        txt_ast_time_val.setText(ApproxTime);
        txt_total_price.setTypeface(OpenSans_Regular);
        txt_total_price_val.setTypeface(OpenSans_Regular);
        txt_total_price_val.setText(Math.round(totlePrice) + "");
        txt_payment_type_val.setTypeface(OpenSans_Regular);
        txt_payment_type_val.setText(PaymentType);

        Log.d("truckIcon", "truckIcon = " + truckIcon);
        Picasso.with(TripDetailActivity.this)
                .load(Uri.parse(Url.CAR_IMAGE_URL + truckIcon))
                .placeholder(R.drawable.truck_icon)
                .into(img_car_image);

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent in= new Intent( TripDetailActivity.this,BookingDetailActivity.class);
              //  startActivity(in);
                finish();
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a, d, MMM yyyy,EEE");
        try {
            Date parceDate = simpleDateFormat.parse(booking_date);
            SimpleDateFormat parceDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            pickup_date_time = parceDateFormat.format(parceDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        layout_confirm_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* ProgressDialog.show();
                cusRotateLoading.start();*/


//
//                RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),  userPref.getString("id", ""));
//                RequestBody username = RequestBody.create(MediaType.parse("text/plain"),  userPref.getString("username", ""));
//                RequestBody pickup_date_time1 = RequestBody.create(MediaType.parse("text/plain"), pickup_date_time);
//                RequestBody drop_area = RequestBody.create(MediaType.parse("text/plain"),  drop_point);
//                RequestBody pickup_area = RequestBody.create(MediaType.parse("text/plain"),  pickup_point);
//                RequestBody time_type = RequestBody.create(MediaType.parse("text/plain"), DayNight);
//                RequestBody amount = RequestBody.create(MediaType.parse("text/plain"),  String.valueOf(totlePrice));
//                RequestBody km = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(distance));
//                RequestBody pickup_lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(PickupLatitude));
//                RequestBody pickup_longs = RequestBody.create(MediaType.parse("text/plain"),   String.valueOf(PickupLongtude));
//                RequestBody drop_lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(DropLatitude));
//                RequestBody drop_longs = RequestBody.create(MediaType.parse("text/plain"),   String.valueOf(DropLongtude));
//                RequestBody isdevice = RequestBody.create(MediaType.parse("text/plain"),  "1");
//                RequestBody flag = RequestBody.create(MediaType.parse("text/plain"),  "0");
//                RequestBody taxi_type = RequestBody.create(MediaType.parse("text/plain"), truckType);
//                RequestBody taxi_id = RequestBody.create(MediaType.parse("text/plain"), CabId);
//                RequestBody purpose = RequestBody.create(MediaType.parse("text/plain"), transfertype);
//                RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), getIntent().getExtras().getString("comment"));
//                RequestBody person = RequestBody.create(MediaType.parse("text/plain"), getIntent().getExtras().getString("person"));
//                RequestBody payment_type = RequestBody.create(MediaType.parse("text/plain"), PaymentType);
//                RequestBody book_create_date_time = RequestBody.create(MediaType.parse("text/plain"),  BookingType);
//                RequestBody transactionId = RequestBody.create(MediaType.parse("text/plain"), transaction_id);
//                RequestBody approx_time = RequestBody.create(MediaType.parse("text/plain"), ApproxTime);



                bookingdetail= new Bookingdetail();

                bookingdetail.setUserId(userPref.getString("id", ""));
                bookingdetail.setUsername(userPref.getString("username", ""));
                bookingdetail.setPickupDateTime(pickup_date_time);
                bookingdetail.setDropArea(drop_point);
                bookingdetail.setTimetype(DayNight);
                bookingdetail.setAmount(String.valueOf(totlePrice));
                bookingdetail.setKm( String.valueOf(distance));
                bookingdetail.setPickupLat(String.valueOf(PickupLatitude));
                bookingdetail.setPickupLong( String.valueOf(PickupLongtude));
                bookingdetail.setDropLat( String.valueOf(DropLatitude));
                bookingdetail.setDropLong( String.valueOf(DropLongtude));
                bookingdetail.setIsdevice("1");
                bookingdetail.setFlag("0");
                bookingdetail.setTaxiType(truckType);
                bookingdetail.setTaxiId(CabId);
                bookingdetail.setPurpose(transfertype);
                bookingdetail.setComment(comment);
                bookingdetail.setPerson(person);
                bookingdetail.setPaymentType(PaymentType);
                bookingdetail.setBookCreateDateTime(BookingType);
                bookingdetail.setTransactionId("0");
                bookingdetail.setApproxTime(ApproxTime);
                bookingdetail.setPickupArea(pickup_point);




                tripPresenterImp.toSetdataForGetResponce(bookingdetail);


     /*           Ion.with(TripDetailActivity.this)
                        .load(Url.BOOK_CAB_URL)
                        .setTimeout(10000)
                        //.setJsonObjectBody(json)
                        .setMultipartParameter("user_id", userPref.getString("id", ""))
                        .setMultipartParameter("username", userPref.getString("username", ""))
                        .setMultipartParameter("pickup_date_time", pickup_date_time)
                        .setMultipartParameter("drop_area", drop_point)
                        .setMultipartParameter("pickup_area", pickup_point)
                        .setMultipartParameter("time_type", DayNight)
                        .setMultipartParameter("amount", String.valueOf(totlePrice))
                        .setMultipartParameter("km", String.valueOf(distance))
                        .setMultipartParameter("pickup_lat", String.valueOf(PickupLatitude))
                        .setMultipartParameter("pickup_longs", String.valueOf(PickupLongtude))
                        .setMultipartParameter("drop_lat", String.valueOf(DropLatitude))
                        .setMultipartParameter("drop_longs", String.valueOf(DropLongtude))
                        .setMultipartParameter("isdevice", "1")
                        .setMultipartParameter("flag", "0")
                        .setMultipartParameter("taxi_type", truckType)
                        .setMultipartParameter("taxi_id", CabId)
                        .setMultipartParameter("purpose", transfertype)
                        .setMultipartParameter("comment", comment)
                        .setMultipartParameter("person", person)
                        .setMultipartParameter("payment_type", PaymentType)
                        .setMultipartParameter("book_create_date_time", BookingType)
                        .setMultipartParameter("transactionId", transaction_id)
                        .setMultipartParameter("approx_time",ApproxTime)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception error, JsonObject result) {
                                // do stuff with the result or error
                                Log.d("Booking result", "Booking result = " + result + "==" + error);
                                ProgressDialog.cancel();
                                cusRotateLoading.stop();
                                if (error == null) {


                                    try {
                                        JSONObject resObj = new JSONObject(result.toString());
                                        Log.d("Booking result", "Booking result = " + resObj);
                                        if (resObj.getString("status").equals("success")) {
                                            //Common.showMkSucess(TripDetailActivity.this, resObj.getString("message").toString(), "yes");
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent ai = new Intent(TripDetailActivity.this, AllTripActivity.class);
                                                    startActivity(ai);
                                                    finish();
                                                }
                                            }, 500);
                                        } else if (resObj.getString("status").equals("false")) {
*//*
                                            Common.user_InActive = 1;
                                            Common.InActive_msg = resObj.getString("message");*//*

                                            SharedPreferences.Editor editor = userPref.edit();
                                            editor.clear();
                                            editor.commit();

                                            Intent logInt = new Intent(TripDetailActivity.this, MainActivity.class);
                                            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            logInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(logInt);
                                        } else {
                                            Common.showMkError(TripDetailActivity.this, resObj.getString("error code").toString());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Common.ShowHttpErrorMessage(TripDetailActivity.this, error.getMessage());
                                }
                            }
                        });
*/

//                Log.d("confirmParam", "confirmParam = " + Url.BOOK_CAB_URL + "?" + confirmParam);
//                //ConfirmClient.get("",)
//                ConfirmClient.get(Url.BOOK_CAB_URL, confirmParam, new AsyncHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        Log.d("responseBody", "responseBody" + new String(responseBody));
//                        loader.cancel();
//                        cusRotateLoading.stop();
//
//
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        loader.cancel();
//                        cusRotateLoading.stop();
//                        Common.ShowHttpErrorMessage(TripDetailActivity.this, error.getMessage());
//                    }
//                });
            }
        });

    }

    private void initViews() {
        txt_car_name =  findViewById(R.id.txt_car_name);
        txt_pickup_point =  findViewById(R.id.txt_pickup_point);
        txt_pickup_point_val =  findViewById(R.id.txt_pickup_point_val);
        txt_drop_point =  findViewById(R.id.txt_drop_point);
        txt_drop_point_val =  findViewById(R.id.txt_drop_point_val);
        txt_truct_type =  findViewById(R.id.txt_truct_type);
        txt_truct_type_val =  findViewById(R.id.txt_truct_type_val);
        img_car_image = (ImageView) findViewById(R.id.img_car_image);
        txt_distance =  findViewById(R.id.txt_distance);
        txt_distance_val =  findViewById(R.id.txt_distance_val);
        txt_distance_km =  findViewById(R.id.txt_distance_km);
        txt_ast_time =  findViewById(R.id.txt_ast_time);
        txt_ast_time_val =  findViewById(R.id.txt_ast_time_val);
        txt_booking_date =  findViewById(R.id.txt_booking_date);
        txt_booking_date_val =  findViewById(R.id.txt_booking_date_val);
        layout_back_arrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);
        txt_total_price =  findViewById(R.id.txt_total_price);
        txt_total_price_dol =  findViewById(R.id.txt_total_price_dol);
        txt_total_price_val =  findViewById(R.id.txt_total_price_val);
        layout_confirm_request = (RelativeLayout) findViewById(R.id.layout_confirm_request);
        txt_payment_type_val =  findViewById(R.id.txt_payment_type_val);
        txt_payment_type =  findViewById(R.id.txt_payment_type);
        txt_to =  findViewById(R.id.txt_to);
        txt_vehicle_detail =  findViewById(R.id.txt_vehicle_detail);
        txt_payment_detail =  findViewById(R.id.txt_payment_detail);
        txt_confirm_request =  findViewById(R.id.txt_confirm_request);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        txt_car_name = null;
        txt_pickup_point = null;
        txt_pickup_point_val = null;
        txt_drop_point = null;
        txt_drop_point_val = null;
        txt_truct_type = null;
        txt_truct_type_val = null;
        img_car_image = null;
        txt_distance = null;
        txt_distance_val = null;
        txt_distance_km = null;
        txt_booking_date = null;
        txt_booking_date_val = null;
        layout_back_arrow = null;
        txt_total_price = null;
        txt_total_price_dol = null;
        txt_total_price_val = null;
        layout_confirm_request = null;

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
    public void onResponce(TripBookBean mtripBookBean) {
        if( mtripBookBean.getStatus().equals("failed")){
            AlertDialog.Builder dialog_detect= new AlertDialog.Builder(TripDetailActivity.this);
            dialog_detect.setTitle("Sorry");
            dialog_detect.setMessage(mtripBookBean.getMessage());
            dialog_detect.show();

        }
          else {
            if (mtripBookBean.getStatus().equals("success")) {
                Intent ai = new Intent(TripDetailActivity.this, AllTripActivity.class);
                startActivity(ai);
                finish();
            }

        }


    }
}
