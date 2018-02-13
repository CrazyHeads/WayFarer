package com.application.microsoft.wayfarer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.activities.MainActivity;
import com.application.microsoft.wayfarer.models.Place;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by RAJULA on 12-02-2018.
 */


public class GridViewAdapter extends ArrayAdapter<Place> {
    private Context context;
    private int layoutResourceId;
    MainActivity mainActivity =  new MainActivity();
    private ArrayList<Place> placesList = mainActivity. getPlacesList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Place> placesList) {
        super(context, layoutResourceId, placesList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.placesList = placesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.tvName);
            holder.image = (ImageView) row.findViewById(R.id.ivimage);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final Place place = placesList.get(position);
        System.out.println(place.getName());
        holder.imageTitle.setText(place.getName());
        Picasso.with(context).load(placesList.get(position).getImgURL()).into(holder.image);
//        URL url = null;
//        Bitmap bmp = null;
//        try {
//            url = new URL(place.getImgURL());
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        holder.image.setImageBitmap(bmp);
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }


}