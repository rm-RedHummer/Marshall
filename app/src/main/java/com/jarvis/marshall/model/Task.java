package com.jarvis.marshall.model;

import java.util.ArrayList;

/**
 * Created by Jarvis on 13/02/2018.
 */

public class Task {
    private String key, eventKey, name, details, deadlineDate, status, deadlineTime, remarks;
    private ArrayList<String> members;

    public Task(String key, String eventKey, String name, String details, String deadlineDate, String status, String deadlineTime, String remarks) {
        this.key = key;
        this.eventKey = eventKey;
        this.name = name;
        this.details = details;
        this.deadlineDate = deadlineDate;
        this.status = status;
        this.deadlineTime = deadlineTime;
        this.remarks = remarks;
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}
