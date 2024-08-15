package com.example.wb_tennis_app.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Handle notification logic, e.g., register or handle notifications
        Log.d("NotificationService", "Handling notification task in background.");
    }
}
