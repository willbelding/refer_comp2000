package com.example.wb_tennis_app.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.wb_tennis_app.models.Booking;
import com.example.wb_tennis_app.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public BookingRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        dbHelper.close();
    }

    public void addBooking(Booking booking) {
        ContentValues values = new ContentValues();
        values.put("courtNo", booking.getCourtNo());
        values.put("date", booking.getDate());
        values.put("duration", booking.getDuration());
        values.put("memberName", booking.getMemberName());
        values.put("accountNo", booking.getAccountNo());

        database.insert("bookings", null, values);
    }

    public List<Booking> getBookingsByAccountNo(String accountNo) {
        List<Booking> bookings = new ArrayList<>();

        Cursor cursor = database.query(
                "bookings",
                null,
                "accountNo = ?",
                new String[]{accountNo},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Booking booking = new Booking();
                    booking.setBookingNo(cursor.getInt(cursor.getColumnIndexOrThrow("booking_no")));
                    booking.setCourtNo(cursor.getString(cursor.getColumnIndexOrThrow("courtNo")));
                    booking.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    booking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow("duration")));
                    booking.setMemberName(cursor.getString(cursor.getColumnIndexOrThrow("memberName")));
                    booking.setAccountNo(cursor.getString(cursor.getColumnIndexOrThrow("accountNo")));

                    bookings.add(booking);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return bookings;
    }

    public void updateBooking(Booking booking) {
        ContentValues values = new ContentValues();
        values.put("courtNo", booking.getCourtNo());
        values.put("date", booking.getDate());
        values.put("duration", booking.getDuration());
        values.put("memberName", booking.getMemberName());
        values.put("accountNo", booking.getAccountNo());

        database.update("bookings", values, "booking_no = ?", new String[]{String.valueOf(booking.getBookingNo())});
    }

    public void deleteBooking(int bookingNo) {
        database.delete("bookings", "booking_no = ?", new String[]{String.valueOf(bookingNo)});
    }
}


