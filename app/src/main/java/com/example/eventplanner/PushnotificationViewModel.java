package com.example.eventplanner;

import androidx.lifecycle.ViewModel;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PushnotificationViewModel extends ViewModel {

    private final Repository repository = new Repository();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void subscribeToNewTopic(String topicInput, TopicCallback callback) {
        FirebaseMessaging.getInstance().subscribeToTopic(topicInput)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSubscribed();
                        System.out.println("successfully subscribed to the topic");
                    } else {
                        System.out.println("failed to subscribe to the topic");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("failed to subscribe to the topic: " + e.getMessage());
                });
    }

    public void sendNewMessage(PushNotification notification) {
        executorService.submit(() -> {
            repository.sendNotification(notification);
        });
    }
}
