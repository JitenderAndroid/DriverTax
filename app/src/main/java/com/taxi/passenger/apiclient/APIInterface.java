package com.taxi.passenger.apiclient;


import java.util.Map;

import com.taxi.passenger.AllTripFeedMvp.Model.AllTrips;
import com.taxi.passenger.CalculateDistanceMvp.Model.CalculateDistancebean;
import com.taxi.passenger.ChangePasswordMvp.Model.ChangePasswordModel;
import com.taxi.passenger.DeleteCabMvp.Model.DeleteCabBean;
import com.taxi.passenger.FilterAllTripMvp.Model.FilterTripBean;
import com.taxi.passenger.RatingMVP.Model.RatingModel;

import com.taxi.passenger.TripsMvp.Model.TripBookBean;
import com.taxi.passenger.apiclient.modelForGoogleApiMap.AddressFromGeocode;
import com.taxi.passenger.apiclient.modelForGoogleApiMap.PlaceSearchApiBean;
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
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface APIInterface {

    @POST("login")
    Call<UserLoginDetailModel> callLoginWebservice(@Query("device_token") String token,
                                                   @Query("email") String userName,
                                                   @Query("password") String password

    );


    @Multipart
    @POST("sign_up")
    Call<RegisterDetailModel> callRegisterWebservice(@Part("name") RequestBody name,
                                                     @Part("username") RequestBody userName,
                                                     @Part("email") RequestBody email,
                                                     @Part ("password") RequestBody password,
                                                     @Part("dob") RequestBody dob,
                                                     @Part("gender") RequestBody gender,
                                                     @Part("mobile") RequestBody mobile,
                                                     @Part("isdevice") RequestBody isDevice,
                                                     @Part("image\"; filename=\"image\" ") RequestBody reqFile,
                                                     @Part("device_token") RequestBody token,
                                                     @Part("facebook_id") RequestBody facebook_id,
                                                     @Part("twitter_id") RequestBody twitter_id
                                                     );


    @FormUrlEncoded
    @POST("forgot_password")
    Call<ForgotPasswordModel> callForgotPasswordWebService(@Field("email") String userName);


    @POST("rate_the_driver")
    Call<RatingModel> callRatingWebservice(@Query("rating") String rating,
                                           @Query("driver_id") int driver_id


    );


    @POST("accepted_ride_data")
    Call<AccepetedRideDriverDetail> callAcceptedRideDriverDetail(@Query("user_id") int user_id

    );


    @POST("user_logout")
    Call<LogoutApiModel> callLogoutUser(@Query("user_id") int user_id
    );


    @FormUrlEncoded
    @POST("change_password")
    Call<ChangePasswordModel> callChangePassword(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("book_cab")
    Call<TripBookBean> callTripBookBeanCall(@FieldMap Map<String,
            String> params);

/*    @GET("maps/api/place/autocomplete/json?")
    Call<AddressResponse> searchAddress(@Query("input") String user,
                                        @Query("key") String key,
                                        @Query("components") String components);*/

    @GET("maps/api/place/autocomplete/json?")
    Call<AddressFromGeocode> searchAddress(@Query("key") String s,
                                           @Query("input") String latLng);


    @GET("maps/api/geocode/json?")
    Call<AddressFromGeocode> doReverseGeocodingWithLatLng(@Query("key") String key,
                                                          @Query("latlng") String location);


    @GET
    Call<AddressFromGeocode> doReverseGeocodingWithLatLng(@Url String url,
                                                          @Query("key") String key,
                                                          @Query("latlng") String location);


    @GET
    Call<PlaceSearchApiBean> searchPlace(@Url String url,
                                         @Query("key") String key,
                                         @Query("query") String address);

    @GET
    Call<AutoCompleteAddress> doAutoCompleteAddress(@Url String url,
                                                    @Query("key") String key,
                                                    @Query("input") String address,
                                                    @Query("location") String location,
                                                    @Query("components") String components);

    @Multipart
    @POST("profile_edit")
    Call<ProfileEditModel> callProfileEditWebservice(@Part("name") RequestBody name,
                                                     @Part("email") RequestBody email,
                                                     @Part("username") RequestBody userName,
                                                     @Part("mobile") RequestBody mobile,
                                                     @Part("uid") RequestBody uid,
                                                     @Part("isdevice") RequestBody isDevice,
                                                     @Part("dob") RequestBody dob,
                                                     @Part("gender") RequestBody gender,
                                                     @Part("image\"; filename=\"image\" ") RequestBody reqFile);



    @Multipart
    @POST("profile_edit")
    Call<ProfileEditModel> callProfileEditWebserviceWithoutImage(@Part("name") RequestBody name,
                                                     @Part("email") RequestBody email,
                                                     @Part("username") RequestBody userName,
                                                     @Part("mobile") RequestBody mobile,
                                                     @Part("uid") RequestBody uid,
                                                     @Part("isdevice") RequestBody isDevice,
                                                     @Part("dob") RequestBody dob,
                                                     @Part("gender") RequestBody gender
                                                    );




    @FormUrlEncoded
    @POST ("load_trips")
    Call<AllTrips>    callAllTripFeedByUser(@Field("user_id")  String userId,
                                            @Field("off")  String off);



    @FormUrlEncoded
    @POST ("filter_book")
    Call<FilterTripBean>    callFilterTripFeedByUser(@Field("user_id")  String userId,
                                                  @Field("filter")    String filter,
                                                  @Field("off")  String off

    );



    @GET ("user_reject_trip")
    Call<DeleteCabBean> callDeleteBooking(@Query("booking_id") String bookingId,
                                           @Query("uid")String uid);


    @GET
    Call<CalculateDistancebean> doCalcualteDistance(@Url String url,
                                                      @Query("sensor") String  sensor,
                                                      @Query("mode") String mode,

                                                      @Query("origin") String origin,
                                                      @Query("destination") String destination
                                                    );

//
//    @GET("fix_area_list")
//    Call< >
}
