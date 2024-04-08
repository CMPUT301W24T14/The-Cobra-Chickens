package com.example.eventplanner;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApi {

    String CONTENT_TYPE = "application/json";
    String API_KEY = "AAAA_8tNdV4:APA91bHM1iQWM5XJUr2fZxRyWAU29xNjnXaj4tor8pWKMK9zLkn15IyjRyHA1uvVRjjTfJJWevNKTCOXhKsU1Z6cmYK-GHLRBRv6TeugEL702jDncjuGxCogTHB2wuBklINfUcRo8jDn";

    @Headers({"Authorization: key=" + API_KEY, "Content-Type: " + CONTENT_TYPE})
    @POST("fcm/send")
    Call<ResponseBody> postNotification(
            @Body PushNotification notification
    );
}
