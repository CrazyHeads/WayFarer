package com.application.microsoft.wayfarer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
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


public class ListViewAdapter extends ArrayAdapter<Place> {
    private Context context;
    private int layoutResourceId;
    MainActivity mainActivity = new MainActivity();
    private ArrayList<Place> placesList = placesList = mainActivity.getPlacesList();

    public ListViewAdapter(Context context, int layoutResourceId, ArrayList<Place> placesList) {
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
            holder.placeTitle = (TextView) row.findViewById(R.id.tvName);
            holder.image = (ImageButton) row.findViewById(R.id.ivimage);
            holder.placeDesc =(TextView) row.findViewById(R.id.tvDesc);
            holder.checkbox = (CheckBox) row.findViewById(R.id.list_item_checkbox);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final Place place = placesList.get(position);
        holder.placeTitle.setText(place.getName());
        holder.placeDesc.setText(place.getDescription());
        Picasso.with(context).load(placesList.get(position).getImgURL()).into(holder.image);
        holder.checkbox.setId(position);
        holder.checkbox.setTag(Integer.valueOf(position));
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int id = cb.getId();
                if (cb.isChecked() == true && placesList.get(id).getSelected() != null)
                    cb.setChecked(false);
                cb.setChecked(true);
                if (cb.isChecked()) {
                    placesList.get(id).setSelected(true);
                }

                if (!cb.isChecked())
                    placesList.get(id).setSelected(false);

            }
        });
        return row;
    }

    static class ViewHolder {
        TextView placeTitle;
        ImageButton image;
        CheckBox checkbox;
        TextView placeDesc;
    }

}