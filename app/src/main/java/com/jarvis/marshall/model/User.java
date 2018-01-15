package com.jarvis.marshall.model;

import java.util.ArrayList;

/**
 * Created by Jarvis on 31/12/2017.
 */

public class User {
    private String key,name,username;
    private ArrayList<String> groupList;
    public User(String key, String name,String username) {
        this.key = key;
        this.name = name;
        this.username = username;
    }

    public String getKey() {
        return key;
    }
    public String getName() {
        return name;
    }
    public String getUsername() { return username; }
    public ArrayList<String> getGroupList() { return groupList; }
    public void setGroupList(ArrayList<String> groupList) { this.groupList=groupList; }
}
