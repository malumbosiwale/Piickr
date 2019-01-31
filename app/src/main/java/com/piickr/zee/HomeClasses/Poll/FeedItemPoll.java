package com.piickr.zee.HomeClasses.Poll;

public class FeedItemPoll {
    private String id, title, description, date, media, field1title, field2title, field3title, field4title, uid;
    private String[] voted;
    private int field1votes, field2votes, field3votes, field4votes;

    public FeedItemPoll() {
    }

    public FeedItemPoll(String id,String uid, String title, String description, String date, String field1title, String field2title,
                        int field1votes, int field2votes, int field3votes, int field4votes, String[] voted, String field3title, String field4title) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.uid = uid;
        this.field1votes = field1votes;
        this.field2votes = field2votes;
        this.field3votes = field3votes;
        this.field4votes = field4votes;
        this.voted = voted;
        this.field1title = field1title;
        this.field2title = field2title;
        this.field3title = field3title;
        this.field4title = field4title;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String[] getVoted() {
        return voted;
    }

    public void setVoted(String[] voted) {
        this.voted = voted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getField1votes() {
        return field1votes;
    }

    public void setField1votes(int field1votes) {
        this.field1votes = field1votes;
    }

    public int getField2votes() {
        return field2votes;
    }

    public void setField2votes(int field2votes) {
        this.field2votes = field2votes;
    }

    public int getField3votes() {
        return field3votes;
    }

    public void setField3votes(int field3votes) {
        this.field3votes = field3votes;
    }

    public int getField4votes() {
        return field4votes;
    }

    public void setField4votes(int field4votes) {
        this.field4votes = field4votes;
    }

    public String getField4title() {
        return field4title;
    }

    public void setField4title(String field4title) {
        this.field4title = field4title;
    }

    public String getField3title() {
        return field3title;
    }

    public void setField3title(String field3title) {
        this.field3title = field3title;
    }

    public String getField2title() {
        return field2title;
    }

    public void setField2title(String field2title) {
        this.field2title = field2title;
    }

    public String getField1title() {
        return field1title;
    }

    public void setField1title(String field1title) {
        this.field1title = field1title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
