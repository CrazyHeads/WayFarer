package com.application.microsoft.wayfarer.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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

public class MyPlanActivity extends Activity {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    ArrayList<Plan> plans = new ArrayList<>();
    ListView listView;
    PlanViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);
        ;
        listView = (ListView) findViewById(R.id.listView);
        new GetPlans().execute();

        adapter = new PlanViewAdapter(MyPlanActivity.this, plans);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MyPlanActivity.this, MapsActivity.class);
            intent.putParcelableArrayListExtra("placesList", plans.get(position).getPlanedPlaces());
            startActivity(intent);
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_menu, menu);

                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

                // TODO  Auto-generated method stub

                switch (item.getItemId()) {

                    case R.id.selectAll:
                        final int checkedCount = plans.size();
                        adapter.removeSelection();

                        for (int i = 0; i < checkedCount; i++) {

                            listView.setItemChecked(i, true);
                        }

                        mode.setTitle(checkedCount + "  Selected");

                        return true;

                    case R.id.delete:

                        AlertDialog.Builder builder = new AlertDialog.Builder(MyPlanActivity.this);
                        builder.setMessage("Do you  want to delete selected record(s)?");
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                            }

                        });

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                            @Override

                            public void onClick(DialogInterface dialog, int which) {

                                // TODO  Auto-generated method stub

                                SparseBooleanArray selected = adapter

                                        .getSelectedIds();

                                for (int i = (selected.size() - 1); i >= 0; i--) {

                                    if (selected.valueAt(i)) {
                                        Plan selecteditem = adapter.getItem(selected.keyAt(i));
                                        adapter.remove(selecteditem);


                                    }

                                }

                                mode.finish();
                                selected.clear();


                            }

                        });

                        AlertDialog alert = builder.create();

                        alert.setTitle("Confirmation");

                        alert.show();

                        return true;

                    default:

                        return false;

                }
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + "  Selected");
                adapter.toggleSelection(position);

            }

        });


    }

    public class GetPlans extends AsyncTask<String, String, String> {
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
                } else {
                    String query = "select places,city,madeOn from trips where userId = '" + sharedPreferences.getString("UserID", "") + "';";
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
                    if (!flag) {
                        Toast.makeText(getApplicationContext(), "No Saved Plans ! Create a Plan", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MyPlanActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    rs.close();
                    stmt.close();
                    con.close();
                }


            } catch (Exception ex) {
                isSuccess = false;
                flag = "Exceptions";
                Log.e("ERROR", ex.getMessage());

            }

            return flag;
        }
    }
}
