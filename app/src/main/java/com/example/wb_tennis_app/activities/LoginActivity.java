package com.example.wb_tennis_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wb_tennis_app.R;
import com.example.wb_tennis_app.UserManagement.UserRepository;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        // Initialize UserRepository
        userRepository = new UserRepository(this);
        userRepository.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userRepository != null) {
            userRepository.close(); // Close the database connection when the activity is destroyed
        }
    }

    public void loginUser(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = null;
        try {
            cursor = userRepository.getUserByUsername(username);

            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("account_no")); // Assuming "account_no" is the correct column name for account number

                if (storedPassword.equals(password)) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                    // Save the account number to SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("account_no", accountNo);
                    editor.apply();

                    // Start MainMenuActivity
                    Intent intent = new Intent(this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();  // Close LoginActivity
                } else {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            }
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure cursor is closed to prevent memory leaks
            }
        }
    }
}