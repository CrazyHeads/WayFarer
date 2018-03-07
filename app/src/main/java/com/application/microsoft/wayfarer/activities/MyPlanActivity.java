package com.application.microsoft.wayfarer.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.adapters.PlanViewAdapter;
import com.application.microsoft.wayfarer.models.Place;
import com.application.microsoft.wayfarer.models.Plan;
import com.application.microsoft.wayfarer.utils.ConnectionFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.http.GET;

public class MyPlanActivity  extends Activity
{
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    ArrayList<Plan> plans = new ArrayList<>();
    ListView listView;
    PlanViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);;
        listView = (ListView) findViewById(R.id.listView);
        new GetPlans().execute();

        adapter = new PlanViewAdapter(MyPlanActivity.this, plans);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MyPlanActivity.this,MapsActivity.class);
           // intent.putParcelableArrayListExtra("placesList",  plans.get(position));
          //  Intent intent = new Intent(MapsActivity.this,MapsActivity.class);
            startActivity(intent);
        });
    }

    public class GetPlans extends AsyncTask<String,String,String>
    {
        String flag = "";
        Boolean isSuccess = false;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyPlanActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            System.out.println("PostExecute!!");
            if (pDialog.isShowing())
                pDialog.dismiss();
            adapter.notifyDataSetChanged();
        }



        @Override

        protected String doInBackground(String... params) {
            try {
                    Connection con = ConnectionFactory.getConnection();
                    sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                if (con == null) {
                        flag = "Error in connection with SQL server";
                    }else{
                        String query = "select places,city,madeOn from trips where userId = '"+sharedPreferences.getString("UserID","")+"';";
                        System.out.println(query);
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        boolean flag = false;
                           while (rs.next()) {
                               System.out.println("HII");
                                Type type = new TypeToken<ArrayList<Place>>() {
                                }.getType();
                                ArrayList<Place> placesList = new Gson().fromJson(rs.getString("places"), type);
                                Plan plan = new Plan();
                                plan.setCity(rs.getString("city"));
                                plan.setPlanedPlaces(placesList);
                                plan.setMadeOn(rs.getDate("madeOn"));
                                plans.add(plan);
                                flag = true;
                            }
                        if(!flag) {
                            Toast.makeText(getApplicationContext(), "No Saved Plans ! Create a Plan", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MyPlanActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        rs.close();
                        stmt.close();
                        con.close();
                    }



                }catch (Exception ex)
                {
                    isSuccess = false;
                    flag = "Exceptions";
                    Log.e("ERROR", ex.getMessage());

                }

            return flag;
        }
    }
}
