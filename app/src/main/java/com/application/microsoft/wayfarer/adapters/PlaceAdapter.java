package com.application.microsoft.wayfarer.adapters;

/**
 * Created by RAJULA on 28-01-2018.
 */


import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.models.Place;
import com.squareup.picasso.Picasso;

public class PlaceAdapter extends ArrayAdapter<Place> {
    ArrayList<Place> placesList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    String url;
    CheckBox checkBox;
    private Context context;

    static class ViewHolder {
        public ImageView imageview;
        public TextView placeName;
        public CheckBox checkbox;
    }

    public PlaceAdapter(Context context, int resource, ArrayList<Place> objects) {
        super(context, resource, objects);
        this.context = context;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        placesList = objects;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
                holder.imageview = (ImageView) v.findViewById(R.id.ivimage);
                holder.placeName = (TextView) v.findViewById(R.id.tvName);
               // holder.checkbox = (CheckBox) v.findViewById(R.id.checkBox);
                v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setImageResource(R.drawable.logo);
        holder.checkbox.setId(position);
        holder.imageview.setId(position);

        Picasso.with(context).load(placesList.get(position).getImgURL()).into(holder.imageview);
        holder.placeName.setText(placesList.get(position).getName());
        return v;

    }
}