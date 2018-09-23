package com.taxi.passenger.RatingMVP.RatingInterfaceContractor;

/**
 * Created by Abhilasha Yadav on 11/3/2017.
 */

public interface RatingContractor {



    interface RatingInteracter {
        void setRating(String rating ,int  driverId);

    }



    interface RatingPresenter {
        void getRating(String rating, int driverId);
        void ratingUpdated(String message);
    }

    interface IRatingView {
        void showProgress();

        void hideProgress();

        void onRatingupdated(String message);

    }

}
