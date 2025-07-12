package com.huytran.traybotapp.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.huytran.traybotapp.R;
import com.huytran.traybotapp.page.HomeActivity;
import com.huytran.traybotapp.page.login.LoginActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    private final String filename = "Storage.txt";
    private final String filepath = "Super_mystery_folder";
    File myInternalFile;
    String username_tmp, password_tmp, islogin = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // check login
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
        myInternalFile = new File(directory, filename);
        if (check_is_login()) {
            checkUserData(username_tmp, password_tmp);
        } else {
            Log.d("wtf", "wtf");
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    // Login function
    public void checkUserData(String name, String password) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(name);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String passwordFromDB = snapshot.child(name).child("password").getValue(String.class);

                    if (Objects.equals(passwordFromDB, password)) {
                        try {
                            String data = name + "\n" + password + "\n" + "true";
                            FileOutputStream fos = new FileOutputStream(myInternalFile);
                            fos.write(data.getBytes());
                            fos.close();
                        } catch (IOException e) {
//                            e.printStackTrace();
                            if (e instanceof IOException) {

                            }
                        }

                        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                        boolean isIntroShown = prefs.getBoolean("isIntroShown", false);

                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public boolean check_is_login() {
        try {
            FileInputStream fis = new FileInputStream(myInternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i = 1;
            while ((strLine = br.readLine()) != null) {
                if (i == 1) username_tmp = strLine;
                if (i == 2) password_tmp = strLine;
                if (i == 3) islogin = strLine;
                i++;
            }
            in.close();
            return islogin.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}