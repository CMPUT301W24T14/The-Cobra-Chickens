package com.example.eventplanner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Service class responsible for handling Firebase Cloud Messaging (FCM) messages.
 * This service extends FirebaseMessagingService and overrides the onMessageReceived method
 * to process incoming messages and generate notifications as needed.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 100;
    /**
     * Called when a message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            // Check if data needs to be processed by long running job
            if (remoteMessage.getData().containsKey("urgent_task")) {
                // If the message contains a key indicating an urgent task, handle it immediately
                handleNow();
            } else {
                // Otherwise, schedule a long-running job
                scheduleJob();
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Handle the generation of your own notifications here if needed
        sendNotification(remoteMessage.getData().get("your_custom_key"));
    }
    /**
     * Schedule a long-running job.
     * This method should be called for non-urgent tasks.
     */
    private void scheduleJob() {
        // Schedule long running job using WorkManager
        // For example: WorkManager.getInstance().enqueue(myLongRunningWorkRequest);
    }
    /**
     * Handle the urgent task immediately.
     * This method should be called for urgent tasks.
     */
    private void handleNow() {
        // Handle time-bound tasks here
    }
    /**
     * Generate and display a notification.
     * @param messageBody The body of the notification message.
     */
    // If you intend on generating your own notifications, implement this method
    private void sendNotification(String messageBody) {
        // Create a notification and set the notification content
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.rounded_notifications_24)
                .setContentTitle("My Notification")
                .setContentText(messageBody)
                .setAutoCancel(true); // Dismiss the notification when clicked

        // Get the notification manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the Android version is greater than or equal to Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
