package com.taxi.passenger.interfaces;



/*import com.taxi.response.AllTrip;
import com.taxi.response.DriverLoginDetailModel;*/

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

/**
 * Created by Abhilasha yadav on 6/10/2017.
 */


public class NetworkInterfaces {
    public interface LoginWebServiceInterface {
        void onSuccess(String status, String errorcode, UserLoginDetailModel driverLoginDetail);
        void onFailure(Throwable t);
    }

    public interface RegisterWebServiceInterface {

        void onSuccess(RegisterDetailModel registerDetailModel);
        void onFailure();

    }

    public interface ForgotPasswordInterface {
        void onSuccess(ForgotPasswordModel forgotPasswordModel);
        void onFailure();
    }

    public interface  RatingInterface{
        void onSuccess(RatingModel ratingModel);
        void onFailure();
    }

    public interface  AcceptedRideDriverDetailedInterfaces{
        void onSuccess(AccepetedRideDriverDetail accepetedRideDriverDetail);
        void onFailure();
    }

    public interface  LogoutUserInterfaces{
        void onSuccess(LogoutApiModel logoutModel);
        void onFailure();
    }

    public interface   ChangePassword{
        void onSuccess(ChangePasswordModel changePasswordModel);
        void onFailure();
    }

    public  interface   BookTrip{
        void onSuccess(TripBookBean mtripBookBean);
        void onFailure();
    }

    public interface GoogleApiMapResponce{
        void onGeocodeSuccess(AddressFromGeocode addressFromGeocode);
        void onGeocodeFailure();
    }


    public interface googleMapApiForPlaceSearch{
        void onSuccessPlaceSearch(PlaceSearchApiBean placeSearchApiBean);
        void onFailurePlaceSearch();
    }


    public interface googleAutocompleteAddress{
        void onSuccessAutoSearchAddress(AutoCompleteAddress autoCompleteAddress);
        void onFailureAutoSearchAddress();
    }


    public interface ProfileEditWebServiceInterface {

        void onSuccess(ProfileEditModel profileEditModel);
        void onFailure();

    }


    public interface AllTripByUserWebInterface{
        void onSuccessAllTrip(AllTrips allTripFeed);
        void onFailureAllTrip();
    }



    public interface FilterTripByUserWebInterface{
        void onSuccessFilterAllTrip(FilterTripBean allTripFeed);
        void onFailureFilterAllTrip();
    }

    public interface  DeleteCabWebInterface{
        void onSuccessDeleteTrip(DeleteCabBean deleteCabBean);
        void onFailureDeleteTrip();
    }


  public   interface  CalculationDistance{
        void calculationSuccess(CalculateDistancebean calculateDistancebean);
        void  calculationFailure();
    }
}
