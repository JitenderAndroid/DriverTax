package com.taxi.passenger.gpsLocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.apiclient.modelForGoogleApiMap.AddressFromGeocode;
import com.taxi.passenger.apiclient.modelForGoogleApiMap.PlaceSearchApiBean;
import com.taxi.passenger.interfaces.NetworkInterfaces;


public class LocationAddress {

    private static final String TAG = "LocationAddress";
    private SharedPreferences userPref;

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread(new UpdateLocationByLatLongThread(latitude, longitude, handler));
        thread.start();


    }


    public static void getAddressFromLocation(final String address,
                                              final Context context, final Handler handler)


    {

        Thread thread = new Thread(new UpdateLocationThread(address, handler));
        thread.start();
    }

    static class UpdateLocationThread implements Runnable, NetworkInterfaces.googleMapApiForPlaceSearch {

        String address;
        Context context;
        Handler handler;

        public UpdateLocationThread(String address, Handler handler) {
            this.address = address;
            this.context = context;
            this.handler = handler;

        }

        @Override
        public void run() {
            WebService.getInstance().letsFindAddressDetail(address, UpdateLocationThread.this);
        }

        @Override
        public void onSuccessPlaceSearch(PlaceSearchApiBean placeSearchApiBean) {
            String result = null;
            try {

                if (!placeSearchApiBean.getStatus().equals("ZERO_RESULTS")) {

                    if (placeSearchApiBean != null) {
                        StringBuilder sb = new StringBuilder();
                        placeSearchApiBean.getResults().get(0).getGeometry().getLocation().getLat();
                        sb.append(placeSearchApiBean.getResults().get(0).getGeometry().getLocation().getLat()).append(",");
                        sb.append(placeSearchApiBean.getResults().get(0).getGeometry().getLocation().getLng());
                        result = sb.toString();

                    }
                } else {
                    result = null;
                }

            } finally {
                Message message = Message.obtain();
                message.setTarget(handler);
                if (result != null) {
                    message.what = 1;
                    Bundle bundle = new Bundle();
//                        result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                "\n\nAddress:\n" + result;
                    bundle.putString("address", result);
                    message.setData(bundle);
                } else {
                    message.what = 1;
                    Bundle bundle = new Bundle();

                    bundle.putString("address", result);
                    message.setData(bundle);
                }
                message.sendToTarget();
            }

        }

        @Override
        public void onFailurePlaceSearch() {

            Message message = Message.obtain();
            message.setTarget(handler);
            String result = "some technical issue";
            //noinspection ConstantConditions
            if (result != null) {
                message.what = 1;
                Bundle bundle = new Bundle();
//                        result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                "\n\nAddress:\n" + result;
                bundle.putString("address", result);
                message.setData(bundle);
            } else {
                message.what = 2;
                Bundle bundle = new Bundle();

                bundle.putString("address", result);
                message.setData(bundle);
            }
            message.sendToTarget();

        }
    }

    static class UpdateLocationByLatLongThread implements Runnable, NetworkInterfaces.GoogleApiMapResponce {
        double latitude;
        double longitude;
        Handler handler;


        public UpdateLocationByLatLongThread(double latitude, double longitude, Handler handler) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.handler = handler;

        }

        @Override
        public void run() {
            String ch = "noida";

            WebService.getInstance().letsDoLanLongGeocode(latitude, longitude, ch, UpdateLocationByLatLongThread.this);

        }

        @Override
        public void onGeocodeSuccess(AddressFromGeocode addressFromGeocode) {


            String result = null;
            try {

                if (!addressFromGeocode.getStatus().equals("ZERO_RESULTS")) {

                    if (addressFromGeocode != null) {


                        StringBuilder sb = new StringBuilder();
                        sb.append(addressFromGeocode.getResults().get(0).getFormattedAddress());


                        result = sb.toString();


                    }
                } else {
                    result = null;
                }

            } finally {
                Message message = Message.obtain();
                message.setTarget(handler);
                if (result != null) {
                    message.what = 1;
                    Bundle bundle = new Bundle();
//                        result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                "\n\nAddress:\n" + result;
                    bundle.putString("address", result);
                    message.setData(bundle);
                } else {
                    message.what = 1;
                    Bundle bundle = new Bundle();

                    bundle.putString("address", result);
                    message.setData(bundle);
                }
                message.sendToTarget();
            }
        }


        @Override
        public void onGeocodeFailure() {
            Message message = Message.obtain();
            message.setTarget(handler);
            String result = "some technical issue";
            //noinspection ConstantConditions
            if (result != null) {
                message.what = 1;
                Bundle bundle = new Bundle();
//                        result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                "\n\nAddress:\n" + result;
                bundle.putString("address", result);
                message.setData(bundle);
            } else {
                message.what = 2;
                Bundle bundle = new Bundle();
                   /*   result = "Latitude: " + latitude + " Longitude: " + longitude +
                               "\n Unable to get address for this lat-long.";*/
                bundle.putString("address", result);
                message.setData(bundle);
            }
            message.sendToTarget();


        }
    }
}

