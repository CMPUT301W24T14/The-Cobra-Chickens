package com.example.eventplanner;

import java.util.ArrayList;
import java.util.Date;

public class Event {
    String eventName;
    Date eventDate;
    String poster;
    String organizer;



    public Event(String eventName, Date eventDate) {
=======
    ArrayList<String> announcements;

    public Event(String eventName, Date eventDate, String poster, String organizer, ArrayList<String> announcements) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.poster = poster;
        this.organizer = organizer;
        this.announcements = announcements;
    }

    public Event(String eventName, Date eventDate){
        Developer
        this.eventName = eventName;
        this.eventDate = eventDate;
    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
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
}
