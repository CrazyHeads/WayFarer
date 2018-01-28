package com.application.microsoft.wayfarer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        register();
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
        Intent myIntent = new Intent(LoginActivity.this, MapActivity.class);
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
        Intent myIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(myIntent);

    }
}