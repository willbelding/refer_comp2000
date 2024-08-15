package com.example.wb_tennis_app.services;

import com.example.wb_tennis_app.models.Booking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("bookings")
    Call<Void> bookCourt(@Body Booking booking);
}
