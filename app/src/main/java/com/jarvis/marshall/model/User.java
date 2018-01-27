package com.jarvis.marshall.model;

import java.util.ArrayList;

/**
 * Created by Jarvis on 31/12/2017.
 */

public class User {
    private String key,name;
    private ArrayList<String> groupList;
    public User(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }
    public String getName() {
        return name;
    }
    public ArrayList<String> getGroupList() { return groupList; }
    public void setGroupList(ArrayList<String> groupList) { this.groupList=groupList; }
}
