package com.application.microsoft.wayfarer.activities;

//import android.content.Intent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.application.microsoft.wayfarer.NetworkCalls.ApiInterface;
import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.TSPEngine.TSPEngine;
import com.application.microsoft.wayfarer.handlers.HttpHandler;
import com.application.microsoft.wayfarer.models.Place;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.application.microsoft.wayfarer.utils.Functions.convertDistanceToMeter;

public class PlanActivity extends AppCompatActivity {

    ArrayList<Place> selectedPlaces = new ArrayList<>();
    private ApiInterface apiService;
    private TSPEngine mTspEng;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String DIRECTION_API_KEY = "AIzaSyDG7S40R4SgClQX9Zbm59W9ctYocGEWR4A";
    private String origin;
    private String destination;
    String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    private ArrayList<String> mDistanceList = new ArrayList<>();

    public ArrayList<Place> optimizedLocationListDistance;

    private int numberOfLocations;
    private int[][] mInputMatrixForTspDistance;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        selectedPlaces = getIntent().getParcelableArrayListExtra("placesList");
        for(int i = 0; i < selectedPlaces.size(); i++){
            if(selectedPlaces.get(i).getSelected() == null || !selectedPlaces.get(i).getSelected()){
                selectedPlaces.remove(i);
            }
        }

//        Place p = new Place();
//        Place p3 = new Place();
//        p3.setName("Jalavihar Water Park");
//        p3.setLat(17.4329278);
//        p3.setLng(78.464668);
//        selectedPlaces.add(p3);
//        p.setName("Golconda Fort");
//        p.setLat(17.383309);
//        p.setLng(78.4010528);
//        selectedPlaces.add(p);
//        Place p1 = new Place();
//        p1.setName("Chilkur Balaji Temple");
//        p1.setLat(17.3587171);
//        p1.setLng(78.29873909999999);
//        selectedPlaces.add(p1);
//        Place p2 = new Place();
//        p2.setName("Buddha Statue");
//        p2.setLat(17.4155657);
//        p2.setLng(78.47497300000001);
//        selectedPlaces.add(p2);
//
//        Place p4 = new Place();
//        p4.setName("T.Anjaiah Lumbini Park");
//        p4.setLat(17.4100675);
//        p4.setLng(78.47320839999999);
//        selectedPlaces.add(p4);

        mTspEng = new TSPEngine();
        numberOfLocations = selectedPlaces.size();
        mInputMatrixForTspDistance = new int[numberOfLocations][numberOfLocations];
        optimizedLocationListDistance = new ArrayList<>();
        new Distance().execute();
    }


    private void prepareDistanceList() {
        System.out.println("noOfLocations" + numberOfLocations);
        for (int i = 0; i < numberOfLocations; i++) {
            String origin = selectedPlaces.get(i).getLat() + "," + selectedPlaces.get(i).getLng();
            for (int j = 0; j < numberOfLocations; j++) {
                String dest = selectedPlaces.get(j).getLat() + "," + selectedPlaces.get(j).getLng();
//                  System.out.println(origin +" "+ dest );
                mDistanceList = calculateDistance(mDistanceList, origin, dest);
//                    System.out.println(mDistanceList.size());


            }
        }

    }

    private ArrayList<String> calculateDistance(final ArrayList<String> mDistanceList, final String origin, final String dest) {

        setOrigin(origin);
        setDestination(dest);

        new Distance().execute();

        System.out.println(getDistance());
        return mDistanceList;
    }

    public void getOptimizeRoute(ArrayList<String> mDistanceList) {
//        prepareDistanceList();
        int row = -1;
        System.out.println(mDistanceList.size());
        for (int i = 0; i < mDistanceList.size(); i++) {
            int column = i % numberOfLocations;
            if (column == 0)
                row++;
            System.out.println(mDistanceList.get(i));
            mInputMatrixForTspDistance[row][column] = convertDistanceToMeter(mDistanceList.get(i));
            System.out.println(row + " " + column + " " + mInputMatrixForTspDistance[row][column]);
        }

        ArrayList<Integer> pointOrderByDistance = mTspEng.computeTSP(mInputMatrixForTspDistance,
                numberOfLocations);

        for (int i = 0; i < pointOrderByDistance.size() - 1; i++) {
            optimizedLocationListDistance.add(selectedPlaces.get(pointOrderByDistance.get(i)));
            System.out.println("Hey" + pointOrderByDistance.get(i) + selectedPlaces.get(pointOrderByDistance.get(i)).getName());
        }

    }

    private String createUrl(String origin, String destination) {

        return DIRECTION_URL_API + "origin=" + origin + "&destination=" + destination + "&mode=driving&key=" + DIRECTION_API_KEY;
    }

    private class Distance extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PlanActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {
            for (int i = 0; i < numberOfLocations; i++) {
                String origin = selectedPlaces.get(i).getLat() + "," + selectedPlaces.get(i).getLng();
                for (int j = 0; j < numberOfLocations; j++) {
                    String dest = selectedPlaces.get(j).getLat() + "," + selectedPlaces.get(j).getLng();
                    //   mDistanceList = calculateDistance(mDistanceList, origin, dest);
                    String url = createUrl(origin, dest);
                    HttpHandler sh = new HttpHandler();
                    String jsonStr = sh.makeServiceCall(url);
                    System.out.println(url);
                    if (jsonStr != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(jsonStr);
                            distance = jsonObj.getJSONArray("routes")
                                    .getJSONObject(0)
                                    .getJSONArray("legs")
                                    .getJSONObject(0)
                                    .getJSONObject("distance")
                                    .getString("text");
                            System.out.println("Distance:" + distance);
                            mDistanceList.add(distance);
                       } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getOptimizeRoute(mDistanceList);
            pDialog.dismiss();
            for (int i = 0; i < numberOfLocations; i++) {
                System.out.println(i + ":" + optimizedLocationListDistance.get(i).getName());

            }
        }
    }
}
