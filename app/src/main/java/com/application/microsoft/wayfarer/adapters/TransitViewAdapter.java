package com.application.microsoft.wayfarer.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.activities.EstimationActivity;
import com.application.microsoft.wayfarer.models.Route;
import com.application.microsoft.wayfarer.models.Transit;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by RAJULA on 26-02-2018.
 */

public class TransitViewAdapter extends ArrayAdapter<Transit> {
    private Context context;
    private int layoutResourceId;
    ArrayList<Transit> transits = new ArrayList<>();

    public TransitViewAdapter(Context context, int layoutResourceId, ArrayList<Transit> transits) {
        super(context, layoutResourceId, transits);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.transits = transits;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView (int position, final View convertView, ViewGroup parent){
        View row = convertView;
        TransitViewAdapter.ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.instructions = (TextView) row.findViewById(R.id.instruction);
            holder.distanceDuration = (TextView) row.findViewById(R.id.destination_text_view);
            holder.fare = (TextView) row.findViewById(R.id.fare_txt_view);
            holder.travelMode =(ImageView) row.findViewById(R.id.mode_image);
            holder.transitMode = (TextView) row.findViewById(R.id.transitNo);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final Transit transit = transits.get(position);
        holder.instructions.setText(transit.getInstructions());
        if (transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Bus")) {
            holder.travelMode.setImageResource(R.drawable.bus);
            holder.fare.setText("Metro Bus Fare "
                    + EstimationActivity.calculateACBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim())) + "\n"
                    + "Ordinary Bus Fare " + EstimationActivity.calculateOrdinaryBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim())));
        }else if (transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Metro rail")) {
            holder.travelMode.setImageResource(R.drawable.metro);
            holder.fare.setText("Metro Rail Fare " + EstimationActivity.calculateMetroRailFares(Double.parseDouble(transit.getDistance().replace("km", " ").trim())));

        } else if(transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Train")){
            holder.travelMode.setImageResource(R.drawable.mmts);
            holder.fare.setText("MMTS Fare " + EstimationActivity.calculateMMTSFares(Double.parseDouble(transit.getDistance().replace("km", " ").trim())));
        }
//         holder.distanceDuration.setText("Distance :" + transit.getDistance() + "\n" + "Duration :" + transit.getDuration());
        return row;
    }

    static class ViewHolder {
       ImageView travelMode;
       TextView distanceDuration;
       TextView instructions;
       TextView fare;
       TextView transitMode;
    }


}

