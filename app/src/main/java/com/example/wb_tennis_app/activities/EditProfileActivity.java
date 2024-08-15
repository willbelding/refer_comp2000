package com.example.wb_tennis_app.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wb_tennis_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText dateOfBirthEditText;
    private EditText playerStyleEditText;
    private Button saveButton;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI components
        fullNameEditText = findViewById(R.id.fullName);
        emailEditText = findViewById(R.id.email);
        dateOfBirthEditText = findViewById(R.id.dateOfBirth);
        playerStyleEditText = findViewById(R.id.playerStyle);
        saveButton = findViewById(R.id.saveButton);

        // Setup SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Initialize calendar
        calendar = Calendar.getInstance();

        // Set up DatePicker for date of birth field
        dateOfBirthEditText.setInputType(InputType.TYPE_NULL);  // Prevent manual input
        dateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Load saved profile details
        loadProfileDetails();

        // Enable edit mode initially
        toggleEditMode(true);

        // Set up save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileDetails();
            }
        });
    }

    private void showDatePicker() {
        new DatePickerDialog(EditProfileActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateOfBirthEditText();
        }
    };

    private void updateDateOfBirthEditText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateOfBirthEditText.setText(sdf.format(calendar.getTime()));
    }

    private void loadProfileDetails() {
        String accountNo = sharedPreferences.getString("account_no", "");
        if (!accountNo.isEmpty()) {
            // Fetch profile details from a data source using accountNo
            String fullName = sharedPreferences.getString(accountNo + "_fullName", "");
            String email = sharedPreferences.getString(accountNo + "_email", "");
            String dateOfBirth = sharedPreferences.getString(accountNo + "_dateOfBirth", "");
            String playerStyle = sharedPreferences.getString(accountNo + "_playerStyle", "");

            fullNameEditText.setText(fullName);
            emailEditText.setText(email);
            dateOfBirthEditText.setText(dateOfBirth);
            playerStyleEditText.setText(playerStyle);
        }
    }

    private void saveProfileDetails() {
        String accountNo = sharedPreferences.getString("account_no", "");
        if (!accountNo.isEmpty()) {
            String fullName = fullNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String dateOfBirth = dateOfBirthEditText.getText().toString();
            String playerStyle = playerStyleEditText.getText().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(accountNo + "_fullName", fullName);
            editor.putString(accountNo + "_email", email);
            editor.putString(accountNo + "_dateOfBirth", dateOfBirth);
            editor.putString(accountNo + "_playerStyle", playerStyle);
            editor.apply();

            // Optionally, you can show a confirmation or toast message here

            // Switch back to view mode
            toggleEditMode(false);
        }
    }

    private void toggleEditMode(boolean enable) {
        fullNameEditText.setEnabled(enable);
        emailEditText.setEnabled(enable);
        dateOfBirthEditText.setEnabled(enable);
        playerStyleEditText.setEnabled(enable);

        saveButton.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
}
