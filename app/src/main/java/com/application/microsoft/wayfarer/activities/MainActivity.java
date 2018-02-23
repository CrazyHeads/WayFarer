package com.application.microsoft.wayfarer.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.adapters.ListViewAdapter;
import com.application.microsoft.wayfarer.handlers.HttpHandler;
import com.application.microsoft.wayfarer.models.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView listView;
    private ListViewAdapter listAdapter;
    private String city = "";

    String API_KEY = "AIzaSyCsZxCWGhQ9l7jkTBQx4kPCJu4EtAoRiFg";
    String[] cities;
    ArrayList<Place> placesList;
    int index;
    String PLACES_OF_INTEREST_URL = "";

    public ArrayList<Place> getPlacesList() {
        return placesList;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placesList = new ArrayList<>();
        cities = getResources().getStringArray(R.array.cities_arrays);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, cities);
        listView = (ListView) findViewById(R.id.listView);
        placesList.clear();
        listAdapter = new ListViewAdapter(this, R.layout.row,placesList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();

                city = cities[index];
                if(index != 0) {
                    PLACES_OF_INTEREST_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+point+of+interest&language=en&key=" + API_KEY + "";
                    listView.clearAnimation();
                    listAdapter.clear();
                    new GetPlaces().execute();
                    listAdapter.addAll(placesList);
                    listView = (ListView) findViewById(R.id.listView);
                    listView.invalidateViews();
                    listAdapter.notifyDataSetChanged();
                    listView.setAdapter(listAdapter);
                    System.out.println(city);
                    Toast.makeText(getApplicationContext(), "Selected: " + city, Toast.LENGTH_LONG).show();
                }
                index = -1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(getApplicationContext(),"Please Select a City!",  Toast.LENGTH_LONG).show();

            }
        });


    }


    public void plan(View v) {
        Intent intent = new Intent(MainActivity.this,EstimationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("placesList",placesList);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    @SuppressLint("StaticFieldLeak")
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
            String jsonStr = sh.makeServiceCall(PLACES_OF_INTEREST_URL);
            Log.e(TAG, "Response from PLACES_OF_INTEREST_URL: " + jsonStr);
            if (jsonStr != null) {
                try {
                    System.out.println(PLACES_OF_INTEREST_URL);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray  jsonArray = jsonObj.getJSONArray("results");
                    for (int i = 0; i <  jsonArray.length(); i++) {
                        JSONObject object =  jsonArray.getJSONObject(i);
                        Place place = new Place();
                        String photoReference;
                        place.setCity(city);
                        place.setID(object.getString("place_id"));
                        String placeStr = sh.makeServiceCall("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place.getID()+"&key="+API_KEY+"");
                        JSONObject jsonObj1 = new JSONObject(placeStr);
                        place.setDescription(jsonObj1.getJSONObject("result").getJSONArray("reviews").getJSONObject(0).getString("text"));
                        place.setName(object.getString("name"));
                        JSONObject geometry = object.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        place.setLat(location.getDouble("lat"));
                        place.setLng(location.getDouble("lng"));
                        JSONArray photos = object.getJSONArray("photos");
                        JSONObject photoReferenceUrl = photos.getJSONObject(0);
                        photoReference = photoReferenceUrl.getString("photo_reference");
                        String IMAGE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference="+photoReference+"&sensor=false&key="+ API_KEY +"";
                        System.out.println(IMAGE_URL);
                        place.setImgURL(IMAGE_URL);
                        if (!placesList.contains(place))
                            placesList.add(place);
                    }
                    for (int i = 0; i < placesList.size(); i++){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if(!Objects.equals(placesList.get(i).getCity(), city))
                                placesList.remove(i);
                        }
                    }
                    System.out.println("Done!!");
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                            System.out.println("Exception!!");
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
                        System.out.println("Json Error!!");
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            System.out.println("PostExecute!!");
            if (pDialog.isShowing())
                pDialog.dismiss();
            listAdapter.notifyDataSetChanged();

        }
    }

}
