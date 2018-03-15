package com.application.microsoft.wayfarer.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.utils.ConnectionFactory;

import java.sql.SQLException;
import java.sql.Statement;


public class FeedbackActivity extends AppCompatActivity {
    private EditText et_msg;
    private String msg;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    public void onSubmit(View v) {
        //  Intent myIntent = new Intent(FeedbackActivity.this, MenuActivity.class);
        //  startActivity(myIntent);
        et_msg = (EditText) findViewById(R.id.message);
        msg = et_msg.getText().toString().trim();
        SaveFeedBack saveFeedBack = new SaveFeedBack();
        saveFeedBack.execute();

    }

    public class SaveFeedBack extends AsyncTask<String, Void, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(FeedbackActivity.this, "Thankyou for your feedback", Toast.LENGTH_SHORT).show();

            if (isSuccess) {
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            java.sql.Connection con = ConnectionFactory.getConnection();
            try {
                if (con == null) {
                    z = "Error in connection with SQL server";
                    System.out.println("Connection Error!!");
                }
                int userId = Integer.parseInt(sharedPreferences.getString("UserID", ""));
                Statement stmt = null;
                try {
                    stmt = con.createStatement();
                    System.out.println(userId);
                    int flag = stmt.executeUpdate("insert into feedback(userId, msg) values('" + userId + "','" + msg + "');");
                    System.out.println(flag);
                    z = "FeedBack done";
                    isSuccess = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    stmt.close();
                }

            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
                Log.e("ERROR", ex.getMessage());
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return z;
        }
    }
}


