package com.huytran.traybotapp.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.huytran.traybotapp.R;
import com.huytran.traybotapp.page.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}