package com.example.wb_tennis_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wb_tennis_app.R;

public class MainMenuActivity extends AppCompatActivity {
    private Button profileButton;
    private Button bookingButton;
    private Button courtListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the saved theme before calling super.onCreate
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("isDarkMode", false);
        setTheme(isDarkMode ? R.style.AppTheme_Scheme2 : R.style.AppTheme);

        // Call the parent class's onCreate method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Initialize buttons
        courtListButton = findViewById(R.id.courtListButton);
        bookingButton = findViewById(R.id.bookingButton);
        profileButton = findViewById(R.id.profileButton);

        // Set OnClickListener for the profile button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the booking button
        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, BookingActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the court list button
        courtListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }
}