package com.example.wb_tennis_app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

public class EditBookingActivity extends AppCompatActivity {

    private EditText editTextMemberName, editTextEmail, editTextPhoneNumber, editTextDate;
    private Spinner spinnerCourtType, spinnerCourtNo, spinnerDuration;
    private Button btnSaveChanges;
    private Booking bookingToEdit;

    // Define arrays for court numbers based on court types
    private final String[] grassCourts = {"1", "2", "3", "4"};
    private final String[] hardCourts = {"5", "6"};
    private final String[] artificialGrassCourts = {"7", "8", "9", "10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        // Initialize views
        editTextMemberName = findViewById(R.id.editTextMemberName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        spinnerCourtType = findViewById(R.id.spinnerCourtType);
        spinnerCourtNo = findViewById(R.id.spinnerCourtNo);
        editTextDate = findViewById(R.id.editTextDate);
        spinnerDuration = findViewById(R.id.spinnerDuration);
        btnSaveChanges = findViewById(R.id.buttonSaveChanges);

        // Get booking details from intent
        bookingToEdit = (Booking) getIntent().getSerializableExtra("booking");
        if (bookingToEdit != null) {
            populateFields(bookingToEdit);
        }

        // Set up the DatePicker for the editTextDate
        editTextDate.setInputType(0); // Disables keyboard input
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Set up listener for Court Type spinner to update Court No spinner
        spinnerCourtType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourtType = spinnerCourtType.getSelectedItem().toString();
                updateCourtNoSpinner(selectedCourtType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Save changes button click listener
        btnSaveChanges.setOnClickListener(view -> saveChanges());
    }

    private void populateFields(Booking booking) {
        editTextMemberName.setText(booking.getMemberName());
        editTextEmail.setText(booking.getEmail());
        editTextPhoneNumber.setText(booking.getPhoneNumber());

        // Set the spinner selection based on booking data
        setSpinnerSelection(spinnerCourtType, booking.getCourtType());
        editTextDate.setText(booking.getDate());
        setSpinnerSelection(spinnerDuration, booking.getDuration());

        // Update the Court No spinner based on the current Court Type
        updateCourtNoSpinner(booking.getCourtType());
        setSpinnerSelection(spinnerCourtNo, booking.getCourtNo());
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void updateCourtNoSpinner(String courtType) {
        ArrayAdapter<String> adapter;

        switch (courtType) {
            case "Grass":
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grassCourts);
                break;
            case "Hard":
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hardCourts);
                break;
            case "Artificial":
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, artificialGrassCourts);
                break;
            default:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{});
                break;
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourtNo.setAdapter(adapter);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditBookingActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Format the date and set it in the EditText
                        String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        editTextDate.setText(formattedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void saveChanges() {
        if (bookingToEdit == null) return; // No booking to edit

        // Retrieve user input
        String memberName = editTextMemberName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String courtType = spinnerCourtType.getSelectedItem().toString().trim();
        String courtNo = spinnerCourtNo.getSelectedItem().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String duration = spinnerDuration.getSelectedItem().toString().trim();

        // Validate input
        if (memberName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() ||
                courtType.isEmpty() || courtNo.isEmpty() || date.isEmpty() || duration.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields before saving changes.", Toast.LENGTH_SHORT).show();
            return; // Stop the editing process if any field is empty
        }

        // Update booking details
        bookingToEdit.setMemberName(memberName);
        bookingToEdit.setEmail(email);
        bookingToEdit.setPhoneNumber(phoneNumber);
        bookingToEdit.setCourtType(courtType);
        bookingToEdit.setCourtNo(courtNo);
        bookingToEdit.setDate(date);
        bookingToEdit.setDuration(duration);
        bookingToEdit.setDayOfWeek(getDayOfWeek(date));

        // Call API to update booking
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                BookingService bookingService = new BookingService();
                Call<BookingResponse> call = bookingService.updateBooking(bookingToEdit.getBookingNo(), bookingToEdit);

                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<BookingResponse> call, @NonNull Response<BookingResponse> response) {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> Toast.makeText(EditBookingActivity.this, "Booking updated!", Toast.LENGTH_SHORT).show());
                            finish(); // Close activity
                        } else {
                            runOnUiThread(() -> Toast.makeText(EditBookingActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BookingResponse> call, @NonNull Throwable t) {
                        runOnUiThread(() -> Toast.makeText(EditBookingActivity.this, "Network Error", Toast.LENGTH_SHORT).show());
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(EditBookingActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("EditBookingActivity", "Exception: " + e.getMessage(), e);
                });
            }
        });
    }

    private int getDayOfWeek(String dateString) {
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