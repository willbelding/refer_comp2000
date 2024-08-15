package com.example.wb_tennis_app.UserManagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserRepository {

    private SQLiteDatabase database;
    private UserDatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new UserDatabaseHelper(context);
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

    // Method to add a new user to the database without email
    public long addUser(String username, String password, String accountNo) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("account_no", accountNo);
        return database.insert("users", null, values);
    }

    // Method to get a user by username
    public Cursor getUserByUsername(String username) {
        String[] columns = {"id", "username", "password", "account_no"};
        String selection = "username = ?";
        String[] selectionArgs = { username };
        return database.query("users", columns, selection, selectionArgs, null, null, null);
    }

    // Method to update a user's password using the username as the identifier
    public void updateUserPassword(String username, String newPassword) {
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        String whereClause = "username = ?";
        String[] whereArgs = { username };
        database.update("users", values, whereClause, whereArgs);
    }

    // Method to delete a user by username
    public void deleteUser(String username) {
        String whereClause = "username = ?";
        String[] whereArgs = { username };
        database.delete("users", whereClause, whereArgs);
    }
}

