package com.example.wb_tennis_app.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wb_tennis_app.R;
import com.example.wb_tennis_app.models.Booking;
import com.example.wb_tennis_app.models.BookingResponse;
import com.example.wb_tennis_app.services.BookingService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {

    private Spinner spinnerCourtType, spinnerCourtNo, spinnerDuration;
    private EditText editTextDate, editTextTime;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        spinnerCourtType = findViewById(R.id.editTextCourtType);
        spinnerCourtNo = findViewById(R.id.editTextCourtNo);
        spinnerDuration = findViewById(R.id.editTextDuration);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        Button btnSubmitBooking = findViewById(R.id.buttonBookNow);

        // Initialize calendar instance
        calendar = Calendar.getInstance();

        // Set up the Spinners
        ArrayAdapter<CharSequence> courtTypeAdapter = ArrayAdapter.createFromResource(this, R.array.court_types, android.R.layout.simple_spinner_item);
        courtTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourtType.setAdapter(courtTypeAdapter);

        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(this, R.array.durations, android.R.layout.simple_spinner_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(durationAdapter);

        // Set up listener for Court Type Spinner to populate Court Number Spinner
        spinnerCourtType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateCourtNumberSpinner(spinnerCourtType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the DatePicker for the editTextDate
        editTextDate.setInputType(0); // Disables keyboard input
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // Set up the TimePicker for the editTextTime
        editTextTime.setInputType(0); // Disables keyboard input
        editTextTime.setOnClickListener(v -> showTimePickerDialog());

        btnSubmitBooking.setOnClickListener(view -> submitBooking());
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                BookingActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    calendar.set(year1, month1, dayOfMonth);
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                    editTextDate.setText(formattedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                BookingActivity.this,
                (view, hourOfDay, minute1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);
                    String formattedTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
                    editTextTime.setText(formattedTime);
                },
                hour, minute, true);
        timePickerDialog.show();
    }

    private void updateCourtNumberSpinner(String courtType) {
        String[] courtNumbers;
        switch (courtType) {
            case "Grass":
                courtNumbers = new String[]{"1", "2", "3", "4"};
                break;
            case "Hard":
                courtNumbers = new String[]{"5", "6"};
                break;
            case "Artificial Grass":
                courtNumbers = new String[]{"7", "8", "9", "10"};
                break;
            default:
                courtNumbers = new String[]{};
                break;
        }

        ArrayAdapter<String> courtNumberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courtNumbers);
        courtNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourtNo.setAdapter(courtNumberAdapter);
    }

    private void submitBooking() {
        // Retrieve user input
        String memberName = ((EditText) findViewById(R.id.editTextMemberName)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString().trim();
        String phoneNumber = ((EditText) findViewById(R.id.editTextPhoneNumber)).getText().toString().trim();
        String courtType = spinnerCourtType.getSelectedItem().toString();
        String courtNo = spinnerCourtNo.getSelectedItem().toString();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim(); // Added time input
        String duration = spinnerDuration.getSelectedItem().toString();

        // Validate input
        if (memberName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() ||
                courtType.isEmpty() || courtNo.isEmpty() || date.isEmpty() || time.isEmpty() || duration.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields before booking.", Toast.LENGTH_SHORT).show();
            return; // Stop the booking process if any field is empty
        }

        // Combine date and time into a single string for the booking
        String dateTime = date + "T" + time + ":00";

        // Automatically retrieve the account number from SharedPreferences
        String accountNo = getUserAccountNumber();
        if (accountNo.isEmpty()) {
            Toast.makeText(this, "User account number is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate day of the week
        int dayOfWeek = getDayOfWeek(date);

        Booking booking = new Booking(0, memberName, accountNo, email, phoneNumber, courtType, courtNo, dateTime, dayOfWeek, duration);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                BookingService bookingService = new BookingService();
                Call<BookingResponse> call = bookingService.createBooking(booking);

                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<BookingResponse> call, @NonNull Response<BookingResponse> response) {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> Toast.makeText(BookingActivity.this, "Booking successful!", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(BookingActivity.this, "Booking failed: " + response.message(), Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BookingResponse> call, @NonNull Throwable t) {
                        runOnUiThread(() -> {
                            Toast.makeText(BookingActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            Log.e("BookingActivity", "Error: " + t.getMessage(), t);
                        });
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(BookingActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("BookingActivity", "Exception: " + e.getMessage(), e);
                });
            }
        });
    }

    private String getUserAccountNumber() {
        // Retrieve the account number from SharedPreferences
        return getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("account_no", "");
    }

    private int getDayOfWeek(String dateString) {
        // Logic to calculate the day of the week from a date string
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(sdf.parse(dateString)));
            return calendar.get(Calendar.DAY_OF_WEEK) - 1; // 0 = Sunday, 6 = Saturday
        } catch (Exception e) {
            return -1; // Error case
        }
    }
}
