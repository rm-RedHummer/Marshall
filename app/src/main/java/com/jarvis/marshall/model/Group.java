package com.jarvis.marshall.model;

import java.util.ArrayList;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class Group {
    private String key,groupName,groupCode;
    private ArrayList<String> groupMembers;
    public Group(String groupName,ArrayList<String> groupMembers) {
        this.groupName = groupName;
        this.groupMembers = groupMembers;
    }
    public String getGroupName(){
        return groupName;
    }
    public String getKey(){
        return key;
    }
    public ArrayList<String> getGroupMembers() { return groupMembers; }
    public String getGroupCode() { return groupCode; }

    public void setGroupCode(String groupCode) { this.groupCode = groupCode ; }
    public void setKey(String key){
        this.key=key;
    }
}
