package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Parcelable {

    String eventId;
    String eventName;
    Date eventDate;
    String eventLocation;
    String eventPoster;
    ArrayList<String> eventAnnouncements;
    ArrayList<String> checkedInUsers;
    ArrayList<String> signedUpUsers;

    //if both poster and announcements are available
    public Event(String eventPoster, String eventName, Date eventDate, ArrayList<String> announcements) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventPoster = eventPoster;
        this.eventAnnouncements = announcements;
    }

    //if announcements is available, but poster are not available
    public Event(String eventName, Date eventDate, ArrayList<String> announcements){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventAnnouncements = announcements;
    }

    //if poster available but announcements are not available
    public Event(String eventPoster, String eventName, Date eventDate){
        this.eventPoster = eventPoster;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }


    //if poster and announcements both unavailable
    public Event(String eventName, Date eventDate){

        this.eventName = eventName;
        this.eventDate = eventDate;
    }


    // camille-testing event w/ a location
    public Event(String eventId, String eventName, Date eventDate, String eventLocation) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;

    }

    // camille - testing event w/ all info
    public Event(String eventId, String eventName, Date eventDate, String eventLocation, String eventPoster,
                 ArrayList<String> eventAnnouncements,
                 ArrayList<String> signedUpUsers, ArrayList<String> checkedInUsers) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventPoster = eventPoster;
        this.eventAnnouncements = eventAnnouncements;
        this.signedUpUsers = signedUpUsers;
        this.checkedInUsers = checkedInUsers;
    }

    protected Event(Parcel in) {
        eventId = in.readString();
        eventName = in.readString();
        eventDate = new Date(in.readLong());
        eventLocation = in.readString();
        eventPoster = in.readString();
        eventAnnouncements = in.createStringArrayList();
        signedUpUsers = in.createStringArrayList();
        checkedInUsers = in.createStringArrayList();
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

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
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
        dest.writeLong(eventDate.getTime());
        dest.writeString(eventLocation);
        dest.writeString(eventPoster);
        dest.writeStringList(eventAnnouncements);
        dest.writeStringList(signedUpUsers);
        dest.writeStringList(checkedInUsers);
    }
}

//comment to commit (ignore)