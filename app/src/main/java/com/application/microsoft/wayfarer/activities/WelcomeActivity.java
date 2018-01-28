package com.application.microsoft.wayfarer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.application.microsoft.wayfarer.R;


public class WelcomeActivity extends AppCompatActivity {
    ImageView click;
    private static int SPLASH_TIME_OUT = 4300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

        click = (ImageView) findViewById(R.id.button);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_animation);
        click.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class
                ));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void show(View v) {
        Intent myIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(myIntent);
    }


}
