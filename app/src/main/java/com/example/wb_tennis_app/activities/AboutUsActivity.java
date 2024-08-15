package com.example.wb_tennis_app.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wb_tennis_app.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("About Us");
        }
    }
}