package com.taxi.passenger.apiclient;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.facebook.all.All;
import com.taxi.passenger.AllTripFeedMvp.Model.AllTrips;
import com.taxi.passenger.CalculateDistanceMvp.Model.CalculateDistancebean;
import com.taxi.passenger.ChangePasswordMvp.Model.ChangePasswordModel;
import com.taxi.passenger.ChangePasswordMvp.Model.ChangePasswordRequestModel;
import com.taxi.passenger.DeleteCabMvp.Model.DeleteCabBean;
import com.taxi.passenger.FilterAllTripMvp.Model.FilterTripBean;
import com.taxi.passenger.RatingMVP.Model.RatingModel;
import com.taxi.passenger.TripsMvp.Model.Bookingdetail;
import com.taxi.passenger.TripsMvp.Model.TripBookBean;

import com.taxi.passenger.apiclient.modelForGoogleApiMap.AddressFromGeocode;
import com.taxi.passenger.apiclient.modelForGoogleApiMap.PlaceSearchApiBean;
import com.taxi.passenger.interfaces.NetworkInterfaces;
import com.taxi.passenger.model.response.AccepetedRideDriverDetail;
import com.taxi.passenger.ForgotPasswordMvp.Model.ForgotPasswordModel;
import com.taxi.passenger.model.response.AutoCompleteAddress;
import com.taxi.passenger.model.response.LogoutApiModel;
import com.taxi.passenger.SignUpMvp.Model.RegisterDetailModel;
import com.taxi.passenger.model.response.ProfileEditModel;
import com.taxi.passenger.model.response.UserLoginDetailModel;
import com.taxi.passenger.utils.AllTripFeed;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public final class WebService {
    private static APIInterface apiInterface;
    private static WebService mWebservice;
    private Call<AutoCompleteAddress> call;

    public WebService() {
        if (apiInterface == null) {
            apiInterface = APIClient.getClient().create(APIInterface.class);
        }
    }

    public static WebService getInstance() {
        if (mWebservice == null) {
            mWebservice = new WebService();
        }
        return mWebservice;
    }

    public void callLoginService(String token, String userName, String password, final NetworkInterfaces.LoginWebServiceInterface loginResponseInterface) {
        apiInterface.callLoginWebservice(token, userName, password).enqueue(new Callback<UserLoginDetailModel>() {
            @Override
            public void onResponse(Call<UserLoginDetailModel> call, Response<UserLoginDetailModel> response) {
                UserLoginDetailModel driverLoginDetail = response.body();
                String status = driverLoginDetail.getStatus();
                String errorcode = driverLoginDetail.getErrorcode();

                Log.d("WebServices", "" + driverLoginDetail);
                //loginResponseInterface.onFailure();
                loginResponseInterface.onSuccess(status, errorcode, driverLoginDetail);
            }

            @Override
            public void onFailure(Call<UserLoginDetailModel> call, Throwable t) {
                // loginResponseInterface.onSuccess("","", driverLoginDetail);
                loginResponseInterface.onFailure(t);
            }
        });

    }


    public void callRegisterWebService(RequestBody name, RequestBody email, RequestBody username, RequestBody mobile, RequestBody password, RequestBody isdevice, RequestBody token, RequestBody facebook_id, RequestBody twitter_id, RequestBody dob, RequestBody gender, RequestBody fileBody, final NetworkInterfaces.RegisterWebServiceInterface registerResponseInterface) {
        apiInterface.callRegisterWebservice(name, username, email, password, dob, gender, mobile, isdevice, fileBody, token, facebook_id, twitter_id).enqueue(new Callback<RegisterDetailModel>() {
            @Override
            public void onResponse(Call<RegisterDetailModel> call, Response<RegisterDetailModel> response) {
                RegisterDetailModel registerDetailModel = response.body();
                registerResponseInterface.onSuccess(registerDetailModel);
            }

            @Override
            public void onFailure(Call<RegisterDetailModel> call, Throwable t) {
                registerResponseInterface.onFailure();
            }
        });

    }

    public void callForgotWebService(String email, final NetworkInterfaces.ForgotPasswordInterface forgotPasswordInterface) {

        apiInterface.callForgotPasswordWebService(email).enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                ForgotPasswordModel forgotPasswordModel = response.body();
                forgotPasswordInterface.onSuccess(forgotPasswordModel);
            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                forgotPasswordInterface.onFailure();
            }
        });
    }


    public void callRatingWebservice(String rating, int driverId, final NetworkInterfaces.RatingInterface ratingInterface) {
        apiInterface.callRatingWebservice(rating, driverId).enqueue(new Callback<RatingModel>() {
            @Override
            public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                RatingModel ratingModel = response.body();
                ratingInterface.onSuccess(ratingModel);
            }

            @Override
            public void onFailure(Call<RatingModel> call, Throwable t) {
                ratingInterface.onFailure();
            }
        });
    }


    public void callAcceptedRideDriverDetail(int user_id, final NetworkInterfaces.AcceptedRideDriverDetailedInterfaces acceptedRideDriverDetailedInterfaces) {
        apiInterface.callAcceptedRideDriverDetail(user_id).enqueue(new Callback<AccepetedRideDriverDetail>() {
            @Override
            public void onResponse(Call<AccepetedRideDriverDetail> call, Response<AccepetedRideDriverDetail> response) {
                AccepetedRideDriverDetail accepetedRideDriverDetail = response.body();
                acceptedRideDriverDetailedInterfaces.onSuccess(accepetedRideDriverDetail);
            }

            @Override
            public void onFailure(Call<AccepetedRideDriverDetail> call, Throwable t) {
                acceptedRideDriverDetailedInterfaces.onFailure();
            }
        });
    }


    public void callLogOutUser(int user_id, final NetworkInterfaces.LogoutUserInterfaces logoutUserInterfaces) {
        apiInterface.callLogoutUser(user_id).enqueue(new Callback<LogoutApiModel>() {
            @Override
            public void onResponse(Call<LogoutApiModel> call, Response<LogoutApiModel> response) {
                LogoutApiModel logoutApiModel = response.body();
                logoutUserInterfaces.onSuccess(logoutApiModel);
            }

            @Override
            public void onFailure(Call<LogoutApiModel> call, Throwable t) {
                logoutUserInterfaces.onFailure();
            }
        });
    }


    public void callChangePassword(String password, int uid, final NetworkInterfaces.ChangePassword changePasswordInterfaces) {
        final ChangePasswordRequestModel model = new ChangePasswordRequestModel();
        model.setPassword(password);
        model.setUid(uid + "");
        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("uid", uid + "");
        apiInterface.callChangePassword(params).enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                ChangePasswordModel changePasswordModel = response.body();
                Log.e("onResponse", "" + response.message());
                changePasswordInterfaces.onSuccess(changePasswordModel);
            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                Log.e("onFailure", "" + t);
                changePasswordInterfaces.onFailure();
            }
        });


    }

    public void callTripBookBeanCall(final Bookingdetail bookingDetail, final NetworkInterfaces.BookTrip bookTripInterFaces) {


        Map<String, String> params = new HashMap<>();
        params.put("user_id", bookingDetail.getUserId());
        params.put("username", bookingDetail.getUsername());
        params.put("pickup_date_time", bookingDetail.getPickupDateTime());
        params.put("drop_area", bookingDetail.getDropArea());
        params.put("pickup_area", bookingDetail.getPickupArea());
        params.put("time_type", bookingDetail.getTimetype());
        params.put("amount", bookingDetail.getAmount());
        params.put("km", bookingDetail.getKm());
        params.put("pickup_lat", bookingDetail.getPickupLat());
        params.put("pickup_longs", bookingDetail.getPickupLong());
        params.put("drop_lat", bookingDetail.getDropLat());
        params.put("drop_longs", bookingDetail.getDropLong());
        params.put("isdevice", bookingDetail.getIsdevice());
        params.put("approx_time", bookingDetail.getApproxTime());
        params.put("flag", bookingDetail.getFlag());
        params.put("taxi_type", bookingDetail.getTaxiType());
        params.put("taxi_id", bookingDetail.getTaxiId());
        params.put("purpose", bookingDetail.getPurpose());
        params.put("comment", bookingDetail.getComment());
        params.put("person", bookingDetail.getPerson());
        params.put("payment_type", bookingDetail.getPaymentType());
        params.put("transaction_id", bookingDetail.getTransactionId());
        params.put("book_create_date_time", bookingDetail.getBookCreateDateTime());



        apiInterface.callTripBookBeanCall(params).enqueue(new Callback<TripBookBean>() {
            @Override
            public void onResponse(Call<TripBookBean> call, Response<TripBookBean> response) {
                if (response.body()!= null) {
                    TripBookBean tripBookBean = response.body();
                    bookTripInterFaces.onSuccess(tripBookBean);
                }
                else{    TripBookBean tripBookBean = null;tripBookBean.setStatus("success");

                    bookTripInterFaces.onSuccess(tripBookBean);
                }
            }

            @Override
            public void onFailure(Call<TripBookBean> call, Throwable t) {
                bookTripInterFaces.onFailure();
            }
        });


    }


    public void letsDoLanLongGeocode(final double latitude, final double longitude, final CharSequence labelAddressFromSearch, final NetworkInterfaces.GoogleApiMapResponce googleApiMapResponce) {
        String latLng = latitude + ", " + longitude;
        String API_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
        apiInterface.doReverseGeocodingWithLatLng(API_URL, "AIzaSyA7H-Mvv4NVN08pu0dS4_eDD6tmtUghMrA", latLng).enqueue(new Callback<AddressFromGeocode>() {
            @Override
            public void onResponse(Call<AddressFromGeocode> call, Response<AddressFromGeocode> response) {

                if (!response.body().getStatus().equalsIgnoreCase("ZERO_RESULTS")) {

                    AddressFromGeocode addressFromGeocode = response.body();
                    Log.d("addressFromGeocode ", "" + addressFromGeocode.getStatus());


                    googleApiMapResponce.onGeocodeSuccess(addressFromGeocode);
                } else {
                    Log.d("LabelAddressFromSearch", (String) labelAddressFromSearch);

                }

            }

            @Override
            public void onFailure(Call<AddressFromGeocode> call, Throwable t) {
                Log.e("letsDoLanLongGeocode", "Search Result Error vinod" + t.getLocalizedMessage());
                googleApiMapResponce.onGeocodeFailure();

            }
        });
    }


    public void letsFindAddressDetail(String address, final NetworkInterfaces.googleMapApiForPlaceSearch googleApiMapResponce) {
        String API_URL = " https://maps.googleapis.com/maps/api/place/textsearch/json?";
        apiInterface.searchPlace(API_URL, "AIzaSyA7H-Mvv4NVN08pu0dS4_eDD6tmtUghMrA", address).enqueue(new Callback<PlaceSearchApiBean>() {
            @Override
            public void onResponse(Call<PlaceSearchApiBean> call, Response<PlaceSearchApiBean> response) {

                if (response != null) {

                    PlaceSearchApiBean placeSearchApiBean = response.body();
                    if (!placeSearchApiBean.getStatus().equals("OVER_QUERY_LIMIT")) {
                        googleApiMapResponce.onSuccessPlaceSearch(placeSearchApiBean);
                    }

                } else {
                    Log.d("googleApiMapResponce", "responce null");
                }
            }

            @Override
            public void onFailure(Call<PlaceSearchApiBean> call, Throwable t) {
                googleApiMapResponce.onFailurePlaceSearch();
            }
        });


    }

    public void letsAutoCompleteAddress(String input, final double latitude, final double longitude, final NetworkInterfaces.googleAutocompleteAddress googleAutocompleteAddress) {
        String latLng = latitude + ", " + longitude;
        String API_URl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
        String components = "country:IN";
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
        call = apiInterface.doAutoCompleteAddress(API_URl, "AIzaSyA7H-Mvv4NVN08pu0dS4_eDD6tmtUghMrA", input, latLng, components);
        call.enqueue(new Callback<AutoCompleteAddress>() {
            @Override
            public void onResponse(Call<AutoCompleteAddress> call, Response<AutoCompleteAddress> response) {
                if (response != null) {
                    AutoCompleteAddress autoCompleteAddress = response.body();
                    googleAutocompleteAddress.onSuccessAutoSearchAddress(autoCompleteAddress);
                } else {
                    Log.d("AutocompleteAddress", "responce null");
                }
            }

            @Override
            public void onFailure(Call<AutoCompleteAddress> call, Throwable t) {
                googleAutocompleteAddress.onFailureAutoSearchAddress();
            }
        });
    }


    public void callProfileEdit(RequestBody name, RequestBody email, RequestBody username, RequestBody mobile, RequestBody uid, RequestBody isdevice, RequestBody dob, RequestBody gender, RequestBody fileBody, final NetworkInterfaces.ProfileEditWebServiceInterface registerResponseInterface) {
        apiInterface.callProfileEditWebservice(name, email, username, mobile, uid, isdevice, dob, gender, fileBody).enqueue(new Callback<ProfileEditModel>() {
            @Override
            public void onResponse(Call<ProfileEditModel> call, Response<ProfileEditModel> response) {
                ProfileEditModel profileEditModel = response.body();
                registerResponseInterface.onSuccess(profileEditModel);
            }

            @Override
            public void onFailure(Call<ProfileEditModel> call, Throwable t) {
                registerResponseInterface.onFailure();
            }
        });

    }

    public void callProfileEditWithoutImage(RequestBody name, RequestBody email, RequestBody username, RequestBody mobile, RequestBody uid, RequestBody isdevice, RequestBody dob, RequestBody gender, RequestBody fileBody, final NetworkInterfaces.ProfileEditWebServiceInterface registerResponseInterface) {
        apiInterface.callProfileEditWebserviceWithoutImage(name, email, username, mobile, uid, isdevice, dob, gender).enqueue(new Callback<ProfileEditModel>() {
            @Override
            public void onResponse(Call<ProfileEditModel> call, Response<ProfileEditModel> response) {
                ProfileEditModel profileEditModel = response.body();
                registerResponseInterface.onSuccess(profileEditModel);
            }

            @Override
            public void onFailure(Call<ProfileEditModel> call, Throwable t) {
                registerResponseInterface.onFailure();
            }
        });

    }


    public void callAllTripByUser(String userId, String off, final NetworkInterfaces.AllTripByUserWebInterface allTripByUserWebInterface) {
        apiInterface.callAllTripFeedByUser(userId, off).enqueue(new Callback<AllTrips>() {
                                                                    @Override
                                                                    public void onResponse(Call<AllTrips> call, Response<AllTrips> response) {

                                                                        if (response.body() != null) {
                                                                            AllTrips allTrip = response.body();
                                                                            allTripByUserWebInterface.onSuccessAllTrip(allTrip);
                                                                        } else {
                                                                            AllTrips allTripFeed = null;
                                                                            allTripFeed.setStatus("sorry");
                                                                            allTripByUserWebInterface.onSuccessAllTrip(allTripFeed);
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<AllTrips> call, Throwable t) {
                                                                        allTripByUserWebInterface.onFailureAllTrip();
                                                                    }
                                                                }

        );

    }

    public void callFilterTrip(String userid, String filter, String off, final NetworkInterfaces.FilterTripByUserWebInterface filterTripByUserWebInterface) {
        apiInterface.callFilterTripFeedByUser(userid, filter, off).enqueue(new Callback<FilterTripBean>() {
            @Override
            public void onResponse(Call<FilterTripBean> call, Response<FilterTripBean> response) {

                if (response.body() != null) {
                    FilterTripBean filterTripBean = response.body();
                    filterTripByUserWebInterface.onSuccessFilterAllTrip(filterTripBean);
                } else {
                    FilterTripBean filterTripBean = new FilterTripBean();
                    filterTripBean.setStatus("sorry");
                    filterTripByUserWebInterface.onSuccessFilterAllTrip(filterTripBean);

                }

            }

            @Override
            public void onFailure(Call<FilterTripBean> call, Throwable t) {
                filterTripByUserWebInterface.onFailureFilterAllTrip();

            }
        });
    }


    public void callDeletebooking(String booking_id, String uid, final NetworkInterfaces.DeleteCabWebInterface deleteCabWebInterface) {
        apiInterface.callDeleteBooking(booking_id, uid).enqueue(new Callback<DeleteCabBean>() {
            @Override
            public void onResponse(Call<DeleteCabBean> call, Response<DeleteCabBean> response) {
                if (response.body() != null) {
                    DeleteCabBean deleteCabBean = response.body();
                    deleteCabWebInterface.onSuccessDeleteTrip(deleteCabBean);
                } else {
                    DeleteCabBean deleteCabBean = new DeleteCabBean();
                    deleteCabBean.setStatus("sorry");
                    deleteCabWebInterface.onSuccessDeleteTrip(deleteCabBean);
                }
            }

            @Override
            public void onFailure(Call<DeleteCabBean> call, Throwable t) {
                deleteCabWebInterface.onFailureDeleteTrip();
            }
        });
    }

    public void callCalculationDistanceApi(String origin, String destination, final NetworkInterfaces.CalculationDistance calculationDistance) {
        String latLng = destination;
        String sensor = "true";
        String mode = "driving";
        String API_URl = "http://maps.googleapis.com/maps/api/directions/json?";
        //  String components = "country:IN";

        apiInterface.doCalcualteDistance(API_URl, sensor, mode, origin, destination).enqueue(new Callback<CalculateDistancebean>() {
            @Override
            public void onResponse(Call<CalculateDistancebean> call, Response<CalculateDistancebean> response) {
                if (response != null) {
                    calculationDistance.calculationSuccess(response.body());
                } else {
                    //calculationDistance.calculationSuccess();
                }
            }

            @Override
            public void onFailure(Call<CalculateDistancebean> call, Throwable t) {
                calculationDistance.calculationFailure();
            }
        });
    }
}
