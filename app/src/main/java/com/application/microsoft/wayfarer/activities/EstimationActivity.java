package com.application.microsoft.wayfarer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.adapters.GridViewAdapter;
import com.application.microsoft.wayfarer.models.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * Created by satyavati on 22-01-2018.
 */

public class EstimationActivity extends AppCompatActivity {
    Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation);
        MainActivity mainActivity = new MainActivity();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapView);
//        mapFragment.getMapAsync(this);
//        GoogleMap mMap = null;
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<Place> selectedPlaces = (ArrayList<Place>) args.getSerializable("placesList");
        for (Place place : selectedPlaces) {
            if (place.getSelected()) {
                System.out.println(place.getName());
//                mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng()))
//                        .title(place.getName()));
            }
        }
        select = (Button) findViewById(R.id.button2);
    }

    public void save(View v) {
        Intent myIntent = new Intent(EstimationActivity.this, LoginActivity.class);
        startActivity(myIntent);

    }
}

