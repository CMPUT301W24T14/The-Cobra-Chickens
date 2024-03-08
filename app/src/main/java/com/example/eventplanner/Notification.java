package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * A Parcelable class representing a notification for an event.
 */
public class Notification implements Parcelable {
    String eventId;
    String eventName;
    String eventDate;
    String eventTime;
    ArrayList<String> eventNotifications;

    // Constructor for all fields
    public Notification(String eventId, String eventName, String eventDate, String eventTime, ArrayList<String> eventNotifications) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventNotifications = eventNotifications;
    }

    protected Notification(Parcel in) {
        eventId = in.readString();
        eventName = in.readString();
        eventDate = in.readString();
        eventTime = in.readString();
        eventNotifications= in.createStringArrayList();
    }

    /**
     * Get the ID of the event.
     *
     * @return The event ID.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Set the ID of the event.
     *
     * @param eventId The event ID to set.
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Get the name of the event.
     *
     * @return The event name.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Set the name of the event.
     *
     * @param eventName The event name to set.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Get the date of the event.
     *
     * @return The event date.
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Set the date of the event.
     *
     * @param eventDate The event date to set.
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Get the time of the event.
     *
     * @return The event time.
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * Set the time of the event.
     *
     * @param eventTime The event time to set.
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Get the list of notifications for the event.
     *
     * @return The list of notifications.
     */
    public ArrayList<String> getEventNotifications() {
        return eventNotifications;
    }

    /**
     * Set the list of notifications for the event.
     *
     * @param eventNotifications The list of notifications to set.
     */
    public void setEventNotifications(ArrayList<String> eventNotifications) {
        this.eventNotifications = eventNotifications;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventName);
        dest.writeString(eventDate);
        dest.writeString(eventTime);
        dest.writeStringList(eventNotifications);
    }
}
