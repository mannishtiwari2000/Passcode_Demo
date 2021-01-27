package com.main.passcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Success extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        }
}