package com.application.microsoft.wayfarer.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.adapters.RoutesViewAdapter;
import com.application.microsoft.wayfarer.adapters.TransparentProgressDialog;
import com.application.microsoft.wayfarer.handlers.HttpHandler;
import com.application.microsoft.wayfarer.models.Place;
import com.application.microsoft.wayfarer.models.Route;
import com.application.microsoft.wayfarer.models.Transit;
import com.application.microsoft.wayfarer.utils.ConnectionFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;



/*Estimating the total cost of the selected places using Public Transit */

public class EstimationActivity extends AppCompatActivity {
    private static double busFare = 8;
    private static double ACBusFare = 15;
    private static double metroFare = 10;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
   // private static final String DIRECTION_API_KEY = "AIzaSyDG7S40R4SgClQX9Zbm59W9ctYocGEWR4A";
    private static final String DIRECTION_API_KEY = "AIzaSyDkM0-WNudAA2S03EJDzC7xOfCCfo8jgDM";
    private static final Hashtable<Integer, Integer> mmtsFares = new Hashtable<Integer, Integer>();
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    TextView details;
    ArrayList<Place> placesList;
    private TransparentProgressDialog pDialog;
    double totalFare = 0.0;
    private ArrayList<String> tripDetails;


    ArrayList<Route> routes = new ArrayList<>();
    StringBuffer sb = new StringBuffer();
    public ArrayList<Route> getRoutes() {
        return routes;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation);
        placesList = getIntent().getParcelableArrayListExtra("selectedPlacesList");
        details = (TextView) findViewById(R.id.textView);
        details.setMovementMethod(new ScrollingMovementMethod());
        new TransitDetails().execute();
    }
    /* Saving the selected Places List */
    public void save(View v) {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name  = sharedPreferences.getString("UserID","");
        if(!name.equals("")){
            SavePlan savePlan = new SavePlan();
            savePlan.execute();
            Intent myIntent = new Intent(EstimationActivity.this, MenuActivity.class);
            startActivity(myIntent);

        } else {
            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Plan",new Gson().toJson(placesList));
            editor.apply();
            Intent intent = new Intent(EstimationActivity.this, LoginActivity.class);
            intent.putParcelableArrayListExtra("selectedPlacesList",placesList);
            startActivity(intent);
        }


    }

    private class TransitDetails extends AsyncTask<String, Void,Void> {
        String url;
        Transit transit = new Transit();
        /* Displaying the please wait dialog box until the data is displayed*/
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(EstimationActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            LayoutInflater li = LayoutInflater.from(EstimationActivity.this);
            @SuppressLint("InflateParams") View dialogView = li.inflate(R.layout.progress_diaglog, null);
            final ImageView imgView = (ImageView) dialogView.findViewById(R.id.imageView);
            Glide.with(EstimationActivity.this).load(R.drawable.route).into(new GlideDrawableImageViewTarget(imgView));
            TextView tv = (TextView) dialogView.findViewById(R.id.textView);
            tv.setText("Finding the mode of transport ...");
            pDialog = new TransparentProgressDialog(EstimationActivity.this, R.drawable.search_places);
            for(int i = 0; i < 1000; i++);
//              Ion.with(imgView).load("https://cdn.dribbble.com/users/127072/screenshots/1582404/pin-eye2.gif");
            pDialog.setContentView(dialogView);
//              pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            System.out.println("PostExecute!!");
            if (pDialog.isShowing())
                pDialog.dismiss();
            details.setText(String.valueOf(sb));

        }
        /* It invokes as soon as the PreExecute finishes executing.Here we are setting the Source and Destination from the PlacesList and displaying the respective Bus Transit Fare*/
        @Override
        protected Void doInBackground(String... strings) {
            placesList.add(placesList.get(0));
            for (int k = 0; k + 2 <= placesList.size(); k++) {
                Route route = new Route();
                url = createUrl(placesList.get(k).getLat() +"," + placesList.get(k).getLng(), placesList.get(k+1).getLat() + "," + placesList.get(k+1).getLng());
                System.out.println(placesList.get(k).getName()+" "+ placesList.get(k+1).getName());
                route.setSource(placesList.get(k).getName());
                sb.append(placesList.get(k).getName() + "\n");
                route.setDestination(placesList.get(k+1).getName());
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.makeServiceCall(url);
                ArrayList<Transit> transitList = new ArrayList<>();
                if (jsonStr != null) {
                    try {
                        System.out.println(url);
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray jsonData = jsonObj.getJSONArray("routes")
                                .getJSONObject(0).getJSONArray("legs")
                                .getJSONObject(0).getJSONArray("steps");
/* Getting the distance,duration and mode from the JSON object */
                        for (int i = 0; i < jsonData.length(); i++) {
                            double fare = 0.0;
                            JSONObject stop = jsonData.getJSONObject(i);
                            transit.setTravelMode(stop.getString("travel_mode"));
                            transit.setDistance(stop.getJSONObject("distance").getString("text"));
                            transit.setDuration(stop.getJSONObject("duration").getString("text"));
                            transit.setInstructions(stop.getString("html_instructions"));
                            printDetails();
                            if (transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Bus")) {
                                transit.setNoOfStops(stop.getJSONObject("transit_details").getInt("num_stops"));
                                transit.setTransitNumber(stop.getJSONObject("transit_details").getJSONObject("line").getString("short_name"));
                                fare = calculateOrdinaryBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim()));
                                printBusDetails(fare);
                                totalFare += fare;

                            } else if (transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Metro rail")) {
                                transit.setNoOfStops(stop.getJSONObject("transit_details").getInt("num_stops"));
                                fare = calculateMetroRailFares(Double.parseDouble(transit.getDistance().replace("km", " ").trim()));
                                printMetroDetails(fare);
                                totalFare += fare;
                            } else if (transit.getTravelMode().equals("TRANSIT") && transit.getInstructions().contains("Train")) {
                                fare = calculateMMTSFares(Double.parseDouble(transit.getDistance().replace("km", " ").trim()));
                                printTrainDetails(fare);
                                totalFare += fare;
                            } else {
                                sb.append("\n");
                            }
                            transitList.add(transit);

                        }
                    } catch (JSONException e) {
                    }
                    route.setTransitInfo(transitList);

                }
                routes.add(route);
                route.setFare(totalFare);
                System.out.println("Total Fare " +totalFare);
                totalFare = Math.ceil(totalFare);

            }
            sb.append("\n\nTOTAL FARE Rs: " +totalFare + "\n");



            return null;
        }
        /* Printing the details of the transit mode,duration and distance*/
        public void printDetails() {
            sb.append(transit.getTravelMode());
            sb.append("\n\n");
            sb.append(transit.getInstructions());
            sb.append(" for " + transit.getDistance() + " for " + transit.getDuration());
            sb.append("\n\n");
            return;
        }
        public void printBusDetails(double fare) {
            sb.append("No of Stops: " +transit.getNoOfStops() + "\n");
            sb.append("Transit Number " +transit.getTransitNumber());
            sb.append("\n");
            sb.append("Ac Bus Fare " + calculateACBusFare(Double.parseDouble(transit.getDistance().replace("km", " ").trim())) + "\n");
            sb.append("Ordinary Bus Fare " + fare + "\n");
            return;
        }
        public void printMetroDetails(double fare) {
            sb.append("Transit Details: " + transit.getNoOfStops() + "\n");
            sb.append("Metro Rail Fare " + fare);
            return;
        }
        public void printTrainDetails(double fare) {
            sb.append("MMTS Fare " +fare);
        }



    }

    /* Generating direction URL */
    private String createUrl(String origin, String destination) {

        return DIRECTION_URL_API + "origin=" +origin+ "&destination=" +destination+ "&mode=transit&key="+DIRECTION_API_KEY;
    }
/* Calculating the  respective busFares */

    public static double calculateACBusFare(double distance) {

        if (distance <= 2) {
            return ACBusFare;
        }
        if (distance <= 17) {
            return ACBusFare + ((distance/2) * 5);
        }
        else {
            return (ACBusFare + 3 + ((distance/2) * 5))+ ((distance/5) * 10);
        }
    }


    public static double calculateOrdinaryBusFare(double distance) {
        if (distance <= 2) {
            return busFare;
        }
        else if (distance <= 17) {
            return busFare + ((distance/2) * 2);
        }
        return (busFare + ((distance/2) * 2)) + ((distance/5));
    }

    public static double calculateMetroRailFares(double distance) {
        //fares for metro rail
        if (distance <= 2) {
            return metroFare;
        }

        return metroFare + (distance/2) * 5;
    }


    public static double calculateMMTSFares(double distance) {
        //standard fares of MMTS
        mmtsFares.put(15,5);
        mmtsFares.put(30,10);
        mmtsFares.put(40,11);
        Set<Integer> fares = mmtsFares.keySet();
        for(Integer key: fares) {
            if (distance <= key) {
                return mmtsFares.get(key);
            }

        }
        return -1;

    }
    /* If the user clicks on the save plan,he will be directed to the Menus activity*/
    public class SavePlan extends AsyncTask<String,String,String>
    {
        String flag = "";
        String role, cd;
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            if(isSuccess) {
                Intent intent = new Intent(EstimationActivity.this, MyPlanActivity.class);
                startActivity(intent);
                finish();
            }
        }



        @Override
        /* Inserting into the database of the selected Places,City and the UserId*/

        protected String doInBackground(String... params) {
            Connection con = null;
            Statement stmt = null;
            try {
                con = ConnectionFactory.getConnection();
                System.out.print("Innnn");
                if (con == null) {
                    flag = "Error in connection with SQL server";
                }else{
                    System.out.print("In Save");
                    sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    String userID = sharedPreferences.getString("UserID","");
                   // System.out.println("Us")
//                    java.util.Date date = new java.util.Date();
//                    System.out.print(date);
                   // String query = "insert into trips(userId,places,city,madeOn) values("+userID+", '"+new Gson().toJson(placesList)+"', '"+placesList.get(0).getCity()+"', '"+date.toString()+"');";
                    String query = "insert into trips(userId,places,city) values("+userID+", '"+new Gson().toJson(placesList)+"', '"+placesList.get(0).getCity()+"');";
                    System.out.print(query);
                    stmt = con.createStatement();
                    int flag = stmt.executeUpdate(query);
                    System.out.println("FLAG"+flag);
                    if (flag < 1) {
                        System.out.println("Plan not Added!");
                    } else {
                        System.out.println("Added Plan!!");
                    }
                }



            }catch (Exception ex)
            {
                isSuccess = false;
                flag = "Exceptions";
                Log.e("ERROR", ex.getMessage());

            } finally {

//                try {
////                    stmt.close();
////                    con.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
            }

            return flag;
        }
    }

}