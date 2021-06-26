package com.example.imagegallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView tv1,tv2;
    private Animation bottomAnim;
    private static int SPLASH_TIME_OUT=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv1=findViewById(R.id.tvHeadSplash);
        tv2 = findViewById(R.id.tvDescSplash);

        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        tv1.setAnimation(bottomAnim);
        tv2.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}