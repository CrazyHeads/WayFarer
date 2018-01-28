package com.application.microsoft.wayfarer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.satyavati.microsoft.R;


public class MainActivity extends AppCompatActivity {
    Button click;
    Button press;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.statement);
        click = (Button) findViewById(R.id.btn);
        press = (Button) findViewById(R.id.button);
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }

    //This function that i have mentioned in the
    // .xml file of activity_main.xml android:onClick="show". and as well as AndroidMainfest.xml
    // also we made changes.
    private Spinner spinner1 ;
    //private Button btnSubmit; In the future when places are selected
    public void show(View v) {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(myIntent);

    }

    public void plan(View v) {
        Intent myIntent = new Intent(MainActivity.this, EstimationActivity.class);
        startActivity(myIntent);

    }
    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
      //  spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    // get the selected dropdown list value
    public void addListenerOnButton() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
       // btnSubmit = (Button) findViewById(R.id.btnSubmit);
       /* btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(MyAndroidAppActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
                                "\nSpinner 2 : "+ String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
        */
    }



}
