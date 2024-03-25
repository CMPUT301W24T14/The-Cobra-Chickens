package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Parcelable {

    private String eventId;
    private String eventName;
    private String eventMaxAttendees;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String eventPoster;
    private String checkInCode;
    private String promoCode;
    private ArrayList<String> eventAnnouncements;
    private ArrayList<String> checkedInUsers;
    private ArrayList<String> signedUpUsers;

    public Event(String eventId, String eventName, String eventMaxAttendees, String eventDate, String eventTime,String eventLocation, String eventPoster,
                 String checkInCode, String promoCode,
                 ArrayList<String> eventAnnouncements,
                 ArrayList<String> signedUpUsers, ArrayList<String> checkedInUsers) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.eventMaxAttendees = eventMaxAttendees;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventPoster = eventPoster;

        this.checkInCode = checkInCode;
        this.promoCode = promoCode;

        this.eventAnnouncements = eventAnnouncements;
        this.signedUpUsers = signedUpUsers;
        this.checkedInUsers = checkedInUsers;

    }

    public Event (String eventId, String eventName, String eventMaxAttendees, String eventDate, String eventTime,String eventLocation, String eventPoster){
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventMaxAttendees = eventMaxAttendees;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventPoster = eventPoster;
    }

    protected Event(Parcel in) {
        eventId = in.readString();
        eventName = in.readString();
        eventMaxAttendees = in.readString();
        eventDate = in.readString();
        eventTime = in.readString();
        eventLocation = in.readString();
        eventPoster = in.readString();

        checkInCode = in.readString();
        promoCode = in.readString();

        eventAnnouncements = in.createStringArrayList();
        signedUpUsers = in.createStringArrayList();
        checkedInUsers = in.createStringArrayList();
    }

    public String getCheckInCode() {
        return checkInCode;
    }

    public void setCheckInCode(String checkInCode) {
        this.checkInCode = checkInCode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventPoster() {
        return eventPoster;
    }

    public void setEventPoster(String eventPoster) {
        this.eventPoster = eventPoster;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public ArrayList<String> getCheckedInUsers() {
        return checkedInUsers;
    }

    public void setCheckedInUsers(ArrayList<String> checkedInUsers) {
        this.checkedInUsers = checkedInUsers;
    }

    public String getEventMaxAttendees() {
        return eventMaxAttendees;
    }

    public void setEventMaxAttendees(String eventMaxAttendees) {
        this.eventMaxAttendees = eventMaxAttendees;
    }

    public ArrayList<String> getSignedUpUsers() {
        return signedUpUsers;
    }

    public void setSignedUpUsers(ArrayList<String> signedUpUsers) {
        this.signedUpUsers = signedUpUsers;
    }

    public ArrayList<String> getEventAnnouncements() {
        return eventAnnouncements;
    }

    public void setEventAnnouncements(ArrayList<String> eventAnnouncements) {
        this.eventAnnouncements = eventAnnouncements;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventName);
        dest.writeString(eventMaxAttendees);
        dest.writeString(eventDate);
        dest.writeString(eventTime);
        dest.writeString(eventLocation);
        dest.writeString(eventPoster);
        dest.writeStringList(eventAnnouncements);
        dest.writeStringList(signedUpUsers);
        dest.writeStringList(checkedInUsers);
    }
}