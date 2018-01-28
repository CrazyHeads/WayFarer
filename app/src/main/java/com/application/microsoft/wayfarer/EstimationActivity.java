package com.application.microsoft.wayfarer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.satyavati.microsoft.R;

/**
 * Created by satyavati on 22-01-2018.
 */

public class EstimationActivity extends AppCompatActivity {
    Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation);

        select = (Button) findViewById(R.id.button2);
    }

    public void save(View v) {
        Intent myIntent = new Intent(EstimationActivity.this, LoginActivity.class);
        startActivity(myIntent);

    }
}

