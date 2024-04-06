package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CheckedInUser implements Parcelable {

    private String userId;
    private String numberOfCheckins;

    public CheckedInUser(String userId, String numberOfCheckins) {
        this.userId = userId;
        this.numberOfCheckins = numberOfCheckins;
    }

    protected CheckedInUser(Parcel in) {
        userId = in.readString();
        numberOfCheckins = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNumberOfCheckins() {
        return numberOfCheckins;
    }

    public void setNumberOfCheckins(String numberOfCheckins) {
        this.numberOfCheckins = numberOfCheckins;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(numberOfCheckins);
    }

    public static final Creator<CheckedInUser> CREATOR = new Creator<CheckedInUser>() {
        @Override
        public CheckedInUser createFromParcel(Parcel in) {
            return new CheckedInUser(in);
        }

        @Override
        public CheckedInUser[] newArray(int size) {
            return new CheckedInUser[size];
        }
    };

    private Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put(userId, numberOfCheckins);

        return map;
    }
}
