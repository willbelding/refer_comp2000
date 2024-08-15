package com.example.wb_tennis_app.services;

import com.example.wb_tennis_app.models.Booking;
import com.example.wb_tennis_app.models.BookingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class BookingService {

    private static final String BASE_URL = "https://web.socem.plymouth.ac.uk/COMP2000/ReferralApi/api/";

    private BookingApi bookingApi;

    public BookingService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookingApi = retrofit.create(BookingApi.class);
    }

    public Call<List<Booking>> getAllBookings() {
        return bookingApi.getAllBookings();
    }

    public Call<BookingResponse> createBooking(Booking booking) {
        return bookingApi.createBooking(booking);
    }

    public Call<BookingResponse> updateBooking(int id, Booking booking) {
        return bookingApi.updateBooking(id, booking);
    }

    public Call<Void> deleteBooking(int id) {
        return bookingApi.deleteBooking(id);
    }

    public interface BookingApi {
        @GET("Bookings")
        Call<List<Booking>> getAllBookings();

        @POST("Bookings")
        Call<BookingResponse> createBooking(@Body Booking booking);

        @PUT("Bookings/{id}")
        Call<BookingResponse> updateBooking(@Path("id") int id, @Body Booking booking);

        @DELETE("Bookings/{id}")
        Call<Void> deleteBooking(@Path("id") int id);
    }
}
