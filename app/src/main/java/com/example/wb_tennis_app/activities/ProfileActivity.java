package com.example.wb_tennis_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.wb_tennis_app.R;
import androidx.appcompat.app.AppCompatActivity;


public class ProfileActivity extends AppCompatActivity {
    private Button manageButton;
    private Button myprofileButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        manageButton = findViewById(R.id.manageButton);
        myprofileButton = findViewById(R.id.myprofileButton);
        settingsButton = findViewById(R.id.settingsButton);

        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ManageBookingsActivity.class);
                startActivity(intent);
            }
        });

        myprofileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}