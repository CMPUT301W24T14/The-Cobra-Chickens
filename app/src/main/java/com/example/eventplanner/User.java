package com.example.eventplanner;

public class User {
    String img;
    String username, usercontact, userhomepage;
    Boolean location;

    public User(String img, String username, String usercontact, String userhomepage, Boolean location) {
        this.img = img;
        this.username = username;
        this.usercontact = usercontact;
        this.userhomepage = userhomepage;
        this.location = location;
    }

    public User(String username, String usercontact, String userhomepage, Boolean location) {
        this.username = username;
        this.usercontact = usercontact;
        this.userhomepage = userhomepage;
        this.location = location;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsercontact() {
        return usercontact;
    }

    public void setUsercontact(String usercontact) {
        this.usercontact = usercontact;
    }

    public String getUserhomepage() {
        return userhomepage;
    }

    public void setUserhomepage(String userhomepage) {
        this.userhomepage = userhomepage;
    }

    public Boolean getLocation() {
        return location;
    }

    public void setLocation(Boolean location) {
        this.location = location;
    }
}
