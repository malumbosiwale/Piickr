package com.piickr.zee.HomeClasses.Messages;

public class FeedItemMyfriends {
    private String index, idReceiver;
    public long timestamp;

    public FeedItemMyfriends() {
    }

    public FeedItemMyfriends(String index, String idReceiver, long timestamp) {
        super();
        this.index = index;
        this.idReceiver = idReceiver;
        this.timestamp = timestamp;
    }


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
