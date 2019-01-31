package com.piickr.zee.HomeClasses.Home;

public class FeedItemHome {
    private String id, uid, body, date, media, misc;

    public FeedItemHome() {
    }

    public FeedItemHome(String id, String uid, String body, String date, String media, String misc) {
        super();
        this.id = id;
        this.uid = uid;
        this.body = body;
        this.date = date;
        this.media = media;
        this.misc = misc;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }
}
