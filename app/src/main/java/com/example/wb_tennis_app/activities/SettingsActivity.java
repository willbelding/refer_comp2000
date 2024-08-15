package com.example.wb_tennis_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wb_tennis_app.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("isDarkMode", false);
        setTheme(isDarkMode ? R.style.AppTheme_Scheme2 : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeSwitch = findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isDarkMode", isChecked);
                editor.apply();

                // Broadcast the theme change
                Intent intent = new Intent("com.example.wb_tennis_app.THEME_CHANGED");
                intent.addCategory(Intent.CATEGORY_DEFAULT);  // Add a category if necessary
                sendBroadcast(intent);

                // Restart the SettingsActivity itself to apply the new theme
                recreate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
