package com.piickr.zee.LoginClasses;

public class FeedItemProfile {
    private String  uid, photouri, username, email, age, location, interests, gender;

    public FeedItemProfile() {
    }

    public FeedItemProfile(String uid, String photouri, String username, String email, String age, String location, String interests, String gender) {
        super();
        this.uid = uid;
        this.photouri = photouri;
        this.username = username;
        this.email = email;
        this.age = age;
        this.location = location;
        this.interests = interests;
        this.gender = gender;
    }

    public String getPhotouri() {
        return photouri;
    }

    public void setPhotouri(String photouri) {
        this.photouri = photouri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
