package com.application.microsoft.wayfarer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.sql.SQLException;
import java.sql.Statement;


public class LoginActivity extends AppCompatActivity {
    Button click;
    Button select;
    private EditText et_email, et_password;
    private String email, password;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        click = (Button) findViewById(R.id.login);
        select = (Button) findViewById(R.id.signup);
        et_email = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
        sharedPreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

    }


    public void login(View v) {
        LoginActivity.DoLogin doLogin = new LoginActivity.DoLogin();
        doLogin.execute();
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
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
    }

    public boolean validate() {
        boolean valid = true;

        if(email.isEmpty() || !(email.equals("admin"))) {
            et_email.setError("Please Enter valid email!");
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
        String flag = "";
        Boolean isSuccess = false;
        String str_email =  et_email.getText().toString();
        String str_password = et_password.getText().toString();
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(isSuccess) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
            Toast.makeText(getApplicationContext(),flag,  Toast.LENGTH_LONG).show();
        }



        @Override

        protected String doInBackground(String... params) {
            if(str_password.trim().equals("") ||str_email.trim().equals("")){
                flag = "Please fill all the fields";
            }else{
                Connection con = ConnectionFactory.getConnection();
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    if (con == null) {
                        flag = "Error in connection with SQL server";
                    }else{
                        String query = "select userId,name from userDetails where password LIKE '"+str_password+"' and email LIKE '"+str_email+"';";
                        System.out.println(query);
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(query);
                        if(rs.next()){
                            flag = "Login Successful";
                            System.out.println("Done!");
                            isSuccess = true;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("UserID", rs.getString("userId"));
                            editor.putString("UserName", rs.getString("name"));
                            editor.apply();
                            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            System.out.println(sharedPreferences.getString("Plan",""));
                            //System.out.println(sharedPreferences.getString("City",""));
                            String query1 = "insert into trips(userId,places,city) values("+rs.getString("userId")+", '"+sharedPreferences.getString("Plan","")+"','"+sharedPreferences.getString("City","")+"');";
                            Statement stmt1 = con.createStatement();
                            int flag = stmt1.executeUpdate(query1);
                            if (flag < 1) {
                                System.out.println("Plan not Added!");
                            } else {
                                System.out.println("Added Plan!!");
                            }
                        }else{
                            flag = "Invalid Credentials";
                            System.out.println("Invalid!!");
                            isSuccess = false;
                        }
                    }
                }catch (Exception ex)
                {
                    isSuccess = false;
                    flag = "Exceptions";
                    Log.e("ERROR", ex.getMessage());

                } finally {
                    try {
                        rs.close();
                        stmt.close();
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return flag;
        }
    }
}


