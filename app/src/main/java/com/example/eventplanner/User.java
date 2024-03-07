package com.example.eventplanner;

import java.util.ArrayList;

public class User {
    private String name;
    private String homepage;
    private String contactInformation;
    private String profilePicture;
    private Boolean geolocationTrackingEnabled;
    private ArrayList<Event> signedUpForEventList;
    private ArrayList<Event> organizingEventsList;
    private ArrayList <Event> checkedInEventsList;


    public User(String name, String homepage, String contactInformation, String profilePicture, Boolean geolocationTrackingEnabled,
                ArrayList<Event> signedUpForEventList, ArrayList<Event> organizingEventsList, ArrayList<Event> checkedInEventsList) {

        this.name = name;
        this.homepage = homepage;
        this.contactInformation = contactInformation;
        this.profilePicture = profilePicture;
        this.geolocationTrackingEnabled = geolocationTrackingEnabled;
        this.signedUpForEventList = signedUpForEventList;
        this.organizingEventsList = organizingEventsList;
        this.checkedInEventsList = checkedInEventsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean getGeolocationTrackingEnabled() {
        return geolocationTrackingEnabled;
    }

    public void setGeolocationTrackingEnabled(Boolean geolocationTrackingEnabled) {
        this.geolocationTrackingEnabled = geolocationTrackingEnabled;
    }

    public ArrayList<Event> getSignedUpForEventList() {
        return signedUpForEventList;
    }

    public void setSignedUpForEventList(ArrayList<Event> signedUpForEventList) {
        this.signedUpForEventList = signedUpForEventList;
    }

    public ArrayList<Event> getOrganizingEventsList() {
        return organizingEventsList;
    }

    public void setOrganizingEventsList(ArrayList<Event> organizingEventsList) {
        this.organizingEventsList = organizingEventsList;
    }

    public ArrayList<Event> getCheckedInEventsList() {
        return checkedInEventsList;
    }

    public void setCheckedInEventsList(ArrayList<Event> checkedInEventsList) {
        this.checkedInEventsList = checkedInEventsList;
    }
}
