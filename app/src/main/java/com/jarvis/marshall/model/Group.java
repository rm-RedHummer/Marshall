package com.jarvis.marshall.model;

import java.util.ArrayList;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class Group {
    private String key,groupName,groupCode;
    private ArrayList<String> groupMembers;
    public Group(String groupName, String key, String groupCode) {
        this.groupName = groupName;
        this.key = key;
        this.groupCode = groupCode;
    }
    public String getGroupName(){
        return groupName;
    }
    public String getKey(){
        return key;
    }
    public String getGroupCode() { return groupCode; }
    public ArrayList<String> getGroupMembers() { return groupMembers; }

    public void setGroupName(String groupName) { this.groupName = groupName ; }
    public void setGroupCode(String groupCode) { this.groupCode = groupCode ; }
    public void setKey(String key){
        this.key=key;
    }
    public void setGroupMembers(ArrayList<String> groupMembers) { this.groupMembers = groupMembers; }

}
