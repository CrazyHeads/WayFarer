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
    CardView mycard, mycard1, mycard2 ;
    Intent i,i1,i2 ;
    LinearLayout ll,ll1,ll2;


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


       mycard = (CardView) findViewById(R.id.plan);
        i = new Intent(this,MainActivity.class);
        mycard.setOnClickListener(v -> startActivity(i));

        mycard1 = (CardView) findViewById(R.id.myPlan);
        i1 = new Intent(this,MyPlanActivity.class);
        mycard1.setOnClickListener(v -> startActivity(i1));

        mycard2 = (CardView) findViewById(R.id.feedback);
        i2 = new Intent(this,FeedbackActivity.class);
        mycard2.setOnClickListener(v -> startActivity(i2));
    }


}
