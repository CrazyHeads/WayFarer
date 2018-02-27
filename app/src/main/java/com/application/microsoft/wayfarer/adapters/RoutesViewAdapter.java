package com.application.microsoft.wayfarer.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.activities.EstimationActivity;
import com.application.microsoft.wayfarer.activities.MainActivity;
import com.application.microsoft.wayfarer.models.Route;
import com.application.microsoft.wayfarer.models.Route;
import com.application.microsoft.wayfarer.models.Transit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class RoutesViewAdapter extends ArrayAdapter<Route> {
    private Context context;
    private int layoutResourceId;
    EstimationActivity estimationActivity = new EstimationActivity();
    private ArrayList<Route> routesList =  estimationActivity.getRoutes();
    public RoutesViewAdapter(Context context, int layoutResourceId, ArrayList<Route> routesList) {
        super(context, layoutResourceId, routesList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.routesList = routesList;
    }
        @Override
        public View getView ( int position, final View convertView, ViewGroup parent){
            View row = convertView;
            RoutesViewAdapter.ViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.source = (TextView) row.findViewById(R.id.source_text_view);
                holder.destination = (TextView) row.findViewById(R.id.destination_text_view);
                row.setTag(holder);
            } else {
                holder = (RoutesViewAdapter.ViewHolder) row.getTag();
            }
            final Route route = routesList.get(position);
            holder.source.setText(route.getSource());
            holder.source.setText(route.getDestination());
            ListView transitDetails = (ListView) row.findViewById(R.id.grid_view);
            transitDetails.setAdapter(new TransitViewAdapter(context, R.layout.transit_layout, route.getTransitInfo()));
            return row;
        }

        static class ViewHolder {
            TextView source;
            TextView destination;

        }


}
