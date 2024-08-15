package com.example.wb_tennis_app.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wb_tennis_app.R;
import com.example.wb_tennis_app.UserManagement.UserDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonRegister;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.etUsername);
        editTextPassword = findViewById(R.id.etPassword);
        buttonRegister = findViewById(R.id.etSubmit);
        dbHelper = new UserDatabaseHelper(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isUsernameTaken(username)) {
                    Toast.makeText(RegisterActivity.this, "Registration failed, username might already be in use", Toast.LENGTH_SHORT).show();
                } else {
                    registerNewUser(username, password);
                }
            }
        });
    }

    private boolean isUsernameTaken(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "username=?", new String[]{username}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    private void registerNewUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        String accountNo = generateAccountNumber(username);
        values.put("account_no", accountNo);  // Generate a unique account number based on username

        long newRowId = db.insert("users", null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        } else {
            // Save account number in SharedPreferences
            getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("account_no", accountNo)
                    .apply();

            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private String generateAccountNumber(String username) {
        return "NO" + username.hashCode();
    }
}