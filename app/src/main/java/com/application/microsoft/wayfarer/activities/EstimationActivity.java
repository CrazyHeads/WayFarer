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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by satyavati on 22-01-2018.
 */

public class EstimationActivity extends AppCompatActivity {
    Button select;
    private String origin;
    private String destination;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String DIRECTION_API_KEY = "AIzaSyDG7S40R4SgClQX9Zbm59W9ctYocGEWR4A";
    private static final Hashtable<Integer, Integer> metroBusFares = new Hashtable<Integer, Integer>();
    private static final Hashtable<Integer, Integer> ordinaryBusFares = new Hashtable<Integer, Integer>();
    private static final Hashtable<Integer, Integer> metroRailFares = new Hashtable<Integer, Integer>();
    private static final Hashtable<Integer, Integer> mmtsFares = new Hashtable<Integer, Integer>();


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
            String transitNumber = "";
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
                        JSONObject stop = jsonData.getJSONObject(i);
                        String travelMode = stop.getString("travel_mode");
                        String distance = stop.getJSONObject("distance").getString("text");
                        String duration = stop.getJSONObject("duration").getString("text");
                        System.out.println(travelMode);
                        String instructions = stop.getString("html_instructions");
                        System.out.println(instructions);
                        System.out.println(" for " +distance+ " for " + duration);
                        if (travelMode.equals("TRANSIT") && instructions.contains("Bus")) {
                            noOfStops = stop.getJSONObject("transit_details").getInt("num_stops");
                            transitNumber = stop.getJSONObject("transit_details").getJSONObject("line").getString("short_name");
                            System.out.println("No of Stops: "+noOfStops);
                            System.out.println("Transit Number " +transitNumber);
                            System.out.println("Metro Bus Fare "+calculateMetroBusFare(Double.parseDouble(distance.replace("km"," ").trim())));
                            System.out.println("Ordinary Bus Fare "+calculateOrdinaryBusFare(Double.parseDouble(distance.replace("km"," ").trim())));
                        }
                        else if(travelMode.equals("TRANSIT") && instructions.contains("Metro rail")) {
                            noOfStops = stop.getJSONObject("transit_details").getJSONObject("line").getInt("num_stops");
                            System.out.println("Transit Details:" +noOfStops);
                            System.out.println("Metro Rail Fare "+calculateMetroRailFares(Double.parseDouble(distance.replace("km"," ").trim())));
                        }
                        else if (travelMode.equals("TRANSIT") && instructions.contains("Train")) {
                            System.out.println("MMTS Fare "+calculateMMTSFares(Double.parseDouble(distance.replace("km"," ").trim())));

                        }

                    }
                } catch (JSONException e) {
                }
            }
            return null;


        }

        private String createUrl() {
            origin = "BVRIT HYDERABAD";
            destination = "Vidyanagar";
            return DIRECTION_URL_API + "origin=" +origin+ "&destination=" +destination+ "&mode=transit&key="+DIRECTION_API_KEY;
        }


        private double calculateMetroBusFare(double distance) {
            //fare for RTC Metro Bus
            metroBusFares.put(6,10);
            metroBusFares.put(14,15);
            metroBusFares.put(24,20);
            metroBusFares.put(34,35);
            metroBusFares.put(40,30);
            Set<Integer> fares = metroBusFares.keySet();
            for(Integer key: fares) {
                if (distance <= key) {
                   // System.out.println("Hii");
                    return metroBusFares.get(key);
                }

            }
            return -1;
        }

        private double calculateOrdinaryBusFare(double distance) {
            //fare for ordinary RTC  Bus
            ordinaryBusFares.put(2,5);
            ordinaryBusFares.put(10,10);
            ordinaryBusFares.put(18,15);
            ordinaryBusFares.put(28,20);
            ordinaryBusFares.put(40,25);
            Set<Integer> fares = ordinaryBusFares.keySet();
            for(Integer key: fares) {
                if (distance <= key) {
                    //System.out.println("Hii");
                    return ordinaryBusFares.get(key);
                }

            }
            return -1;
        }

        private double calculateMetroRailFares(double distance) {
            //fares for metro rail
            metroRailFares.put(2,10);
            metroRailFares.put(4,15);
            metroRailFares.put(6,25);
            metroRailFares.put(8,30);
            metroRailFares.put(10,35);
            metroRailFares.put(14,40);
            metroRailFares.put(18,45);
            metroRailFares.put(22,45);
            metroRailFares.put(26,55);
            metroRailFares.put(27,60);
            Set<Integer> fares = metroRailFares.keySet();
            for(Integer key: fares) {
                if (distance <= key) {
                    //System.out.println("Hii");
                    return metroRailFares.get(key);
                }

            }
            return -1;
        }


        private double calculateMMTSFares(double distance) {
            mmtsFares.put(15,5);
            mmtsFares.put(30,10);
            mmtsFares.put(40,11);
            Set<Integer> fares = mmtsFares.keySet();
            for(Integer key: fares) {
                if (distance <= key) {
                    //System.out.println("Hii");
                    return mmtsFares.get(key);
                }

            }
            return -1;

        }








        }


}

