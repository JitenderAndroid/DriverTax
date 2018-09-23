package com.taxi.passenger.RatingMVP;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.taxi.passenger.R;
import com.taxi.passenger.RatingMVP.Presenter.RatingPresenterImpletation;
import com.taxi.passenger.RatingMVP.RatingInterfaceContractor.RatingContractor;


/**
 * Created by Abhilasha Yadav on 9/27/2017.
 */

public class AfterRideActivity extends AppCompatActivity implements RatingContractor.IRatingView {

    @BindView(R.id.tvTotalBill)
    TextView tvTotalBill;
    @BindView(R.id.ratingBarRide)
    RatingBar ratingBarRide;
    @BindView(R.id.btnSubmitResponse)
    Button btnSubmitResponse;
    @BindView(R.id.progressbarRating)
    ProgressBar progressbarRating;
    private RatingPresenterImpletation mRatingPresenter;
   private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_completed);
        ButterKnife.bind(this);
        //there is presenter  obj for get and set data for communication
        mRatingPresenter = new RatingPresenterImpletation(this);
    }
  //RatingView  methods because they will show on activity
    @Override
    public void showProgress() {
        progressbarRating.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressbarRating.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRatingupdated(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();

    }

    @OnClick(R.id.btnSubmitResponse)
    public void onViewClicked() {
        String rating = String.valueOf(ratingBarRide.getRating());
        int driverId = 144;
        Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
        mRatingPresenter.getRating(rating, driverId);
    }
}
