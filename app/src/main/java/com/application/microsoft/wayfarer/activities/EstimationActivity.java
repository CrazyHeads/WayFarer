package com.application.microsoft.wayfarer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.adapters.RoutesViewAdapter;
import com.application.microsoft.wayfarer.handlers.HttpHandler;
import com.application.microsoft.wayfarer.models.Place;
import com.application.microsoft.wayfarer.models.Route;
import com.application.microsoft.wayfarer.models.Transit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;



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
    TextView transitDetails;
    ArrayList<Place> placesList;
    RoutesViewAdapter routesViewAdapter;
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    ArrayList<Route> routes = new ArrayList<>();

    public ArrayList<Route> getRoutes() {
        return routes;
    }
    ListView listView;
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation);
        placesList = getIntent().getParcelableArrayListExtra("selectedPlacesList");
//        transitDetails = (TextView) findViewById(R.id.transitDetails);
        routesViewAdapter = new RoutesViewAdapter(this, R.layout.route_layout, routes);
        listView = (ListView) findViewById(R.id.route_view);
        listView.setAdapter(routesViewAdapter);
        new TransitDetails().execute();

        routesViewAdapter.addAll(routes);
        routesViewAdapter.notifyDataSetChanged();

    }

    public void save(View v) {
        Intent myIntent = new Intent(EstimationActivity.this, LoginActivity.class);
        startActivity(myIntent);

    }

    private class TransitDetails extends AsyncTask<String, Void, String> {

        String url;
        Transit transit = new Transit();


        @Override
        protected String doInBackground(String... strings) {

            for (int k = 0; k + 2 <= placesList.size(); k++) {
                Route route = new Route();
                url = createUrl(placesList.get(k).getLat() +"," + placesList.get(k).getLng(), placesList.get(k+1).getLat() + "," + placesList.get(k+1).getLng());
                System.out.println(placesList.get(k).getName()+" "+ placesList.get(k+1).getName());
                route.setSource(placesList.get(k).getName());
                route.setDestination(placesList.get(k+1).getName());
                HttpHandler sh = new HttpHandler();
                //                transitDetails.setText(placesList.get(k).getName() );
                String jsonStr = sh.makeServiceCall(url);
                ArrayList<Transit> transitList = new ArrayList<>();
                if (jsonStr != null) {
                    try {
                        System.out.println(url);
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray jsonData = jsonObj.getJSONArray("routes")
                                .getJSONObject(0).getJSONArray("legs")
                                .getJSONObject(0).getJSONArray("steps");
                        // .getJSONObject(0).getJSONArray("steps");

                        for (int i = 0; i < jsonData.length(); i++) {
                            JSONObject stop = jsonData.getJSONObject(i);
                            transit.setTravelMode(stop.getString("travel_mode"));
                            transit.setDistance(stop.getJSONObject("distance").getString("text"));
                            transit.setDuration(stop.getJSONObject("duration").getString("text"));
                            System.out.println(transit.getTravelMode());
//                            transitDetails.append(transit.getTravelMode());
                            transit.setInstructions(stop.getString("html_instructions"));
//                            transitDetails.append(transit.getInstructions() );
                            System.out.println(transit.getInstructions());
                            System.out.println(" for " + transit.getDistance() + " for " + transit.getDuration());
//                            transitDetails.append(" for " + transit.getDistance() + " for " + transit.getDuration() );
                            if (transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Bus")) {
                                transit.setNoOfStops(stop.getJSONObject("transit_details").getInt("num_stops"));
                                transit.setTransitNumber(stop.getJSONObject("transit_details").getJSONObject("line").getString("short_name"));
//                                transitDetails.append("No of Stops: " + transit.getNoOfStops()
//                                + "Transit Number " + transit.getTransitNumber()  + "Metro Bus Fare " + calculateACBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim()))
//                                         + "Metro Bus Fare " + calculateACBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim()))
//                                        + "Ordinary Bus Fare " + calculateOrdinaryBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim())) );
                                System.out.println("No of Stops: " + transit.getNoOfStops());
                                System.out.println("Transit Number " + transit.getTransitNumber());
                                System.out.println("Metro Bus Fare " + calculateACBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim())));
                                System.out.println("Ordinary Bus Fare " + calculateOrdinaryBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim())));
                            } else if (transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Metro rail")) {
                                transit.setNoOfStops(stop.getJSONObject("transit_details").getInt("num_stops"));
//                                transitDetails.append("Transit Details:" + transit.getNoOfStops()
//                                + "Metro Rail Fare " + calculateMetroRailFares(Double.parseDouble(transit.getDistance().replace("km", " ").trim())) );
                                System.out.println("Transit Details:" + transit.getNoOfStops());
                                System.out.println("Metro Rail Fare " + calculateMetroRailFares(Double.parseDouble(transit.getDistance().replace("km", " ").trim())));
                            } else if (transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Train")) {
//                                transitDetails.append("MMTS Fare " + calculateMMTSFares(Double.parseDouble(transit.getDistance().replace("km", " ").trim())) );
                                System.out.println("MMTS Fare " + calculateMMTSFares(Double.parseDouble(transit.getDistance().replace("km", " ").trim())));

                            }
                            transitList.add(transit);

                        }
                    } catch (JSONException e) {
                    }
                    route.setTransitInfo(transitList);

                }
                routes.add(route);
//                routesViewAdapter.add(route);

            }

             return null;
        }

    }


        private String createUrl(String origin, String destination) {

            return DIRECTION_URL_API + "origin=" +origin+ "&destination=" +destination+ "&mode=transit&key="+DIRECTION_API_KEY;
        }


    public static double calculateACBusFare(double distance) {

        //fare for RTC Metro Bus
//            metroBusFares.put(6,10);
//            metroBusFares.put(14,15);
//            metroBusFares.put(24,20);
//            metroBusFares.put(34,35);
//            metroBusFares.put(40,30);
//            Set<Integer> fares = metroBusFares.keySet();
//            for(Integer key: fares) {
//                if (distance <= key) {
//                    // System.out.println("Hii");
//                    return metroBusFares.get(key);
//                }
//
//            }
        if (distance == 2) {
            return 15;
        }
        if (distance <= 17) {
            return 15 + ((distance/2) * 5);
        }
        else {
            return 60 + ((distance/5) * 10);
        }
    }


    public static double calculateOrdinaryBusFare(double distance) {
            if (distance == 2) {
                return 8;
            }
            else if (distance <= 17) {
                return 8 + ((distance/2) * 2);
            }
            return 24 + ((distance/5));

        }

        public static double calculateMetroRailFares(double distance) {
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


        public static double calculateMMTSFares(double distance) {
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




