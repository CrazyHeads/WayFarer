package com.application.microsoft.wayfarer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.utils.ConnectionFactory;

import java.sql.ResultSet;
import java.sql.Statement;

import static com.application.microsoft.wayfarer.activities.LoginActivity.MyPREFERENCES;


/**
 * Created by satyavati on 22-01-2018.
 */

public  class SignUpActivity extends AppCompatActivity {
    Button select;
    SharedPreferences sharedpreferences;
    private EditText et_name, et_email, et_phone, et_password, et_confirm_password;
    private String name, email, phone, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        select = (Button) findViewById(R.id.button5);
        et_name = (EditText) findViewById(R.id.Name);
        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);
        et_confirm_password = (EditText) findViewById(R.id.confrim_password);
    }

    public void done(View v) {
       register();
       SignUpActivity.DoSignup doSignup = new SignUpActivity.DoSignup();
        doSignup.execute();


    }

    public void register() {
         intialize();
        if(!validate()){
            Toast.makeText(this, "Signup has Failed", Toast.LENGTH_SHORT).show();

        }
    }

    public void signupSuccess() {
        Intent myIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(myIntent);
    }

    public boolean validate() {
        boolean valid = true;

        if(name.isEmpty() || name.length() > 32) {
            et_name.setError("Please Enter valid name");
            valid = false;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Please Enter valid Email Address");
            valid = false;
        }

         if(password.isEmpty() || password.length() < 8) {
            et_password.setError("Please Enter valid password");
            valid = false;
        }

        if(confirm_password.isEmpty() || !(confirm_password.equals(password))) {
            et_confirm_password.setError("Please Enter same as password");
            valid = false;
        }

        return valid;
    }

    public void intialize() {

        name = et_name.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
        confirm_password = et_confirm_password.getText().toString().trim();



    }


    public class DoSignup extends AsyncTask<String,Void,String> {
        String z = "";
        Boolean isSuccess = false;
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String confirm_password = et_confirm_password.getText().toString().trim();


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(SignUpActivity.this, r, Toast.LENGTH_SHORT).show();

            if (isSuccess) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                    java.sql.Connection con = ConnectionFactory.getConnection();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                        System.out.println("Connection Error!!");
                    } else {
                        String query = "select * from userDetails where email = '" + email + "' and name = '" + name + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            z = "User Already Exists";
                            isSuccess = false;

                        } else {

                            int flag = stmt.executeUpdate("insert into userDetails(name, email, password) values('" + name + "','" + email + "','" + password + "');");
                            System.out.println(flag);
                            z = "SignUp successfull";
                            isSuccess = true;
                            System.out.println("Added user!!");
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                    Log.e("ERROR", ex.getMessage());

                }

            return z;
        }
    }
}

