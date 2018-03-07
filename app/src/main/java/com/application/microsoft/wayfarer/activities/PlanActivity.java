package com.application.microsoft.wayfarer.activities;

//import android.content.Intent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.TSPEngine.TSPEngine;
import com.application.microsoft.wayfarer.adapters.PlaceAdapter;
import com.application.microsoft.wayfarer.adapters.RecyclerViewAdapter;
import com.application.microsoft.wayfarer.handlers.HttpHandler;
import com.application.microsoft.wayfarer.interfaces.OnStartDragListener;
import com.application.microsoft.wayfarer.models.Place;
import com.application.microsoft.wayfarer.utils.SimpleItemTouchHelperCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.application.microsoft.wayfarer.utils.Functions.convertDistanceToMeter;

public class PlanActivity extends AppCompatActivity implements OnStartDragListener {

    ArrayList<Place> selectedPlaces = new ArrayList<>();
    private TSPEngine mTspEng;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String DIRECTION_API_KEY = "AIzaSyDG7S40R4SgClQX9Zbm59W9ctYocGEWR4A";
    private String origin;
    private String destination;
    String distance;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;


    RecyclerView rvPlaceItems;
    PlaceAdapter placeAdapter;


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


    private ItemTouchHelper mItemTouchHelper;
    private int nu = 0;
    TextView tvNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ArrayList<Place> placesList = getIntent().getParcelableArrayListExtra("placesList");
        Toast.makeText(getApplicationContext(),"This is the optimized route to visit all the places selected!",  Toast.LENGTH_LONG).show();
       // System.out.println("Before size" + placesList.size());
        for (int i = 0; i < placesList.size(); i++) {
            if (placesList.get(i).getSelected()) {
                selectedPlaces.add(placesList.get(i));
                System.out.println(placesList.get(i).getName());
            }
        }
       // System.out.println("after size" + selectedPlaces.size());
        new Distance().execute();
        rvPlaceItems = (RecyclerView) findViewById(R.id.rvPlaceItems);
        rvPlaceItems.setLayoutManager(new LinearLayoutManager(this));
        rvPlaceItems.setItemAnimator(new DefaultItemAnimator());

        placeAdapter = new PlaceAdapter(optimizedLocationListDistance);
        rvPlaceItems.setAdapter(placeAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(rvPlaceItems);

    }


    void moveItem(int oldPos, int newPos) {
        Place place = selectedPlaces.get(oldPos);

        selectedPlaces.remove(oldPos);
        selectedPlaces.add(newPos, place);
        placeAdapter.notifyItemMoved(oldPos, newPos);
    }

    void deleteItem(final int position) {

        System.out.println("Deleted from list"+position);
        if (selectedPlaces.remove(position) == null) {
            System.out.println("Cant remove anymore");
            return;
        }
        else {
            selectedPlaces.remove(position);
            //placeAdapter.notifyItemRangeRemoved(position,1);
            //placeAdapter.notifyItemRemoved(position);
            //  placeAdapter.notifyItemRemoved(position);
            placeAdapter.notifyDataSetChanged();
        }
        return;
    }



    public void getOptimizeRoute(ArrayList<String> mDistanceList) {
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

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        
    }

    public void estimate(View view) {
        Intent intent = new Intent(PlanActivity.this, EstimationActivity.class);
        intent.putParcelableArrayListExtra("selectedPlacesList", selectedPlaces);
        startActivity(intent);
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

            mTspEng = new TSPEngine();
            numberOfLocations = selectedPlaces.size();
            mInputMatrixForTspDistance = new int[numberOfLocations][numberOfLocations];
            optimizedLocationListDistance = new ArrayList<>();

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
            placeAdapter.notifyDataSetChanged();
        }
    }
}
