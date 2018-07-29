package com.application.microsoft.wayfarer.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Filter;
import com.application.microsoft.wayfarer.R;



import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.adapters.ListViewAdapter;
import com.application.microsoft.wayfarer.adapters.TransparentProgressDialog;
import com.application.microsoft.wayfarer.exception.GooglePlacesException;
import com.application.microsoft.wayfarer.handlers.HttpHandler;
import com.application.microsoft.wayfarer.adapters.CitiesAutoCompleteAdapter;
import com.application.microsoft.wayfarer.models.Place;
import com.application.microsoft.wayfarer.utils.CityAPI;
import com.application.microsoft.wayfarer.utils.GifImageView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String TAG = MainActivity.class.getSimpleName();
    private TransparentProgressDialog pDialog;
    private ListView listView;
    private ListViewAdapter listAdapter;
    private int i = 0;
    private String city = "";
    AutoCompleteTextView autoCompView;
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static  ArrayList<String> listOfKeys = new ArrayList<String>(Arrays.asList(
            "AIzaSyB_MKmWEjS9IuAxSvl0-H7145EoWdwlWP0",
            "AIzaSyBG4_iI8Ukq0eoBrEZxR2sFDtPCH09kkL8",
            "AIzaSyCiaLGlljuLkLombPcv0RGXw_Tpit9KbbE",
            "AIzaSyDpTC7gSRLeCq3dbjBeOgasnCqvfdNhkT0",
            "AIzaSyCi9z5JsxnkjNLUimfpsQj-43yM653a_Dg",
            "AIzaSyBWpF2dQ64Xw7cYevkEkHf6dY536VEFZAA",
            "AIzaSyB6382PsNOmIiD70laaSCIYJNb7pUkmkH4",
            "AIzaSyBnq6HpzvkR4J2X9d0dgC56fxsHrsPs1rM",
            "AIzaSyANKg3XWWULaLTGbsO2A5krC7tDoQgJ8RA",
            "AIzaSyCt6VuMs_JrUqAcFwn70UtZp4pfLntUivI",
            "AIzaSyDUUBHfckNZX5kcVYv8bPXnaCaYLjxvX-8",
            "AIzaSyDYp78kpJERRjh6tfAo47fVuA28hdiw8Jw",
            "AIzaSyDG7S40R4SgClQX9Zbm59W9ctYocGEWR4A",
            "AIzaSyD5kgC_dBf_jOo-p1Is3YIQ-xIUgWeKWRw",
            "AIzaSyAlZniTEl_tErWaqg5irbIxSkim3BZQDRU",
            "AIzaSyBbAugBoZdYCVHYBOSL8Gtd76G2a_V4JPo",
            "AIzaSyCEBCEvu9Z0Jhzm_WLdgkpUCZdSkcHmrXg"

    ));
    // listOfKeys.add("AIzaSyCiaLGlljuLkLombPcv0RGXw_Tpit9KbbE");
    private static String API_KEY = "AIzaSyDkM0-WNudAA2S03EJDzC7xOfCCfo8jgDM";
    // private  static String API_KEY =  "AIzaSyDpTC7gSRLeCq3dbjBeOgasnCqvfdNhkT0"; //"AIzaSyAtmnpdgVvHyYyoILWHGzwqt_ePtrGmalk";
    // private static String API_KEY = "AIzaSyCy5fDtto3nCzohU5BSVe3MQlKjA0PJ-0E";
    String[] cities;
    ArrayList<Place> placesList;
    int index;
    String PLACES_OF_INTEREST_URL = "";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    final Context mContext = this;
    private Button mButton;
    boolean isException = false;
    private Handler mThreadHandler;
    private GooglePlacesAutocompleteAdapter mAdapter;
    private HandlerThread mHandlerThread;


    public ArrayList<Place> getPlacesList() {
        return placesList;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placesList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name  = sharedPreferences.getString("UserID","");
        /* Making the plan ,addLocation button and Starting Location invisible until the user selects a city*/
        if(!name.equals("")){
            Button button =(Button) findViewById(R.id.btn);
            button.setVisibility(View.INVISIBLE);
        }

        AutoCompleteTextView autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autocompleteView.setAdapter(new CitiesAutoCompleteAdapter(this, R.layout.autocomplete_list_item));

        placesList.clear();
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteText);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);
        listAdapter = new ListViewAdapter(this, R.layout.row,placesList);

        Toast.makeText(getApplicationContext(),"Please select a city",  Toast.LENGTH_LONG).show();

        autocompleteView.setOnItemClickListener ((parent, view, position, id) -> {
            // Get data associated with the specified position
            // in the list (AdapterView)
            index = position;

            city =  parent.getItemAtPosition(position).toString();
            System.out.println(index+ "" + city);
            /* If the selected City is greater than zero, then taking the places of interest from the url*/
            if(index >= 0) {
                hideKeyboard(MainActivity.this);
                placesList.clear();
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteText) ;
                autoCompleteTextView.setVisibility(View.VISIBLE);
                Button button =(Button) findViewById(R.id.button);
                button.setVisibility(View.VISIBLE);
                Button button1 =(Button) findViewById(R.id.plus_button);
                button1.setVisibility(View.VISIBLE);
                PLACES_OF_INTEREST_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+point+of+interest&language=en&key=" + API_KEY + "";
//                listView.clearAnimation();
//                listAdapter.clear();
                new GetPlaces().execute();
                listAdapter.addAll(placesList);
                listView = (ListView) findViewById(R.id.listView);
                listView.invalidateViews();
                listAdapter.notifyDataSetChanged();
                listView.setAdapter(listAdapter);
                System.out.println(city);

                Toast.makeText(getApplicationContext(), "Selected: " + city, Toast.LENGTH_LONG).show();
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("City", city);
            editor.apply();
            index = -1;
        });


        mButton = (Button) findViewById(R.id.plus_button);
        /*If plus button is clicked,then Add Location dialog is displayed where user can add custom location */
        mButton.setOnClickListener(arg0 -> {
            LayoutInflater li = LayoutInflater.from(mContext);
            View dialogView = li.inflate(R.layout.custom_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    mContext);
            alertDialogBuilder.setTitle("Add Location");
            alertDialogBuilder.setView(dialogView);
            final EditText userInput = (EditText) dialogView.findViewById(R.id.autoCompleteTextView);
            AutoCompleteTextView actv1 = (AutoCompleteTextView) dialogView.findViewById(R.id.autoCompleteTextView);
            actv1.setAdapter(new GooglePlacesAutocompleteAdapter(mContext, R.layout.list_item));
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Add",null)
                    .setNegativeButton("Done",
                            (dialog, id) -> {
                                dialog.dismiss();
                                hideKeyboard(MainActivity.this);
                            });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
            Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            /* Once the user adds the custom location,a message is displayed*/
            b.setOnClickListener(view -> {
                String location = actv1.getText().toString();
                if(!location.equals("") && location.contains(city)){
                    Place place = new Place();
                    place.setName(location);
                    place.setLat(getLocationFromAddress(mContext,location).latitude);
                    place.setLng(getLocationFromAddress(mContext,location).longitude);
                    place.setCity(city);
                    place.setSelected(true);
                    placesList.add(0,place);
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Added your location" + location, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a location in the city selected!", Toast.LENGTH_LONG).show();
                }
                actv1.getText().clear();
            });
        });



    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /* Giving the autoCompletePlace in the start Location and custom Location */
    @SuppressLint("LongLogTag")
    public ArrayList autocompletePlace(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:ind");
            sb.append("&components=locality:" + city.toLowerCase());
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                String place = predsJsonArray.getJSONObject(i).getString("description");
                if (place.contains(city.split(",")[0]))
                    resultList.add(place);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }




    /* Taking the postions of the selected places*/
    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        hideKeyboard(MainActivity.this);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }



    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = autocompletePlace(constraint.toString());
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }


                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
    /* In this method,the selected places are transferred to PlanActivity*/
    public void plan(View v) {
        Intent intent = new Intent(MainActivity.this,PlanActivity.class);
        intent.putParcelableArrayListExtra("placesList", placesList);
        String address = autoCompView.getText().toString();
        System.out.println(address + " " + city);
        if(address != null &&  address.contains(city) ){
            Place p = new Place();
            p.setName(address);
            LatLng latLng = getLocationFromAddress(this, address);
            p.setLat(latLng.latitude);
            p.setLng(latLng.longitude);
            p.setCity(city);
            p.setSelected(true);
            String IMAGE_URL = "http://leeford.in/wp-content/uploads/2017/09/image-not-found.jpg";
            p.setImgURL(IMAGE_URL);
            placesList.add(0,p);
            startActivity(intent);

        } else if (placesList.size() <= 1)
            Toast.makeText(getApplicationContext(),"Please select places to Visit",  Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),"Please select a starting location in the city selected!",  Toast.LENGTH_LONG).show();
    }
    /* Login Icon*/

    public void loginIcon(View v) {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(myIntent);

    }


    @SuppressLint("StaticFieldLeak")

    private class GetPlaces extends AsyncTask<Void, Void, Void> {

          @SuppressLint("SetTextI18n")
          @Override
        protected void onPreExecute() {
              LayoutInflater li = LayoutInflater.from(mContext);
              @SuppressLint("InflateParams") View dialogView = li.inflate(R.layout.progress_diaglog, null);
              final ImageView imgView = (ImageView) dialogView.findViewById(R.id.imageView);
              Glide.with(MainActivity.this).load(R.drawable.search_places).into(new GlideDrawableImageViewTarget(imgView));
              TextView tv = (TextView) dialogView.findViewById(R.id.textView);
              tv.setText("Searching for Places of Interests....");
              pDialog = new TransparentProgressDialog(MainActivity.this, R.drawable.search_places);
              for(int i = 0; i < 1000; i++);
//              Ion.with(imgView).load("https://cdn.dribbble.com/users/127072/screenshots/1582404/pin-eye2.gif");
              pDialog.setContentView(dialogView);
//              pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
              pDialog.show();
        }
        /*  Getting  the places data from url */

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(PLACES_OF_INTEREST_URL);
            Log.e(TAG, "Response from THE PLACES_OF_INTEREST_URL: " + jsonStr);
            if (jsonStr != null) {
                try {
                    System.out.println(PLACES_OF_INTEREST_URL);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String statusCode = jsonObj.getString("status");
                    System.out.println("Status: " +statusCode);
                    System.out.println(isException);
                    if (statusCode.equals("REQUEST_DENIED")) {
                        isException = true;
                    }
                    System.out.println("WHAT"+isException);


                    JSONArray jsonArray = jsonObj.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Place place = new Place();
                        String photoReference;
                        place.setCity(city);
                        place.setID(object.getString("place_id"));
                        //String placeStr = sh.makeServiceCall("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place.getID()+"&key="+API_KEY+"");
                        String placeStr = sh.makeServiceCall(PLACES_API_BASE + "/details/json?placeid=" + place.getID() + "&key=" + API_KEY + "");
                        JSONObject jsonObj1 = new JSONObject(placeStr);
                        // statusCode = jsonObj1.getString("Status");
                        //  System.out.println("Status: " +statusCode);

                        if (jsonObj1.getJSONObject("result").has("reviews")) {
                            place.setDescription(jsonObj1.getJSONObject("result").getJSONArray("reviews").getJSONObject(0).getString("text").split("\n")[0]);
                        } else {
                            place.setDescription("Not Available!");
                        }
                            place.setName(object.getString("name"));
                        place.setCity(city);
                        JSONObject geometry = object.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        place.setLat(location.getDouble("lat"));
                        place.setLng(location.getDouble("lng"));
                        String IMAGE_URL;
                        if (object.has("photos")){
                            JSONArray photos = object.getJSONArray("photos");
                            JSONObject photoReferenceUrl = photos.getJSONObject(0);
                            photoReference = photoReferenceUrl.getString("photo_reference");
                            IMAGE_URL = PLACES_API_BASE + "/photo?maxwidth=1000&photoreference=" + photoReference + "&sensor=false&key=" + API_KEY + "";

                        } else {
                            IMAGE_URL = "http://leeford.in/wp-content/uploads/2017/09/image-not-found.jpg";

                        }

                        place.setImgURL(IMAGE_URL);
                        place.setSelected(false);
                        if (!placesList.contains(place))
                            placesList.add(place);
                    }
                    for (int i = 0; i < placesList.size(); i++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (!Objects.equals(placesList.get(i).getCity(), city))
                                placesList.remove(i);
                        }
                    }
                    System.out.println("Done!!");
                    isException = false;
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    isException = true;
                    runOnUiThread(new Runnable() {
                        @Override

                        public void run() {
                            /*Toast.makeText(getApplicationContext(),
                                   "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                   .show();*/
                            System.out.println("Exception!!");
                            isException = true;

                        }
                    });

                } catch (GooglePlacesException e) {
                    isException = true;

                }

            }else {
                isException = true;
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
            System.out.println("PostExecute!!" +isException);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(isException){
                listOfKeys.remove(0);
                listOfKeys.add(listOfKeys.size() - 1, API_KEY);
                API_KEY = listOfKeys.get(0);
                System.out.println("HELLO" + API_KEY);
                isException = false;
                System.out.println("Entered");
                new GetPlaces().execute();
                placesList.clear();
            }

            listAdapter.notifyDataSetChanged();

        }

    }
    /* Getting the location using Latitude and Longitude */
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

}
