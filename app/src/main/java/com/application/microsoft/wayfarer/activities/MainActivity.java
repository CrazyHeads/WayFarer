package com.application.microsoft.wayfarer.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.classes.HttpHandler;
import com.application.microsoft.wayfarer.classes.PlaceAdapter;
import com.application.microsoft.wayfarer.models.Place;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    GridView gridview;



    String api_key = "AIzaSyAhEjlG0p370mhPHMS0mQhlU9iSS6Kb0yc";
    String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=hyderabad+city+point+of+interest&language=en&key="+api_key+"";

    ArrayList<Place> placesList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesList = new ArrayList<>();
        initView();


        new GetPlaces().execute();
    }


    private void initView()
    {

        gridview = (GridView) findViewById(R.id.gridView);

//        gridview.setOnItemClickListener(this);
    }

    public void plan(View v) {
        Intent myIntent = new Intent(MainActivity.this, EstimationActivity.class);
        startActivity(myIntent);

    }

    public void show(View v) {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(myIntent);

    }


    private class GetPlaces extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    JSONArray  jarray = jsonObj.getJSONArray("results");


                    for (int i = 0; i <  jarray.length(); i++) {
                        JSONObject object =  jarray.getJSONObject(i);

                        Place place = new Place();
                        String photoReference = null;
                        place.setName(object.getString("name"));
                        JSONObject geometry = object.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        place.setLat(location.getDouble("lat"));
                        place.setLng(location.getDouble("lng"));
                        JSONArray photos = object.getJSONArray("photos");
                        JSONObject getPhtotos = photos.getJSONObject(0);
                        photoReference = getPhtotos.getString("photo_reference");
                        String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?photoreference="+photoReference+"&sensor=false&maxheight=400&maxwidth=400&key="+ api_key +"";
                        System.out.println(imageUrl);
                        place.setImgURL(imageUrl);
                        placesList.add(place);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            PlaceAdapter placeAdapter;
            placeAdapter = new PlaceAdapter(getApplicationContext(), R.layout.row, placesList);
            gridview.setAdapter(placeAdapter);

        }

    }
}
