package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
/**
 The Event class represents an event in the event planner application.
 It implements the Parcelable interface to allow for passing instances of this class between components.
 */
public class Event implements Parcelable {

    private String eventId;
    private String eventName;
    private String eventDescription;
    private String eventMaxAttendees;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String eventPoster;
    private String checkInCode;
    private String promoCode;
    private ArrayList<String> eventAnnouncements;
    private ArrayList<CheckedInUser> checkedInUsers;
    private ArrayList<String> signedUpUsers;
    /**
     * Constructs a new Event instance with all details.
     *
     * @param eventId            The ID of the event.
     * @param eventName          The name of the event.
     * @param eventDescription   The description of the event.
     * @param eventMaxAttendees  The maximum number of attendees for the event.
     * @param eventDate          The date of the event.
     * @param eventTime          The time of the event.
     * @param eventLocation      The location of the event.
     * @param eventPoster        The poster image URL of the event.
     * @param checkInCode        The check-in code for the event.
     * @param promoCode          The promo code for the event.
     * @param eventAnnouncements The announcements related to the event.
     * @param checkedInUsers     The list of users checked into the event.
     * @param signedUpUsers      The list of users signed up for the event.
     */
    public Event(String eventId,
                 String eventName, String eventDescription, String eventMaxAttendees, String eventDate, String eventTime, String eventLocation, String eventPoster,
                 String checkInCode, String promoCode,
                 ArrayList<String> eventAnnouncements,
                 ArrayList<CheckedInUser> checkedInUsers, ArrayList<String> signedUpUsers) {

        this.eventId = eventId;

        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventMaxAttendees = eventMaxAttendees;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventPoster = eventPoster;

        this.checkInCode = checkInCode;
        this.promoCode = promoCode;

        this.eventAnnouncements = eventAnnouncements;

        this.checkedInUsers = checkedInUsers;
        this.signedUpUsers = signedUpUsers;
    }

    public Event (String eventId, String eventName, String eventMaxAttendees, String eventDate, String eventTime, String eventLocation, String eventPoster){
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
        eventDescription = in.readString();
        eventMaxAttendees = in.readString();
        eventDate = in.readString();
        eventTime = in.readString();
        eventLocation = in.readString();
        eventPoster = in.readString();

        checkInCode = in.readString();
        promoCode = in.readString();

        eventAnnouncements = in.createStringArrayList();

        checkedInUsers = in.createTypedArrayList(CheckedInUser.CREATOR);
        signedUpUsers = in.createStringArrayList();

    }
    /**
     * Sets the check-in code for the event.
     * @return checkInCode The check-in code to set.
     */
    public String getCheckInCode() {
        return checkInCode;
    }
    /**
     * Sets the check-in code for the event.
     *
     * @param checkInCode The check-in code to set.
     */
    public void setCheckInCode(String checkInCode) {
        this.checkInCode = checkInCode;
    }
    /**
     * Retrieves the promo code for the event.
     *
     * @return The promo code.
     */
    public String getPromoCode() {
        return promoCode;
    }
    /**
     * Sets the promo code for the event.
     *
     * @param promoCode The promo code to set.
     */
    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
    /**
     * Retrieves the ID of the event.
     *
     * @return The event ID.
     */
    public String getEventId() {
        return eventId;
    }
    /**
     * Retrieves the URL of the event poster image.
     *
     * @return The URL of the event poster.
     */
    public String getEventPoster() {
        return eventPoster;
    }
    /**
     * Sets the URL of the event poster image.
     *
     * @param eventPoster The URL of the event poster to set.
     */
    public void setEventPoster(String eventPoster) {
        this.eventPoster = eventPoster;
    }
    /**
     * Sets the ID of the event.
     *
     * @param eventId The event ID to set.
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    /**
     * Retrieves the date of the event.
     *
     * @return The event date.
     */
    public String getEventDate() {
        return eventDate;
    }
    /**
     * Sets the date of the event.
     *
     * @param eventDate The event date to set.
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    /**
     * Retrieves the time of the event.
     *
     * @return The event time.
     */
    public String getEventTime() {
        return eventTime;
    }
    /**
     * Sets the time of the event.
     *
     * @param eventTime The event time to set.
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    /**
     * Retrieves the list of users checked into the event.
     *
     * @return The list of checked-in users.
     */
    public ArrayList<CheckedInUser> getCheckedInUsers() {
        return checkedInUsers;
    }
    /**
     * Sets the list of users checked into the event.
     *
     * @param checkedInUsers The list of checked-in users to set.
     */
    public void setCheckedInUsers(ArrayList<CheckedInUser> checkedInUsers) {
        this.checkedInUsers = checkedInUsers;
    }
    /**
     * Retrieves the maximum number of attendees for the event.
     *
     * @return The maximum number of attendees.
     */
    public String getEventMaxAttendees() {
        return eventMaxAttendees;
    }
    /**
     * Sets the maximum number of attendees for the event.
     *
     * @param eventMaxAttendees The maximum number of attendees to set.
     */
    public void setEventMaxAttendees(String eventMaxAttendees) {
        this.eventMaxAttendees = eventMaxAttendees;
    }
    /**
     * Retrieves the list of users signed up for the event.
     *
     * @return The list of signed-up users.
     */
    public ArrayList<String> getSignedUpUsers() {
        return signedUpUsers;
    }
    /**
     * Sets the list of users signed up for the event.
     *
     * @param signedUpUsers The list of signed-up users to set.
     */
    public void setSignedUpUsers(ArrayList<String> signedUpUsers) {
        this.signedUpUsers = signedUpUsers;
    }
    /**
     * Retrieves the list of event announcements.
     *
     * @return The list of event announcements.
     */
    public ArrayList<String> getEventAnnouncements() {
        return eventAnnouncements;
    }
    /**
     * Sets the list of event announcements.
     *
     * @param eventAnnouncements The list of event announcements to set.
     */
    public void setEventAnnouncements(ArrayList<String> eventAnnouncements) {
        this.eventAnnouncements = eventAnnouncements;
    }
    /**
     * Retrieves the name of the event.
     *
     * @return The event name.
     */
    public String getEventName() {
        return eventName;
    }
    /**
     * Sets the name of the event.
     *
     * @param eventName The event name to set.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    /**
     * Retrieves the location of the event.
     *
     * @return The event location.
     */
    public String getEventLocation() {
        return eventLocation;
    }
    /**
     * Sets the location of the event.
     *
     * @param eventLocation The event location to set.
     */
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
    /**
     * Retrieves the description of the event.
     *
     * @return eventDescription.
     */
    public String getEventDescription() {
        return eventDescription;
    }
    /**
     * Sets the description of the event.
     *
     * @param eventDescription event description to set.
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
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
    /**
     * Writes content of an event into a parcel
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventName);
        dest.writeString(eventDescription);
        dest.writeString(eventMaxAttendees);
        dest.writeString(eventDate);
        dest.writeString(eventTime);
        dest.writeString(eventLocation);
        dest.writeString(eventPoster);

        dest.writeString(checkInCode);
        dest.writeString(promoCode);

        dest.writeStringList(eventAnnouncements);

        dest.writeTypedList(checkedInUsers);
        dest.writeStringList(signedUpUsers);
    }
}