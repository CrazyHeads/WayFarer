package com.application.microsoft.wayfarer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.satyavati.microsoft.R;

/**
 * Created by satyavati on 22-01-2018.
 */

public  class SignUpActivity extends AppCompatActivity {
    Button select;
    private EditText et_name, et_email, et_phone, et_password, et_confirm_password;
    private String name, email, phone, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        select = (Button) findViewById(R.id.button5);
        et_name = (EditText) findViewById(R.id.Name);
        et_email = (EditText) findViewById(R.id.email);
        et_phone = (EditText) findViewById(R.id.phone);
        et_password = (EditText) findViewById(R.id.password);
        et_confirm_password = (EditText) findViewById(R.id.confrim_password);
    }

    public void done(View v) {
        register();
       /* Intent myIntent = new Intent(SignUpActivity.this, MapActivity.class);
        startActivity(myIntent);
        */
    }

    public void register() {
         intialize();
        if(!validate()){
            Toast.makeText(this, "Signup has Failed", Toast.LENGTH_SHORT).show();
        } else {
            signupSuccess();
        }
    }

    public void signupSuccess() {
        Intent myIntent = new Intent(SignUpActivity.this, MapActivity.class);
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

        if(phone.isEmpty() || phone.length() > 10) {
            et_phone.setError("Please Enter valid phone number");
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
        phone = et_phone.getText().toString().trim();
        password = et_password.getText().toString().trim();
        confirm_password = et_confirm_password.getText().toString().trim();




    }
}

