package com.example.wb_tennis_app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wb_tennis_app.R;

public class NotificationsActivity extends AppCompatActivity {
    private Switch notificationsSwitch;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNotificationsEnabled = notificationsSwitch.isChecked();
                // Handle saving notification settings here
            }
        });
    }
}
