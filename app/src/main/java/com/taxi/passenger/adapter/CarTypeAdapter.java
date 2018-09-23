package com.taxi.passenger.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.taxi.passenger.R;
import com.taxi.passenger.model.response.CabDetail;
import com.taxi.passenger.utils.CircleTransform;
import com.taxi.passenger.utils.Constants;
import com.taxi.passenger.utils.Url;


public class CarTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Activity activity;
    private ArrayList<CabDetail> carTypeArray;
    private Typeface openSansRegular;
    private int itemsCount = 0;
    private boolean showLoadingView = false;

    private OnCarTypeClickListener onCarTypeClickListener;

    public CarTypeAdapter(Activity activity, ArrayList<CabDetail> carTypeArray) {

        this.activity = activity;
        this.carTypeArray = carTypeArray;
        openSansRegular = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-Regular_0.ttf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.car_type_layout, parent, false);
        CarTypeViewHolder carTypeViewHolder = new CarTypeViewHolder(view);
        carTypeViewHolder.layout_car_type_main.setOnClickListener(this);
        return carTypeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CarTypeViewHolder holder = (CarTypeViewHolder) viewHolder;
        if (getItemViewType(position) == Constants.VIEW_TYPE_DEFAULT) {
            bindCarTypeFeedItem(position, holder);
        } else if (getItemViewType(position) == Constants.VIEW_TYPE_LOADER) {
            bindLoadingFeedItem(holder);
        }
    }

    private void bindCarTypeFeedItem(int position, CarTypeViewHolder holder) {


        /*  JSONObject carTypeHasMap = carTypeArray.getJSONObject(position);*/

        Picasso.with(activity)
                .load(Uri.parse(Url.CAR_IMAGE_URL + carTypeArray.get(position).getIcon()))
                .placeholder(R.drawable.truck_icon)
                .transform(new CircleTransform())
                .into(holder.img_car_image);

        holder.txt_car_type.setText(carTypeArray.get(position).getCartype());

        holder.layout_car_type_main.setTag(holder);

    }


    private void bindLoadingFeedItem(CarTypeViewHolder holder) {
        System.out.println("BindLoadingFeedItem >>>>>");
    }

    @Override
    public int getItemCount() {
        return carTypeArray.size();
    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();
        CarTypeViewHolder holder = (CarTypeViewHolder) view.getTag();
        if (viewId == R.id.layout_car_type_main) {
            if (onCarTypeClickListener != null)
                onCarTypeClickListener.SelectCarType(holder.getPosition());
        }

    }

    public void updateItems() {
        itemsCount = carTypeArray.size();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadingView && position == 0) {
            return Constants.VIEW_TYPE_LOADER;
        } else {
            return Constants.VIEW_TYPE_DEFAULT;
        }
    }

    public class CarTypeViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout_car_type_main;
        ImageView img_car_image;
        TextView txt_car_type;

        public CarTypeViewHolder(View view) {
            super(view);

            layout_car_type_main = view.findViewById(R.id.layout_car_type_main);
            img_car_image = view.findViewById(R.id.img_car_image);
            txt_car_type = view.findViewById(R.id.txt_car_type);
        }
    }

    public void setOnCarTypeItemClickListener(OnCarTypeClickListener onCarTypeClickListener) {
        this.onCarTypeClickListener = onCarTypeClickListener;
    }

    public interface OnCarTypeClickListener {

        void SelectCarType(int position);
    }
}

