package com.jarvis.marshall.model;

import java.util.ArrayList;

/**
 * Created by Jarvis on 03/02/2018.
 */

public class Event {
    private String name, date, startTime, endTime,venue, description, status, key, groupKey;
    private ArrayList<String> eventMembers;

    public Event(String name, String date, String startTime, String endTime, String venue,
                 String description, String status, String key, String groupKey){
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.description = description;
        this.status = status;
        this.key = key;
        this.groupKey = groupKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public void setEventMembers(ArrayList<String> eventMembers) {
        this.eventMembers = eventMembers;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getVenue() {
        return venue;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public ArrayList<String> getEventMembers() {
        return eventMembers;
    }


}
