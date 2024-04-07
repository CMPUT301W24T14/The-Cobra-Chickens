/**
 The CheckedInUser class represents a user who has checked in to an event.
 It implements the Parcelable interface to allow for passing instances of this class between components.
 */
package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CheckedInUser implements Parcelable {

    private String userId;
    private String numberOfCheckins;
    /**
     * Constructs a new CheckedInUser instance.
     *
     * @param userId           The ID of the user who checked in.
     * @param numberOfCheckins The number of check-ins for the user.
     */
    public CheckedInUser(String userId, String numberOfCheckins) {
        this.userId = userId;
        this.numberOfCheckins = numberOfCheckins;
    }

    protected CheckedInUser(Parcel in) {
        userId = in.readString();
        numberOfCheckins = in.readString();
    }
    /**
     * Retrieves the user ID.
     *
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Sets the user ID.
     *
     * @param userId The user ID to set.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * Retrieves the number of check-ins.
     *
     * @return The number of check-ins.
     */
    public String getNumberOfCheckins() {
        return numberOfCheckins;
    }
    /**
     * Sets the number of check-ins.
     *
     * @param numberOfCheckins The number of check-ins to set.
     */
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
    /**
     * Converts the CheckedInUser instance to a map for Firebase Firestore.
     *
     * @return A map representation of the CheckedInUser instance.
     */
    private Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put(userId, numberOfCheckins);

        return map;
    }
}
