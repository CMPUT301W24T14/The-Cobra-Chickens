package com.example.eventplanner;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String SHARED_PREFERENCES_NAME = "MySharedPreferences";
    private static final String KEY_CURRENT_TOPIC = "current_topic";

    public static void setCurrentTopic(Context context, String topic) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CURRENT_TOPIC, topic);
        editor.apply();
    }

    public static String getCurrentTopic(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CURRENT_TOPIC, null);
    }
}
