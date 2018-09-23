package com.taxi.passenger.RatingMVP.Presenter;

import android.util.Log;

import com.taxi.passenger.RatingMVP.RatingInterfaceContractor.RatingContractor;

/**
 * Created by Abhilasha Yadav on 9/27/2017.
 */

public class RatingPresenterImpletation implements RatingContractor.RatingPresenter {

    private RatingContractor. IRatingView IRatingView;
    private  RatingContractor.RatingInteracter ratingInteracter;

    public RatingPresenterImpletation(RatingContractor.IRatingView mIRatingView) {
        this.IRatingView = mIRatingView;
        this.ratingInteracter = new RatingInteracterImpl(this);
    }


    @Override
    public void getRating(String rating, int driverId) {
        IRatingView.showProgress();
        ratingInteracter.setRating(rating, driverId);

    }

    @Override
    public void ratingUpdated(String message) {
        Log.d("RatingPresenterImpleta",message);
        IRatingView.hideProgress();
        IRatingView.onRatingupdated(message);
    }
}
