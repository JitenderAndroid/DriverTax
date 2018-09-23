package com.taxi.passenger.adapter;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taxi.passenger.R;
import com.taxi.passenger.activity.HomeActivity;
import com.taxi.passenger.model.response.AutoCompleteAddress;

/**
 * Created by Abhilasha Yadav on 11/17/2017.
 */

public class AddressSuggestionAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    HomeActivity  homeActivity;
    AutoCompleteAddress autoCompleteAddress;
    private ItemClickListener itemClickListener;
    public AddressSuggestionAdapter(HomeActivity homeActivity, AutoCompleteAddress autoCompleteAddress, ItemClickListener itemClickListener) {
        this.homeActivity = homeActivity;
        this.itemClickListener= itemClickListener;
        this.autoCompleteAddress= autoCompleteAddress;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.layout_for_suggestions, parent, false);
        AddressSuggestionAdapterViewHolder addressSuggestionAdapterViewHolder =new AddressSuggestionAdapterViewHolder(view);
        return addressSuggestionAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

        AddressSuggestionAdapterViewHolder viewHolder = (AddressSuggestionAdapterViewHolder) holder;
        viewHolder.tv_suggestion.setText(autoCompleteAddress.getPredictions().get(position).getDescription());


    }

    @Override
    public int getItemCount() {
        return autoCompleteAddress.getPredictions().size();
    }

    private class AddressSuggestionAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tv_suggestion;
        public AddressSuggestionAdapterViewHolder(View itemView) {
            super(itemView);
            tv_suggestion= itemView.findViewById(R.id.tv_name_suggestion);
            tv_suggestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.setData(autoCompleteAddress.getPredictions().get(getAdapterPosition()).getDescription());
                }
            });
        }
    }

    public interface  ItemClickListener{

        void setData( String address);
    }
}



