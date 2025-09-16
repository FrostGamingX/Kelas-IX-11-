package com.example.adminapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.adminapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void showNotification(String title, String body) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "food_order_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Food Orders", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification)  // Buat icon
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(1, builder.build());
    }

    @Override
    public void onNewToken(String token) {
        // Simpan token ke database untuk kirim notif
    }
}