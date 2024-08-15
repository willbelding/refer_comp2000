package com.example.wb_tennis_app.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wb_tennis_app.R;
import com.example.wb_tennis_app.activities.EditBookingActivity;
import com.example.wb_tennis_app.models.Booking;

import java.util.Locale;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private OnBookingClickListener listener;

    public interface OnBookingClickListener {
        void onEditClick(Booking booking);
        void onDeleteClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookings, OnBookingClickListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking, listener);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewBookingInfo;
        private Button buttonEdit, buttonDelete;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookingInfo = itemView.findViewById(R.id.textViewBookingInfo);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(Booking booking, OnBookingClickListener listener) {
            String bookingDetails = String.format(Locale.ENGLISH,
                    "Court Type: %s\nCourt No: %s\nDate: %s\nDuration: %s\nMember Name: %s\nEmail: %s\nPhone: %s\nAccount No: %s\nDay of Week: %d",
                    booking.getCourtType(),
                    booking.getCourtNo(),
                    booking.getDate(),
                    booking.getDuration(),
                    booking.getMemberName(),
                    booking.getEmail(),
                    booking.getPhoneNumber(),
                    booking.getAccountNo(),
                    booking.getDayOfWeek()
            );
            textViewBookingInfo.setText(bookingDetails);

            buttonEdit.setOnClickListener(v -> {
                // Start EditBookingActivity with the booking data
                Intent intent = new Intent(v.getContext(), EditBookingActivity.class);
                intent.putExtra("booking", booking);
                v.getContext().startActivity(intent);
            });

            buttonDelete.setOnClickListener(v -> listener.onDeleteClick(booking));
        }
    }
}


