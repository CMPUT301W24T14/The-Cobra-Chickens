package com.example.eventplanner;

public class ApiManager {
    public void postNotification(PushNotification notification) {
        retrofit2.Call<okhttp3.ResponseBody> call = RetrofitInstance.getApi().postNotification(notification);
        call.enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("Sending data was successful - notification recipient: " + notification.getTo());
                } else {
                    System.out.println("Error sending the data");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                System.out.println("Error occurred during the API call: " + t.getMessage());
            }
        });
    }
}