// OpenAI, 2024, ChatGPT
package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User implements Parcelable {
    private String userId;
    private String name;
    private String homepage;
    private String contactInformation;
    private String profilePicture;
    private Boolean geolocationTrackingEnabled;
    private ArrayList<String> signedUpForEventList;
    private ArrayList<String> organizingEventsList;
    private ArrayList <String> checkedInEventsList;


    public User(String userId, String name, String homepage, String contactInformation, String profilePicture, Boolean geolocationTrackingEnabled,
                ArrayList<String> signedUpForEventList, ArrayList<String> organizingEventsList, ArrayList<String> checkedInEventsList) {

        this.userId = userId;
        this.name = name;
        this.homepage = homepage;
        this.contactInformation = contactInformation;
        this.profilePicture = profilePicture;
        this.geolocationTrackingEnabled = geolocationTrackingEnabled;
        this.signedUpForEventList = signedUpForEventList;
        this.organizingEventsList = organizingEventsList;
        this.checkedInEventsList = checkedInEventsList;
    }

    public User(String userId, String name, String homepage, String contactInformation, String profilePicture){
        this.userId = userId;
        this.name = name;
        this.homepage = homepage;
        this.contactInformation = contactInformation;
        this.profilePicture = profilePicture;
    }

    protected User(Parcel in) {
        userId = in.readString();
        name = in.readString();
        homepage = in.readString();
        contactInformation = in.readString();
        profilePicture = in.readString();
        byte tmpGeolocationTrackingEnabled = in.readByte();
        geolocationTrackingEnabled = tmpGeolocationTrackingEnabled == 0 ? null : tmpGeolocationTrackingEnabled == 1;
        signedUpForEventList = in.createStringArrayList();
        organizingEventsList = in.createStringArrayList();
        checkedInEventsList = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public String getUserId(){return userId;}
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

    public ArrayList<String> getSignedUpForEventList() {
        return signedUpForEventList;
    }

    public void setSignedUpForEventList(ArrayList<String> signedUpForEventList) {
        this.signedUpForEventList = signedUpForEventList;
    }

    public ArrayList<String> getOrganizingEventsList() {
        return organizingEventsList;
    }

    public void setOrganizingEventsList(ArrayList<String> organizingEventsList) {
        this.organizingEventsList = organizingEventsList;
    }

    public ArrayList<String> getCheckedInEventsList() {
        return checkedInEventsList;
    }

    public void setCheckedInEventsList(ArrayList<String> checkedInEventsList) {
        this.checkedInEventsList = checkedInEventsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(homepage);
        dest.writeString(contactInformation);
        dest.writeString(profilePicture);
        dest.writeByte((byte) (geolocationTrackingEnabled == null ? 0 : geolocationTrackingEnabled ? 1 : 2));
        dest.writeStringList(signedUpForEventList);
        dest.writeStringList(organizingEventsList);
        dest.writeStringList(checkedInEventsList);
    }
}