package com.application.microsoft.wayfarer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

import com.application.microsoft.wayfarer.R;


public class MenuActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    CardView mycard ;
    Intent i ;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name  = sharedPreferences.getString("UserID","");
        if(name.equals("")){
            System.out.println("Invalid!!");

        }
        System.out.println(name);


        ll = (LinearLayout) findViewById(R.id.planLL);
        mycard = (CardView) findViewById(R.id.plan);
        i = new Intent(this,MainActivity.class);
        mycard.setOnClickListener(v -> startActivity(i));

//        ll = (LinearLayout) findViewById(R.id.myPlanLL);
//        mycard = (CardView) findViewById(R.id.myPlan);
//        i = new Intent(this,MyPlanActivity.class);
//        mycard.setOnClickListener(v -> startActivity(i));
//
//        ll = (LinearLayout) findViewById(R.id.feedbackLL);
//        mycard = (CardView) findViewById(R.id.feedback);
//        i = new Intent(this,MenuActivity.class);
//        mycard.setOnClickListener(v -> startActivity(i));

    }


}
