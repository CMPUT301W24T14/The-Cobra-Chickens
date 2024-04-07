// OpenAI, 2024, ChatGPT
package com.example.eventplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
/**
 * User class represents a user in the application.
 * It contains user information such as name, homepage, contact information, profile picture, and various lists related to events.
 */
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
    private ArrayList<String> reusableCodes;

    /**
     * Constructs a new User object with specified parameters.
     * @param userId The unique identifier of the user.
     * @param name The name of the user.
     * @param homepage The homepage URL of the user.
     * @param contactInformation The contact information of the user.
     * @param profilePicture The URL of the user's profile picture.
     * @param geolocationTrackingEnabled Indicates whether geolocation tracking is enabled for the user.
     * @param signedUpForEventList The list of event IDs that the user has signed up for.
     * @param organizingEventsList The list of event IDs that the user is organizing.
     * @param checkedInEventsList The list of event IDs that the user has checked into.
     * @param reusableCodes The list of reusable codes associated with the user.
     */
    public User(String userId, String name, String homepage, String contactInformation, String profilePicture, Boolean geolocationTrackingEnabled,
                ArrayList<String> signedUpForEventList, ArrayList<String> organizingEventsList, ArrayList<String> checkedInEventsList, ArrayList<String> reusableCodes) {

        this.userId = userId;
        this.name = name;
        this.homepage = homepage;
        this.contactInformation = contactInformation;
        this.profilePicture = profilePicture;
        this.geolocationTrackingEnabled = geolocationTrackingEnabled;
        this.signedUpForEventList = signedUpForEventList;
        this.organizingEventsList = organizingEventsList;
        this.checkedInEventsList = checkedInEventsList;

        this.reusableCodes = reusableCodes;
    }
    /**
     * Constructs a new User object with minimal parameters.
     * @param userId The unique identifier of the user.
     * @param name The name of the user.
     * @param homepage The homepage URL of the user.
     * @param contactInformation The contact information of the user.
     * @param profilePicture The URL of the user's profile picture.
     */
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

        reusableCodes = in.createStringArrayList();
    }

    public  static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    /**
     * Returns the name of the user.
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the user.
     * @param name The name to be set for the user.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Returns the homepage URL of the user.
     * @return The homepage URL of the user.
     */
    public String getHomepage() {
        return homepage;
    }
    /**
     * Sets the homepage URL of the user.
     * @param homepage The homepage URL to be set for the user.
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    /**
     * Returns the contact information of the user.
     * @return The contact information of the user.
     */
    public String getContactInformation() {
        return contactInformation;
    }
    /**
     * Sets the contact information of the user.
     * @param contactInformation The contact information to be set for the user.
     */
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
    /**
     * Returns the unique identifier of the user.
     * @return The unique identifier of the user.
     */
    public String getUserId(){return userId;}
    /**
     * Returns the URL of the user's profile picture.
     * @return The URL of the user's profile picture.
     */
    public String getProfilePicture() {
        return profilePicture;
    }
    /**
     * Sets the URL of the user's profile picture.
     * @param profilePicture The URL of the profile picture to be set for the user.
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    /**
     * Returns a boolean indicating whether geolocation tracking is enabled for the user.
     * @return True if geolocation tracking is enabled, false otherwise.
     */
    public Boolean getGeolocationTrackingEnabled() {
        return geolocationTrackingEnabled;
    }
    /**
     * Sets whether geolocation tracking is enabled for the user.
     * @param geolocationTrackingEnabled A boolean indicating whether geolocation tracking should be enabled.
     */
    public void setGeolocationTrackingEnabled(Boolean geolocationTrackingEnabled) {
        this.geolocationTrackingEnabled = geolocationTrackingEnabled;
    }
    /**
     * Returns the list of event IDs that the user has signed up for.
     * @return The list of event IDs.
     */
    public ArrayList<String> getSignedUpForEventList() {
        return signedUpForEventList;
    }
    /**
     * Sets the list of event IDs that the user has signed up for.
     * @param signedUpForEventList The list of event IDs to be set.
     */
    public void setSignedUpForEventList(ArrayList<String> signedUpForEventList) {
        this.signedUpForEventList = signedUpForEventList;
    }
    /**
     * Returns the list of event IDs that the user is organizing.
     * @return The list of event IDs.
     */
    public ArrayList<String> getOrganizingEventsList() {
        return organizingEventsList;
    }
    /**
     * Sets the list of event IDs that the user is organizing.
     * @param organizingEventsList The list of event IDs to be set.
     */
    public void setOrganizingEventsList(ArrayList<String> organizingEventsList) {
        this.organizingEventsList = organizingEventsList;
    }
    /**
     * Returns the list of event IDs that the user has checked into.
     * @return The list of event IDs.
     */
    public ArrayList<String> getCheckedInEventsList() {
        return checkedInEventsList;
    }
    /**
     * Sets the list of event IDs that the user has checked into.
     * @param checkedInEventsList The list of event IDs to be set.
     */
    public void setCheckedInEventsList(ArrayList<String> checkedInEventsList) {
        this.checkedInEventsList = checkedInEventsList;
    }
    /**
     * Returns the list of reusable codes associated with the user.
     * @return The list of reusable codes.
     */
    public ArrayList<String> getReusableCodes() {
        return reusableCodes;
    }
    /**
     * Sets the list of reusable codes associated with the user.
     * @param reusableCodes The list of reusable codes to be set.
     */
    public void setReusableCodes(ArrayList<String> reusableCodes) {
        this.reusableCodes = reusableCodes;
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

        dest.writeStringList(reusableCodes);
    }
}