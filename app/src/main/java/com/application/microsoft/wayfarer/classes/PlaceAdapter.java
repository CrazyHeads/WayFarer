package com.application.microsoft.wayfarer.classes;

/**
 * Created by RAJULA on 28-01-2018.
 */


import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.models.Place;

public class PlaceAdapter extends ArrayAdapter<Place> {
    ArrayList<Place> placesList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    String url;
    CheckBox checkBox;

    public PlaceAdapter(Context context, int resource, ArrayList<Place> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        placesList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
                holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
                holder.placeName = (TextView) v.findViewById(R.id.tvName);
                v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setImageResource(R.drawable.logo);
        LinearLayout featuresTable = (LinearLayout) v.findViewById(R.id.linerLayout);

        new DownloadImageTask(holder.imageview,url).execute(placesList.get(position).getImgURL());
        holder.placeName.setText(placesList.get(position).getName());
//        if (position%4 == 0){
//            holder.placeName.setBackgroundColor(Color.parseColor("#1e86cf"));
//
//        } else if (position%4 == 1){
//            holder.placeName.setBackgroundColor(Color.parseColor("#2ca0ea"));
//
//        } else if (position%4 == 2){
//            holder.placeName.setBackgroundColor(Color.parseColor("#2cc4ea"));
//
//        } else if (position%4 == 3){
//            holder.placeName.setBackgroundColor(Color.parseColor("#2ceae3"));
//
//        }

        return v;

    }

    static class ViewHolder {
        public ImageView imageview;
        public TextView placeName;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String url;

        public DownloadImageTask(ImageView bmImage, String url) {
            this.bmImage = bmImage;
            this.url = url;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

}