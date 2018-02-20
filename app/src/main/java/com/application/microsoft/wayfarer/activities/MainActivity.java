package com.application.microsoft.wayfarer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.adapters.GridViewAdapter;
import com.application.microsoft.wayfarer.handlers.HttpHandler;
import com.application.microsoft.wayfarer.models.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener  {
	private String TAG = MainActivity.class.getSimpleName();
	private ProgressDialog pDialog;
    String api_key = "AIzaSyA2cA02iXGXYtR6Gby9OG6jpEwMcwgcDyc";
    String[] cities;
    public ArrayList<Place> getPlacesList() {
        return placesList;
    }
    ArrayList<Place> placesList;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    
    private String city = "";
    int index;
    String publicURL = "https://maps.googleapis.com/maps/api/directions/json?origin=%20Mahavir%20towers%20hyderabad&destination=hps%20begumpet%20hyderabd&waypoint%20=%20BVRITH%20Bachupally%20Hyderabad&mode=transit&key=AIzaSyDG7S40R4SgClQX9Zbm59W9ctYocGEWR4A";
    String url = "";// = "https://maps.googleapis.com/maps/api/place/textsearch/json?query='"+city+"'+city+point+of+interest&language=en&key="+api_key+"";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placesList = new ArrayList<>();
        final Bitmap[] bitmap = new Bitmap[1];
        cities = getResources().getStringArray(R.array.cities_arrays);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,cities );
        gridView = (GridView) findViewById(R.id.gridView);
        placesList.clear();
        gridAdapter = new GridViewAdapter(this, R.layout.row,placesList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();

                city = cities[index];
                if(index != 0) {
                    url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+point+of+interest&language=en&key=" + api_key + "";
                    gridView.clearAnimation();
                    gridAdapter.clear();
//                    placesList.removeAll(placesList);
                    new GetPlaces().execute();
                    gridAdapter.addAll(placesList);
                    gridView = (GridView) findViewById(R.id.gridView);
                    gridView.invalidateViews();
                    gridAdapter.notifyDataSetChanged();
                    gridView.setAdapter(gridAdapter);
                    System.out.println(city);
                }
                index = -1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final Place place = placesList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                bitmap[0] =  GetBitmapfromUrl(place.getImgURL());
                intent.putExtra("title", place.getDescription());
                intent.putExtra("image",bitmap[0]);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void plan(View v) {
        Intent myIntent = new Intent(MainActivity.this, EstimationActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("placesList",(Serializable)placesList);
        myIntent.putExtra("BUNDLE",args);
        startActivity(myIntent);

    }




    public Bitmap GetBitmapfromUrl(String scr) {
        try {
            URL url=new URL(scr);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input=connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(input);
            return bmp;



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
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
//            placesList = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    System.out.println(url);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray  jarray = jsonObj.getJSONArray("results");
                    for (int i = 0; i <  jarray.length(); i++) {
                        JSONObject object =  jarray.getJSONObject(i);
                        Place place = new Place();
                        String photoReference = null;
                        place.setCity(city);
                        place.setID(object.getString("id"));
                        place.setName(object.getString("name"));
                        JSONObject geometry = object.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        place.setLat(location.getDouble("lat"));
                        place.setLng(location.getDouble("lng"));
                        JSONArray photos = object.getJSONArray("photos");
                        JSONObject getPhtotos = photos.getJSONObject(0);
                        photoReference = getPhtotos.getString("photo_reference");
                        String name = object.getString("name").replace(" ","%20");
                        String detailUrl = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="+name+"";
                        System.out.println(detailUrl);
                        String jsonStr1 = sh.makeServiceCall(detailUrl);
                        System.out.println(jsonStr1);
                        Log.e(TAG, "Response from url: " + jsonStr1);
                        JSONObject jsonObject = new JSONObject(jsonStr1);
                        JSONObject details = jsonObject.getJSONObject("query");
                        details = details.getJSONObject("pages");
                        String k = details.keys().next();
                        if (k.equals("-1")){
                            place.setDescription(" ");
                            System.out.println("No Desc!!");
                        } else {
                            details = details.getJSONObject(k);
                            System.out.println("Yes Desc!!");
                            place.setDescription(details.getString("extract"));
                            System.out.println(place.getDescription());
                        }
                        String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?photoreference="+photoReference+"&sensor=false&maxheight=400&maxwidth=400&key="+ api_key +"";
                        System.out.println(imageUrl);
                        place.setImgURL(imageUrl);
                        if (!placesList.contains(place))
                            placesList.add(place);
                    }
                    for (int i = 0; i < placesList.size(); i++){
                        if(placesList.get(i).getCity() != city)
                            placesList.remove(i);
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

        }
    }

}
