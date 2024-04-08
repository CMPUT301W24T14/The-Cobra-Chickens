package com.example.eventplanner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 100;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");

            // Assuming both title and message are required to proceed
            if (title != null && message != null) {
                NotificationData notificationData = new NotificationData(title, message);
                sendNotification(notificationData);
            } else {
                Log.w(TAG, "The notification data did not contain a title and message.");
            }
        }
    }

    // If you intend on generating your own notifications, implement this method
    private void sendNotification(NotificationData notificationData) {
        Context context = getApplicationContext();

        // Create a notification and set the notification content
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.rounded_notifications_24)
                .setContentTitle(notificationData.getTitle())
                .setContentText(notificationData.getMessage())
                .setAutoCancel(true); // Dismiss the notification when it's tapped

        // Get the NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // For Android Oreo and above, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel Human Readable Title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    @Override
    public void onNewToken(String token) {
        // This method is called when a new token is generated for the device
        // Subscribe to the event name topic
        String eventName = SharedPreferencesHelper.getCurrentTopic(getApplicationContext());
        String formattedeventname = eventName.replaceAll("\\s+" ,"_");
        FirebaseMessaging.getInstance().subscribeToTopic(eventName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Subscribed to topic: " + formattedeventname);
                    } else {
                        Log.e(TAG, "Failed to subscribe to topic: " + formattedeventname, task.getException());
                    }
                });
    }
}
