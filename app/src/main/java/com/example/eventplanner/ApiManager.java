package com.example.eventplanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

/**
 * The ApiManager class is responsible for sending notifications.
 */
public class ApiManager {
    /**
     * Sends a push notification by making method makes an asynchronous API call.
     * @param notification The PushNotification object containing the message to be sent.
     */
    public void postNotification(PushNotification notification) {
        Call<ResponseBody> call = RetrofitInstance.getApi().postNotification(notification);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("Sending data was successful - notification recipient: " + notification.getTo());
                } else {
                    System.out.println("Error sending the data");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Error occurred during the API call: " + t.getMessage());
            }
        });
    }
}