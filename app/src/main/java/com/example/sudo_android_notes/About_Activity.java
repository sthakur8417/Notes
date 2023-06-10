package com.example.sudo_android_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class About_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
    }
}