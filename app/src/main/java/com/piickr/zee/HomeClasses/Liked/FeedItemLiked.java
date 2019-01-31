package com.piickr.zee.HomeClasses.Liked;

public class FeedItemLiked {
    private String uid, status, misc;

    public FeedItemLiked() {
    }

    public FeedItemLiked(String uid, String status, String misc) {
        super();
        this.uid = uid;
        this.status = status;
        this.misc = misc;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
