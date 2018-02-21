package com.application.microsoft.wayfarer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.adapters.GridViewAdapter;
import com.application.microsoft.wayfarer.handlers.HttpHandler;
import com.application.microsoft.wayfarer.models.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by satyavati on 22-01-2018.
 */

public class EstimationActivity extends AppCompatActivity {
    Button select;
    private String origin;
    private String destination;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String DIRECTION_API_KEY = "AIzaSyDG7S40R4SgClQX9Zbm59W9ctYocGEWR4A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation);
        MainActivity mainActivity = new MainActivity();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapView);
//        mapFragment.getMapAsync(this);
//        GoogleMap mMap = null;
       /* Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<Place> selectedPlaces = (ArrayList<Place>) args.getSerializable("placesList");
        for (Place place : selectedPlaces) {
            if (place.getSelected()) {
                System.out.println(place.getName());
//                mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng()))
//                        .title(place.getName()));
            }
        }
        System.out.println("Ïn Estimate");
        select = (Button) findViewById(R.id.button2);*/
        new TransitDetails().execute();


    }

    public void save(View v) {
        Intent myIntent = new Intent(EstimationActivity.this, LoginActivity.class);
        startActivity(myIntent);

    }

    private class TransitDetails extends AsyncTask<String, Void, String> {

        String url;
        @Override
        protected String doInBackground(String... strings) {
            url = createUrl();
            String busNumber = "";
            int noOfStops;
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    System.out.println(url);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonData = jsonObj.getJSONArray("routes")
                            .getJSONObject(0).getJSONArray("legs")
                            .getJSONObject(0).getJSONArray("steps");
                           // .getJSONObject(0).getJSONArray("steps");
                    for(int i = 0;i < jsonData.length();i++) {
                        JSONObject stop0 = jsonData.getJSONObject(1);
                        String travelMode = stop0.getString("travel_mode");
                        System.out.println(travelMode);
                        if (travelMode == "TRANSIT") {
                            JSONObject line = stop0.getJSONObject("line");
                            JSONObject agencies = line.getJSONObject("agencies");
                            busNumber = agencies.getString("short_name");
                            noOfStops = line.getInt("num_stops");
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;


        }

        private String createUrl() {
            origin = "BVRIT Hyderabad";
            destination = "HPS Begumpet";
            return DIRECTION_URL_API + "origin=" +origin+ "&destination=" +destination+ "&mode=transit&key="+DIRECTION_API_KEY;
        }



    }


}

