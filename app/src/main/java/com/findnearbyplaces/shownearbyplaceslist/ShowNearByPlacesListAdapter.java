package com.findnearbyplaces.shownearbyplaceslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.findnearbyplaces.model.NearByPlaces;

import java.util.List;

import sms.sarmila.ele.com.findnearbyplaces.R;


public class ShowNearByPlacesListAdapter extends RecyclerView.Adapter<ShowNearByPlacesListAdapter.MyViewHolder> {

    private List<NearByPlaces> nearByPlacesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name , address, currentlyOpen , rating ;
        private ImageView favourite ;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            address = (TextView) view.findViewById(R.id.tv_address);
            currentlyOpen = (TextView) view.findViewById(R.id.tv_currentlyOpen);
            rating = (TextView) view.findViewById(R.id.tv_rating);
            favourite = (ImageView) view.findViewById(R.id.iv_favourite);
        }
    }


    public ShowNearByPlacesListAdapter(List<NearByPlaces> nearByPlacesList ) {
        this.nearByPlacesList = nearByPlacesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.near_by_places_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NearByPlaces nearByPlaces = nearByPlacesList.get(position);
        holder.name.setText(nearByPlaces.getName());
        holder.currentlyOpen.setText(nearByPlaces.isCurrentlyOpen() ? "Yes" : "No");
        holder.rating.setText((int) nearByPlaces.getRating());
        holder.address.setText(nearByPlaces.getAddress());
        //holder.favourite.setBackgroundResource(nearByPlaces.isFavourite() ? );
    }

    @Override
    public int getItemCount() {
        return nearByPlacesList.size();
    }
}
