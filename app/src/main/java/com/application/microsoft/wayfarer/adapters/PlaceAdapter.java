package com.application.microsoft.wayfarer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.models.Place;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gurkirat on 4/6/17.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{

    List<Place> placeItemList;

    public PlaceAdapter(List<Place> placeItemList){
        this.placeItemList = placeItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView posPlace;
        TextView txtPlace;

        public ViewHolder(View itemView){
            super(itemView);

            posPlace = (TextView)itemView.findViewById(R.id.tvPosition);
            txtPlace = (TextView)itemView.findViewById(R.id.txtPlace);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Place place = placeItemList.get(position);
        holder.txtPlace.setText(place.getName());
        holder.posPlace.setText(String.valueOf(placeItemList.indexOf(place)));
    }

    @Override
    public int getItemCount() {
        return placeItemList.size();
    }


}
