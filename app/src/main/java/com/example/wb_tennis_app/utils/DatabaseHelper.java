package com.example.wb_tennis_app.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tennis_app.db";
    private static final int DATABASE_VERSION = 2; // Updated version number

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "account_no TEXT UNIQUE NOT NULL);");

        db.execSQL("CREATE TABLE bookings (" +
                "booking_no INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "courtNo TEXT, " +
                "date TEXT, " +
                "duration TEXT, " +
                "memberName TEXT, " +
                "accountNo TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS bookings");
        onCreate(db);
    }
}
