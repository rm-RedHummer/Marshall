package com.jarvis.marshall.model;

/**
 * Created by Jarvis on 04/03/2018.
 */

public class Comment {
    private String name,date,time,comment,eventKey,key;
    public Comment(String key, String eventKey, String name, String comment, String date, String time){
        this.key = key;
        this.eventKey = eventKey;
        this.name = name;
        this.comment = comment;
        this.date = date;
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getKey() {
        return key;
    }
}
