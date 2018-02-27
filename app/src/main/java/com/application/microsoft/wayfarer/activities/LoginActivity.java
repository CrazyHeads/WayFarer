package com.application.microsoft.wayfarer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.tv.TvInputService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginActivity extends AppCompatActivity {
    Button click;
    Button select;
    private EditText et_username, et_password;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        click = (Button) findViewById(R.id.button4);
        select = (Button) findViewById(R.id.button3);
        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);

    }

    //This function that i have mentioned in the
    // .xml file of activity_main.xml android:onClick="show". and as well as AndroidMainfest.xml
    // also we made changes.
    public void login(View v) {
        //register();
        LoginActivity.DoLogin doLogin = new LoginActivity.DoLogin();
        doLogin.execute();


        /*Intent myIntent = new Intent(LoginActivity.this, MapActivity.class);
        startActivity(myIntent);
        */
    }

    public void register() {
        intilize();
        if(!validate()) {
            Toast.makeText(this, "Log in has Failed", Toast.LENGTH_SHORT).show();
        } else {
            onLoginSuccess();
        }
    }

    public void onLoginSuccess() {
        Intent myIntent = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(myIntent);
    }

    public void intilize() {
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
    }

    public boolean validate() {
        boolean valid = true;

        if(username.isEmpty() || !(username.equals("admin"))) {
            et_username.setError("Please Enter valid username");
            valid = false;
        }

        if (password.isEmpty() || !(password.equals("12345678")) || password.length() < 8) {
            et_password.setError("Please Enter valid password");
            valid = false;
        }

        return valid;
    }

    public void signup(View v) {
        Intent myIntent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(myIntent);

    }
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        String role, cd;
        Boolean isSuccess = false;
        String str_username =  et_username.getText().toString();
        String str_password = et_password.getText().toString();



        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            // Toast.makeText(this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent i = new Intent("com.application.microsoft.wayfarer.activities.WelcomeActivity");
                startActivity(i);
                finish();
            }



        }



        @Override

        protected String doInBackground(String... params) {
            if(str_password.trim().equals("") ||str_username.trim().equals("")){
                z = "Please fill all the fields";
            }else{
                try {
                    Connection con = ConnectionFactory.getConnection();

                    if (con == null) {
                        z = "Error in connection with SQL server";
                    }else{
                        String query = "select * from userDetails where name LIKE 'Shravani';";
                        System.out.println(query);
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next()){
                            z = "Login Successful";
                            isSuccess = true;
                        }else{
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }
//                        rs.close();
//                        stmt.close();
//                        con.close();
                    }



                }catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                    Log.e("ERROR", ex.getMessage());

                }
            }
            return z;
        }
    }
}


