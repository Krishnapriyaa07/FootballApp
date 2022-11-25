package com.example.footballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen2 extends AppCompatActivity {
    Handler h=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen2.this,SplashScreen3.class);
                startActivity(i);
                finish();


            }
        },5000);




    }
}