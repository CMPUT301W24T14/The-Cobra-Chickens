package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class Event implements Parcelable {
    String eventName;
    Date eventDate;
    String poster;

    ArrayList<String> announcements;

    //if both poster and announcements are available
    public Event(String poster, String eventName, Date eventDate, ArrayList<String> announcements) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.poster = poster;
        this.announcements = announcements;
    }

    //if announcements is available, but poster are not available
    public Event(String eventName, Date eventDate, ArrayList<String> announcements){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.announcements = announcements;
    }

    //if poster available but announcements are not available
    public Event(String poster, String eventName, Date eventDate){
        this.poster = poster;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }


    //if poster and announcements both unavailable
    public Event(String eventName, Date eventDate){

        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    protected Event(Parcel in) {
        eventName = in.readString();
        eventDate = new Date(in.readLong());
        poster = in.readString();
        announcements = in.createStringArrayList();
    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }



    public ArrayList<String> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<String> announcements) {
        this.announcements = announcements;
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
        dest.writeString(eventName);
        dest.writeLong(eventDate.getTime());
        dest.writeString(poster);
        dest.writeStringList(announcements);
    }
}

//comment to commit (ignore)