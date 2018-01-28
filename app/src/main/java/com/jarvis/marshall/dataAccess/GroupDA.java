package com.jarvis.marshall.dataAccess;

import android.support.design.widget.Snackbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.model.Group;

import java.util.ArrayList;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class GroupDA extends DA{
    private final String node = "group";
    public void createNewGroup(Group group){
        //String groupCode = id.substring(id.length()-7,id.length()-1);
        rootRef.child(node).child(group.getKey()).setValue(group);
    }

    public void deleteGroup(String key) {
        rootRef.child(node).child(key).removeValue();
    }

    public Query checkGroupCode(String groupCode) {
        return rootRef.child(node).orderByChild("groupCode").equalTo(groupCode);
    }

    public Query checkGroupNames(String groupName) {
        return rootRef.child(node).orderByChild("groupName").equalTo(groupName);
    }
}
