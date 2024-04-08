package com.example.eventplanner;

public class Repository {

    private final ApiManager apiManager;

    public Repository() {
        this.apiManager = new ApiManager();
    }

    public void sendNotification(PushNotification notification) {
        apiManager.postNotification(notification);
    }
}
