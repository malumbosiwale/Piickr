package com.piickr.zee.HomeClasses.Messages.chat;


public class FeedItemMessages {
    private String idSender, idReceiver, text;
    private long timestamp;

    public FeedItemMessages() {
    }

    public FeedItemMessages(String idSender, String idReceiver, String text, long timestamp) {
        super();
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
