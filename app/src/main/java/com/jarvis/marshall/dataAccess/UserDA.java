package com.jarvis.marshall.dataAccess;

import android.util.Log;

import com.google.firebase.database.Query;
import com.jarvis.marshall.model.User;

import java.util.ArrayList;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class UserDA extends DA {
    private final String node = "user";
    public void createNewUser(User user){
        rootRef.child(node).child(user.getKey()).setValue(user);
    }
}
