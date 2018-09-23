package com.taxi.passenger.RatingMVP.Presenter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.taxi.passenger.R;
import com.taxi.passenger.RatingMVP.Model.RatingModel;
import com.taxi.passenger.RatingMVP.RatingInterfaceContractor.RatingContractor;
import com.taxi.passenger.apiclient.WebService;
import com.taxi.passenger.interfaces.NetworkInterfaces;

/**
 * Created by Abhilasha Yadav on 9/27/2017.
 */

public class RatingInteracterImpl implements RatingContractor.RatingInteracter,
        NetworkInterfaces.RatingInterface {

     Context context;
    RatingPresenterImpletation mPresenter;
    public RatingInteracterImpl(RatingPresenterImpletation ratingPresenterImpletation) {
        mPresenter = ratingPresenterImpletation;
    }

    @Override
    public void setRating(String rating,int driverId) {
        WebService.getInstance().callRatingWebservice(rating,driverId,  this);
    }


    @Override
    public void onSuccess(RatingModel ratingModel) {
        if (ratingModel != null){
            Log.d("RatingPresenterImpleta",ratingModel.getStatus());
            mPresenter.ratingUpdated(ratingModel.getStatus());
        }
    }

    @Override
    public void onFailure() {
         String msg=Resources.getSystem().getString(R.string.rating_failure_msg);
         mPresenter.ratingUpdated(msg);
    }


}
