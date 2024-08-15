package com.example.wb_tennis_app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wb_tennis_app.R;
import com.example.wb_tennis_app.adapters.BookingAdapter;
import com.example.wb_tennis_app.models.Booking;
import com.example.wb_tennis_app.services.BookingService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBookingsActivity extends AppCompatActivity implements BookingAdapter.OnBookingClickListener {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

        recyclerView = findViewById(R.id.recyclerViewBookings);
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookingAdapter);

        fetchBookings();
    }

    private void fetchBookings() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                // Get the account number from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String accountNo = sharedPreferences.getString("account_no", null);

                if (accountNo == null) {
                    runOnUiThread(() -> Toast.makeText(ManageBookingsActivity.this, "Error: Account number not found.", Toast.LENGTH_SHORT).show());
                    return;
                }

                BookingService bookingService = new BookingService();
                Call<List<Booking>> call = bookingService.getAllBookings();

                call.enqueue(new Callback<List<Booking>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Booking>> call, @NonNull Response<List<Booking>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            bookingList.clear();

                            // Filter the bookings by the user's account number
                            for (Booking booking : response.body()) {
                                if (booking.getAccountNo().equals(accountNo)) {
                                    bookingList.add(booking);
                                }
                            }

                            // Notify the adapter of the changes
                            bookingAdapter.notifyDataSetChanged();
                        } else {
                            runOnUiThread(() -> Toast.makeText(ManageBookingsActivity.this, "Failed to load bookings", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Booking>> call, @NonNull Throwable t) {
                        runOnUiThread(() -> {
                            Toast.makeText(ManageBookingsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            Log.e("ManageBookingsActivity", "Error: " + t.getMessage(), t);
                        });
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(ManageBookingsActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ManageBookingsActivity", "Exception: " + e.getMessage(), e);
                });
            }
        });
    }

    @Override
    public void onEditClick(Booking booking) {
        // Implement logic to update booking details
        // This could involve opening a dialog or new activity to edit the booking
    }

    @Override
    public void onDeleteClick(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Booking")
                .setMessage("Are you sure you want to delete this booking?")
                .setPositiveButton("Yes", (dialog, which) -> deleteBooking(booking))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteBooking(Booking booking) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                BookingService bookingService = new BookingService();
                Call<Void> call = bookingService.deleteBooking(booking.getBookingNo());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                bookingList.remove(booking);
                                bookingAdapter.notifyDataSetChanged();
                                Toast.makeText(ManageBookingsActivity.this, "Booking deleted", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(ManageBookingsActivity.this, "Failed to delete booking", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        runOnUiThread(() -> {
                            Toast.makeText(ManageBookingsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            Log.e("ManageBookingsActivity", "Error: " + t.getMessage(), t);
                        });
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(ManageBookingsActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ManageBookingsActivity", "Exception: " + e.getMessage(), e);
                });
            }
        });
    }
}